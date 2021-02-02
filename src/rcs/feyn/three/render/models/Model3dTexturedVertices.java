package rcs.feyn.three.render.models;

import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedVertices extends Model3dVertices {
  
  private Raster textureData;

  public Model3dTexturedVertices(Vector3d[] vertices, Raster textureData) {
    super(vertices);
    setTextureData(textureData);
  }
  
  public void setTextureData(Raster textureData) {
  	this.textureData = textureData;
  }
  
  @Override
  public Polygon3dPatch makePatch(Model3dFace face) {
    return new TexturedPolygon3dPatch(
        getVertices(face.getIndices()),
        textureData,
        face.getRenderOptions());
  }
}
