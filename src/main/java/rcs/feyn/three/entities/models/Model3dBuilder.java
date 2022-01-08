package rcs.feyn.three.entities.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import rcs.feyn.three.collision.BoundingObject3d;
import rcs.feyn.three.entities.Rotation3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.utils.ArrayUtils;
import rcs.feyn.utils.StringUtils;

public final class Model3dBuilder {
  
  protected Vector3d position = new Vector3d();
  protected Vector3d velocity = new Vector3d();
  protected Rotation3d rotation = null;
  
  protected ArrayList<Vector3d>  vertices   = new ArrayList<>();
  protected ArrayList<Vector3d>  normals    = new ArrayList<>();
  protected ArrayList<Double>    masses     = new ArrayList<>();
  protected ArrayList<Integer[]> faces      = new ArrayList<>();
  protected ArrayList<FeynColor> colors     = new ArrayList<>();
  protected ArrayList<Matrix44>  transforms = new ArrayList<>();
  
  protected BoundingObject3d outerBoundingObject = null;
  protected ArrayList<BoundingObject3d> innerBoundingObjects = new ArrayList<>();

  protected TextureRaster textureData = null;
  protected int textureAlpha = 255;
  protected double textureZoom = 1;

  public Model3dBuilder() {} 

  public Model3dBuilder fromObjFile(String filepath) {
      Path path = Paths.get(filepath);
      
      try (Stream<String> lines = Files.lines(path)) {
          lines.forEach(line -> {
              String[] split = StringUtils.split(line, " ");

              if (split.length == 0) {
                return;
              }

              String key = split[0].trim();
              
              if (!key.equals("v") && !key.equals("vn") && !key.equals("f")) {
                return;
              }
              
              if (key.equals("v")) {  
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                double z = Double.parseDouble(split[3]);
                addVertex(x, y, z);
              }
              if (key.equals("vn")) {
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                double z = Double.parseDouble(split[3]);
                addNormal(x, y, z);
              }
              if (key.equals("f")) {
                int[] indices = new int[split.length-1];
                for (int i = 0; i < indices.length; i++) {
                  indices[i] = Integer.parseInt(split[indices.length-i].split("/")[0]) - 1;
                }
                addFace(indices);
              }
          });
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
      
    return this;
  }

  public Model3dBuilder addVertex(Vector3d... vertices) {
    for (Vector3d vertex : vertices) {
      this.vertices.add(vertex);
    }
    return this;
  }

  public Model3dBuilder addVertex(double x, double y, double z) {
    return addVertex(new Vector3d(x, y, z));
  }
  
  public ArrayList<Vector3d> getVertices() {
    return vertices;
  }

  public Model3dBuilder addNormal(Vector3d... normals) {
    for (Vector3d normal : normals) {
      this.normals.add(normal.normalizeLocal());
    }
    return this;
  }

  public Model3dBuilder addNormal(double x, double y, double z) {
    return addNormal(new Vector3d(x, y, z));
  }
  
  public ArrayList<Vector3d> getNormals() {
    return normals;
  }
  
  public Model3dBuilder addMass(double... masses) {
    for (double mass : masses) {
      this.masses.add(mass);
    }
    return this;
  }
  
  public ArrayList<Double> getMasses() {
    return masses;
  }

  public Model3dBuilder addPatch(Integer[]... patches) {
    for (Integer[] patch : patches) {
      this.faces.add(patch);
    }
    return this;
  }

  public Model3dBuilder addFace(int... patch) {
    this.faces.add(ArrayUtils.box(patch));
    return this;
  }

  public Model3dBuilder addColor(int r, int g, int b, int a) {
    return setColor(new FeynColor(r, g, b, a));
  }

  public Model3dBuilder setColor(FeynColor color) {
    this.colors.add(color);
    return this;
  }

  public Model3dBuilder setPosition(Vector3d position) {
    this.position = new Vector3d(position);
    return this;
  }

  public Model3dBuilder setVelocity(Vector3d velocity) {
    this.velocity = new Vector3d(velocity);
    return this;
  } 
  
  public Model3dBuilder setRotation(Rotation3d rotation) {
    this.rotation = rotation;
    return this;
  }

  public Model3dBuilder addTransform(Matrix44 transform) {
    this.transforms.add(transform);
    return this;
  }

  public Model3dBuilder setOuterBoundingObject(BoundingObject3d outerBoundingObject) {
    this.outerBoundingObject = outerBoundingObject;
    return this;
  }

  public Model3dBuilder addInnerBoundingObject(BoundingObject3d... innerBoundingObjects) {
    for (BoundingObject3d bo : innerBoundingObjects) {
      this.innerBoundingObjects.add(bo);
    }
    return this;
  }
  
  public Model3dBuilder setTextureData(TextureRaster textureData) {
    this.textureData = textureData;
    return this;
  }
  
  public Model3dBuilder setTextureData(TextureRaster textureData, int textureAlpha, double textureZoom) {
    this.textureData = textureData;
    this.textureAlpha = textureAlpha;
    this.textureZoom = textureZoom;
    return this;
  }

  private void validate() {
    if (vertices.size() < 3) {
      throw new AssertionError();
    }
    if (normals.size() > 0 && normals.size() != vertices.size()) {
      throw new AssertionError();
    }
  }

  public Model3d build() {
    validate();
    
    Vector3d[] v = vertices.toArray(new Vector3d[vertices.size()]);
    Vector3d[] n = normals.toArray(new Vector3d[normals.size()]);
    double[] m = masses.size() == 0 
        ? null 
        : ArrayUtils.unbox(masses.toArray(new Double[normals.size()]));
    
    Model3dVertices vs;
    if (n.length > 0) {
      vs = new Model3dGouraudVertices(v, n, m);
    } else  {
      vs = new Model3dVertices(v, m);
    }
     
    Model3dFace[] f = new Model3dFace[faces.size()];
    for (int i = 0; i < f.length; i++) {
      FeynColor color = i < colors.size()
          ? colors.get(i) 
          : (colors.size() > 0 
                  ? colors.get(colors.size()-1) 
                  : FeynColor.randomColor());
          
      f[i] = textureData != null 
          ? new Model3dTexturedFace(ArrayUtils.unbox(faces.get(i)), textureData, textureAlpha, textureZoom)
          : new Model3dFace(ArrayUtils.unbox(faces.get(i)), color);
    }
    
    Model3d model;
    if (innerBoundingObjects.size() > 0) {
      model = new ComplexCollidableModel3d(
              vs, 
              f, 
              outerBoundingObject, 
              innerBoundingObjects.toArray(new BoundingObject3d[]{}));
    } else if (outerBoundingObject != null) {
      model = new CollidableModel3d(vs, f, outerBoundingObject);
    } else {
      model = new Model3d(vs, f);
    }
    
    model.setPosition(position); 
    model.setVelocity(velocity);
    model.setRotation(rotation);

    transforms.forEach(model::transform);
    
    return model;
  } 
}