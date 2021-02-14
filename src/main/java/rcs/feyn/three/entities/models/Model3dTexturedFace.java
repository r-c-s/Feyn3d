package rcs.feyn.three.entities.models;

import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.render.patches.GouraudTexturedPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedFace extends Model3dFace {
  
  private Raster textureData;
  private int alpha;
  private double zoom;
  
  private Raster lastTextureDate;
  private int lastAlpha;
  
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
  
  public synchronized void setTextureData(Raster textureData) {
    this.lastTextureDate = textureData;
    this.textureData = textureData;
  }
  
  public int getAlpha() {
    return alpha;
  }
  
  public synchronized void setAlpha(int alpha) {
    this.lastAlpha = this.alpha;
    this.alpha = alpha;
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
      newPatch = new GouraudTexturedPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()), 
          textureData,
          alpha,
          zoom,
          options);
    } else {
      newPatch = new TexturedPolygon3dPatch(
          lastVertices, 
          textureData,
          alpha,
          zoom,
          options);
    }
    
    return lastPatch = newPatch;
  }
  
  @Override
  protected synchronized boolean matchesLastPatch(Model3dVertices vertices) {
    return textureData == lastTextureDate 
        && alpha == lastAlpha 
        && super.matchesLastPatch(vertices);
  }
}
