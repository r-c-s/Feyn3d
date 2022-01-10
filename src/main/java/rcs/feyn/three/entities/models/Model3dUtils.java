package rcs.feyn.three.entities.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.utils.ArrayUtils;
import rcs.feyn.utils.XORShift;

public class Model3dUtils {
  
  public static TextureRaster getImageData(String filename) {
    BufferedImage bufferedImage;
    try {
      bufferedImage = ImageIO.read(new File(filename));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    int W = bufferedImage.getWidth();
    int H = bufferedImage.getHeight();
    
    // force image to be square
    int w = Math.min(W, H);
    int h = w;
    
    int[] ints = new int[w * h];
    
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        ints[y*w + x] = bufferedImage.getRGB(x, y);
      }
    }
    return new TextureRaster(ints, w, h);
  }
  
  public static Model3d getRenderableSphere(CollidableModel3d model) {
    BoundingSphere3d sphere = ((BoundingSphere3d)model.getOuterBoundingObject());
    return Model3dFactory.icosphere(sphere.getRadius(), 1)
        .setColor(new FeynColor(FeynColor.white, 100))
        .setPosition(sphere.getPosition().add(model.getCenter()))
        .build();
  }
  
  public static BoundingSphere3d getOuterBoundingSphere(Model3d model) {
    Vector3d center = model.getCenter();
    double radius = 0;
    for (Vector3d vertex : model.getVertices().getVertices()) {
      double distance = vertex.distance(center);
      if (distance > radius) {
        radius = distance;
      }
    }
    return new BoundingSphere3d(radius, center);
  }
  
  public static BoundingSphere3d[] getInnerBoundingSpheres(Model3d model, int recursionLevel) {
    Model3d[] parts = partition3d(model);
    return Arrays.asList(parts)
        .stream()
        .map(Model3dUtils::getOuterBoundingSphere)
        .toArray(BoundingSphere3d[]::new);
  }
  
  public static CollidableModel3d addOuterBoundingSphere(Model3d model) {
    return new CollidableModel3d(
        model.getVertices(),
        model.getFaces(), 
        getOuterBoundingSphere(model));
  }
  
  public static ComplexCollidableModel3d addInnerBoundingSpheres(Model3d model, int recursionLevel) {
    return new ComplexCollidableModel3d(
        model.getVertices(), 
        model.getFaces(), 
        getOuterBoundingSphere(model), 
        getInnerBoundingSpheres(model, recursionLevel));
  }
  
  /**
   * todo: this should return an array of Polygon3d
   */
  public static Model3d[] partition2d(Model3d model) {
    return partition2d(model, 0);
  }
  
  public static Model3d[] partition2d(Model3d model, double thickness) {
    Vector3d centerOfModel = model.getCenter();
    Model3dFace[] faces = model.getFaces();
    Model3d[] parts = new Model3d[faces.length];
    
    for (int i = 0; i < faces.length; i++) {
      Model3dFace face = faces[i];
      Vector3d[] faceVertices = getVertices(model.getVertices(), face);
      
      int[] indices = face.getIndices();
      int[] adjustedIndices = new int[indices.length];
      for (int j = 0; j < indices.length; j++) {
        adjustedIndices[j] = j;
      }

      Model3dFace[] partFaces = new Model3dFace[thickness > 0 ? faceVertices.length + 2 : 1];
      
      for (int j = 0; j < partFaces.length; j++) {

        int[] indicesToUse;
        
        if (j == 0) {
          // the regular face
          indicesToUse = adjustedIndices;
        } else if (j == 1) {
          // the opposite side of the face
          indicesToUse = new int[indices.length];
          for (int k = 0; k < indices.length; k++) {
            indicesToUse[k] = indices.length + k;
          }
          ArrayUtils.reverse(indicesToUse);
        } else {
          // the sides
          indicesToUse = new int[4];
          int k = j - 2;
          indicesToUse[0] = k;
          indicesToUse[1] = k + 3;
          indicesToUse[2] = (k + 1) % indices.length + indices.length;
          indicesToUse[3] = (k + 1) % indices.length;
        }

        partFaces[j] = face.cloneWithNewIndices(indicesToUse);
      }      

      Vector3d centerOfFace = GeoUtils3d.getCenter(faceVertices);
      
      if (thickness > 0) {
        faceVertices = Stream.concat(
              Arrays.stream(faceVertices),
              Arrays.stream(faceVertices).map(vertex -> { 
                Vector3d csv = centerOfFace.sub(vertex);
                Vector3d cmv = centerOfModel.sub(vertex);
                
                double theta = csv.angleBetween(cmv);
                double length = Math.abs(thickness / Math.tan(theta));
                double maxLength = Math.min(length, 0.95 * cmv.length());
                
                Vector3d offsetVertex = cmv.normalize().mulLocal(maxLength);
                
                return vertex.add(offsetVertex);
              }))
            .toArray(Vector3d[]::new);
      }
      
      Vector3d centerOfPart = GeoUtils3d.getCenter(faceVertices);
      
      for (Vector3d vertex : faceVertices) {
        vertex.subLocal(centerOfPart);
      }
      
      Model3d part = new Model3d(
          new Model3dVertices(faceVertices), 
          partFaces);
      
      part.setPosition(centerOfPart);
      
      parts[i] = part;
    }
    
    return parts;
  }
  
  public static Model3d[] partition3d(Model3d model, double recursionLevel) {
    ArrayList<Model3d> partsBuffer = new ArrayList<>();
    partsBuffer.add(model);
    
    for (int i = 0; i < recursionLevel; i++) {
      
      ArrayList<Model3d> partsBufferB = new ArrayList<>();
      
      for (int j = 0; j < partsBuffer.size(); j++) {
        Model3d[] parts = partition3d(partsBuffer.get(j));
        
        for (Model3d part : parts) {
          partsBufferB.add(part);
        }
      }
      
      partsBuffer = partsBufferB;
    }
    
    return partsBuffer.toArray(new Model3d[partsBuffer.size()]);
  }

  public static Model3d[] partition3d(Model3d model) {
    Model3dFace[] faces = model.getFaces();
    Model3d[] parts = new Model3d[faces.length];
    
    for (int i = 0; i < faces.length; i++) {
      Model3dFace face = faces[i];
      
      int[] indices = face.getIndices();
      int len = indices.length;
      int[] adjustedIndices = new int[len];
      for (int j = 0; j < len; j++) {
        adjustedIndices[j] = j;
      }
      
      Vector3d[] modelVertices = getVertices(model.getVertices(), face);
      Vector3d[] partVertices = new Vector3d[len + 1];
      Model3dFace[] partFaces = new Model3dFace[len + 1];
      partVertices[len] = model.getCenter();
      for (int k = 0; k < len; k++) {
        partVertices[k] = modelVertices[k];
      }
      
      Vector3d com = GeoUtils3d.getCenter(partVertices);
      for (Vector3d vertex : partVertices) {
        vertex.subLocal(com);
      }
      
      partFaces[len] = face.cloneWithNewIndices(adjustedIndices);
      
      for (int l = 0; l < len; l++) {
        int[] idxs = new int[] { (1+l)%len, l, len };        
        partFaces[l] = face.cloneWithNewIndices(idxs);
      }

      Model3d part = new Model3d(new Model3dVertices(partVertices), partFaces);
      
      part.setPosition(com);
      
      parts[i] = part;
    }
    
    return parts;
  }
  
  public static void normalizeFacesToTriangles(Model3d model) {
    Model3dVertices modelVertices = model.getVertices();
    boolean isGouraud = modelVertices instanceof Model3dGouraudVertices;

    LinkedList<Vector3d> newVertices = new LinkedList<>();
    LinkedList<Vector3d> newNormals = new LinkedList<>();
    LinkedList<Model3dFace> newFaces = new LinkedList<>();
    
    for (Model3dFace face : model.getFaces()) {
      Vector3d[] vertices = face.getVertices(modelVertices.getVertices());
      
      if (vertices.length > 3) {
        Vector3d faceCenter = GeoUtils3d.getCenter(vertices);
        newVertices.add(faceCenter);
        for (Vector3d vertex : vertices) {
          if (!newVertices.contains(vertex)) {
            newVertices.add(vertex);
          } 
        }
        
        if (isGouraud) {
          Vector3d[] normals = face.getVertices(((Model3dGouraudVertices) modelVertices).getNormals());
          newNormals.add(GeoUtils3d.getNormal(vertices));
          for (Vector3d normal : normals) {
            if (!newNormals.contains(normal)) {
              newNormals.add(normal);
            }
          }
        }

        int numberOfNewFaces = vertices.length;
        
        for (int i = 0; i < numberOfNewFaces; i++) {
          int[] newFaceIndices = new int[] { 
              newVertices.indexOf(faceCenter),
              newVertices.indexOf(vertices[i]),
              newVertices.indexOf(vertices[(i+1) % vertices.length])
          };
          Model3dFace newFace = face.cloneWithNewIndices(newFaceIndices);          
          newFaces.add(newFace);
        }        
      } else { 
        for (Vector3d vertex : vertices) {
          if (!newVertices.contains(vertex)) {
            newVertices.add(vertex);
          }
        }
        
        if (isGouraud) {
          Vector3d[] normals = face.getVertices(((Model3dGouraudVertices) modelVertices).getNormals());
          for (Vector3d normal : normals) {
            if (!newNormals.contains(normal)) {
              newNormals.add(normal);
            }
          }
        }
        
        int[] newFaceIndices = new int[] { 
            newVertices.indexOf(vertices[0]), 
            newVertices.indexOf(vertices[1]),
            newVertices.indexOf(vertices[2])
        };
        
        Model3dFace newFace = face.cloneWithNewIndices(newFaceIndices);
        newFaces.add(newFace);
      }
    }
    
   model.setVertices(
        isGouraud
          ? new Model3dGouraudVertices(newVertices.toArray(Vector3d[]::new), newNormals.toArray(Vector3d[]::new))
          : new Model3dVertices(newVertices.toArray(Vector3d[]::new)));
   
   model.setFaces(newFaces.toArray(Model3dFace[]::new));
  }
  
  /**
   * If you have Models whose faces are not triangles,
   * it is recommended that you call normalizeFacesToTriangles before calling this method
   */
  public static void deform(Model3d model, double factor) {    
    Vector3d com = model.getCenter();
    
    for (Model3dFace face : model.getFaces()) {
      Vector3d[] vertices = getVertices(model.getVertices(), face);
      
      for (Vector3d vertex : vertices) {
        double scalar = 1 + (factor * XORShift.getInstance().randomDouble(-1, 1));
        vertex.subLocal(com).mulLocal(scalar).addLocal(com);
      }
      
      model.getVertices().setVertices(vertices, face.getIndices());
    }
  }
  
  public static void deform(Model3d model, double factor, Vector3d directionBias, double toleranceAngle) {
    Vector3d com = model.getCenter();
    
    for (Model3dFace face : model.getFaces()) {
      Vector3d[] vertices = getVertices(model.getVertices(), face);

      for (Vector3d vertex : vertices) {
        double angleRadians = vertex.sub(com).normalizeLocal().angleBetween(directionBias);
        boolean isInDirectionBias = angleRadians < toleranceAngle;
        if (isInDirectionBias) {
          double scalar = 1 + (factor * XORShift.getInstance().randomDouble(-1, 1));
          vertex.mulLocal(scalar);
        }
      }
      
      model.getVertices().setVertices(vertices, face.getIndices());
    }
  }
  
  public static void setOptions(Model3d model, Set<RenderOptions3d.Option> enable, Set<RenderOptions3d.Option> disable) {
    for (Model3dFace face : model.getFaces()) {
      enable.forEach(opt -> face.getRenderOptions().enable(opt));
      disable.forEach(opt -> face.getRenderOptions().disable(opt));
    }
  }
  
  public static Vector3d[] getVertices(Model3dVertices vertices, Model3dFace face) {
    int[] indices = face.getIndices();
    Vector3d[] allVertices = vertices.getVertices();
    Vector3d[] patchVertices = new Vector3d[indices.length];
    for (int i = 0; i < indices.length; i++) {
      patchVertices[i] = new Vector3d(allVertices[indices[i]]);
    }
    return patchVertices;
  }
}
