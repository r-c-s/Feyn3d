package rcs.feyn.three.render.models;

import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedVertices extends Model3dVertices {
  
  private Raster data;

  public Model3dTexturedVertices(Vector3d[] vertices, Raster data) {
    super(vertices);
    this.data = data;
  }
  
  @Override
  public Polygon3dPatch makePatch(Model3dFace face) {
    return new TexturedPolygon3dPatch(
        getVertices(face.getIndices()),
        data,
        face.getRenderOptions());
  }
}
