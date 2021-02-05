package rcs.feyn.three.render.models;

import rcs.feyn.gfx.Raster;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedFace extends Model3dFace {
  
  private Raster textureData;

  public Model3dTexturedFace(int[] indices, Raster textureData) {
    super(indices, null);
    setTextureData(textureData);
  }
  
  public void setTextureData(Raster textureData) {
    this.textureData = textureData;
  }

  public Polygon3dPatch makePatch(Model3dVertices vertices) {
    return new TexturedPolygon3dPatch(
        getVertices(vertices.getVertices(), indices), 
        textureData,
        options);
  }
}
