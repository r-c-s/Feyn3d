package rcs.feyn.three.entities.models;

import rcs.feyn.color.AlphaEnabled;
import rcs.feyn.color.FeynColor;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.GouraudTexturedPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedFace extends Model3dFace implements AlphaEnabled {
  
  private Raster textureData;
  private int alpha;
  private double zoom;
  
  private Raster lastTextureData;
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
    this.lastTextureData = textureData;
    this.textureData = textureData;
  }
  
  @Override
  public int getAlpha() {
    return alpha;
  }

  @Override
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
    
    if (vertices instanceof Model3dGouraudVertices 
        && options.isEnabled(Option.gouraudShaded) 
        && options.isEnabled(Option.textured)) {
      newPatch = new GouraudTexturedPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()), 
          textureData,
          alpha,
          zoom,
          options);
      
    } else if (vertices instanceof Model3dGouraudVertices 
        && options.isEnabled(Option.gouraudShaded)) { 
      newPatch = new GouraudPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()), 
          FeynColor.white,
          options);
      
    } else if (options.isEnabled(Option.textured)) {
      newPatch = new TexturedPolygon3dPatch(
          lastVertices, 
          textureData,
          alpha,
          zoom,
          options);
    } else {
      newPatch = new Polygon3dPatch(
          lastVertices, 
          FeynColor.white,
          options);
    }
    
    return lastPatch = newPatch;
  }
  
  @Override
  protected synchronized boolean matchesLastPatch(Model3dVertices vertices) {
    return textureData == lastTextureData 
        && alpha == lastAlpha 
        && super.matchesLastPatch(vertices);
  }
}
