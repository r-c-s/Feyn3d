package rcs.feyn.three.entities.models;

import rcs.feyn.color.AbstractColorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;

public class Model3dFace extends AbstractColorable {
  
  protected RenderOptions3d options = RenderOptions3d.defaults();
  
  protected int[] indices;
  
  protected Vector3d[] lastVertices;
  protected Polygon3dPatch lastPatch;

  public Model3dFace(int[] indices, FeynColor color) {
    super(color);
    this.indices = indices;
  }

  public int[] getIndices() {
    return indices.clone();
  }

  public RenderOptions3d getRenderOptions() {
    return options;
  }

  public Polygon3dPatch makePatch(Model3dVertices vertices) {
    if (matchesLastPatch(vertices)) {
      return lastPatch;
    }
    
    synchronized(this) {
      lastVertices = getVertices(vertices.getVertices());
    }
    
    Polygon3dPatch newPatch;
    
    if (vertices instanceof Model3dGouraudVertices) {      
      newPatch = new GouraudPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()), 
          color,
          options);
    } else {
      newPatch = new Polygon3dPatch(
          lastVertices, 
          color,
          options);
    }
    
    return lastPatch = newPatch;
  }
  
  protected synchronized boolean matchesLastPatch(Model3dVertices vertices) {
    // assumes that normals only change if vertices changed
    if (lastVertices == null || lastPatch == null) {
      return false;
    }
    Vector3d[] newVertices = vertices.getVertices();
    for (int i = 0; i < indices.length; i++) {
      if (!newVertices[indices[i]].equals(lastVertices[i])) {
        return false;
      }
    }
    return true;
  }

  protected Vector3d[] getVertices(Vector3d[] vertices) {
    Vector3d[] patchVertices = new Vector3d[indices.length];
    for (int i = 0; i < indices.length; i++) {
      patchVertices[i] = vertices[indices[i]];
    }
    return patchVertices;
  }
}
