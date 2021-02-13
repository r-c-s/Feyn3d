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

  // TODO: cache patch
  public Polygon3dPatch makePatch(Model3dVertices vertices) {
    if (vertices instanceof Model3dGouraudVertices) {
      return new GouraudPolygon3dPatch(
          getVertices(vertices.getVertices()), 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()), 
          color,
          options);
    } else {
      return new Polygon3dPatch(
          getVertices(vertices.getVertices()), 
          color,
          options);
    }
  }

  protected synchronized Vector3d[] getVertices(Vector3d[] vertices) {
    Vector3d[] patchVertices = new Vector3d[indices.length];
    for (int i = 0; i < indices.length; i++) {
      patchVertices[i] = new Vector3d(vertices[indices[i]]);
    }
    return patchVertices;
  }
}
