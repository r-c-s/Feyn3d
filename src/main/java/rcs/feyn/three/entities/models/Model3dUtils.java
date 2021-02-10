package rcs.feyn.three.entities.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.imageio.ImageIO;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.collision.models.CollidableModel3d;
import rcs.feyn.three.collision.models.ComplexCollidableModel3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.XORShift;

public class Model3dUtils {
  
  public static Raster getImageData(String filename) {
    BufferedImage bufferedImage;
    try {
      bufferedImage = ImageIO.read(new File(filename));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    int w = bufferedImage.getWidth();
    int h = bufferedImage.getHeight();
    
    int[] ints = new int[w * h];
    
    for (int x = 0; x < w; x++) {
    	for (int y = 0; y < h; y++) {
        ints[y*w + x] = bufferedImage.getRGB(x, y);
    	}
    }
    return new Raster(ints, w, h);
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
  
  public static Model3d[] partition2d(Model3d model) {
    Model3dFace[] faces = model.getFaces();
    Model3d[] parts = new Model3d[faces.length];
    
    for (int i = 0; i < faces.length; i++) {
      Model3dFace face = faces[i];
      
      int[] indices = face.getIndices();
      int[] adjustedIndices = new int[indices.length];
      for (int j = 0; j < indices.length; j++) {
        adjustedIndices[j] = j;
      }
      
      Model3dFace partFaces = new Model3dFace(adjustedIndices, faces[i].getColor());
      Vector3d[] partVertices = getVertices(model.getVertices(), face);

      Vector3d com = GeoUtils3d.getCenter(partVertices);
      
      for (Vector3d vertex : partVertices) {
        vertex.subLocal(com);
      }
      
      Model3d part = new Model3d(
          new Model3dVertices(partVertices), 
          new Model3dFace[] { partFaces });
      
      part.setPosition(com);
      
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

      if (faces[i] instanceof Model3dTexturedFace) {
        partFaces[len] = new Model3dTexturedFace(
            adjustedIndices, 
            ((Model3dTexturedFace) faces[i]).getTextureData());
      } else {
        partFaces[len] = new Model3dFace(
            adjustedIndices, 
            faces[i].getColor());
      }
      
      for (int l = 0; l < len; l++) {
        Model3dFace currFace = faces[i];
        int[] idxs = new int[] { (1+l)%len, l, len };
        
        if (face instanceof Model3dTexturedFace) {
          partFaces[l] = new Model3dTexturedFace(
              idxs, 
              ((Model3dTexturedFace) face).getTextureData());
        } else {
          partFaces[l] = new Model3dFace(
              idxs, 
              currFace.getColor());
        }
      }

      Model3d part = new Model3d(
          new Model3dVertices(partVertices), 
          partFaces);
      
      part.setPosition(com);
      
      parts[i] = part;
    }
    
    return parts;
  }
  
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
