package rcs.feyn.three.entities.models;

import rcs.feyn.gfx.Raster;
import rcs.feyn.three.render.patches.GouraudTexturedPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedFace extends Model3dFace {
  
  private Raster textureData;
  private int alpha;
  private double zoom;

  public Model3dTexturedFace(int[] indices, Raster textureData) {
    this(indices, textureData, 255, 1);
  }

  public Model3dTexturedFace(int[] indices, Raster textureData, int alpha, double zoom) {
    super(indices, null);
    setTextureData(textureData);
    setAlpha(alpha);
    this.zoom = zoom;
  }
  
  public Raster getTextureData() {
    return textureData;
  }
  
  public void setTextureData(Raster textureData) {
    this.textureData = textureData;
  }
  
  public int getAlpha() {
    return alpha;
  }
  
  public void setAlpha(int alpha) {
    this.alpha = alpha;
  }

  public Polygon3dPatch makePatch(Model3dVertices vertices) {
    if (vertices instanceof Model3dGouraudVertices) {
      return new GouraudTexturedPolygon3dPatch(
          getVertices(vertices.getVertices()), 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()),
          textureData,
          alpha,
          zoom,
          options);
    } else {
      return new TexturedPolygon3dPatch(
          getVertices(vertices.getVertices()), 
          textureData,
          alpha,
          zoom,
          options);
    }
  }
}
