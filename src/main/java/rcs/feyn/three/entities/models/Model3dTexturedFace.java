package rcs.feyn.three.entities.models;

import rcs.feyn.color.AlphaEnabled;
import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.GouraudTexturedPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;

public class Model3dTexturedFace extends Model3dFace implements AlphaEnabled {
  
  private TextureRaster textureData;
  private int alpha;
  private double zoom;
  
  private TextureRaster lastTextureData;
  private int lastAlpha;
  
  public Model3dTexturedFace(int[] indices, TextureRaster textureData) {
    this(indices, textureData, 255, 1);
  }

  public Model3dTexturedFace(int[] indices, TextureRaster textureData, int alpha, double zoom) {
    super(indices, null);
    setTextureData(textureData);
    setAlpha(alpha);
    this.color = new FeynColor(ColorUtils.setAlphaToRGBA(textureData.getAverageColor(), alpha));
    this.zoom = zoom;
  }
  
  @Override
  public Model3dTexturedFace cloneWithNewIndices(int[] indices) {
    Model3dTexturedFace newFace = new Model3dTexturedFace(indices, textureData, alpha, zoom);
    newFace.setRenderOptions(options);
    return newFace;
  }
  
  public TextureRaster getTextureData() {
    return textureData;
  }
  
  public synchronized void setTextureData(TextureRaster textureData) {
    this.lastTextureData = textureData;
    this.textureData = textureData;
  }
  
  public double getZoom() {
    return zoom;
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

  @Override
  public Polygon3dPatch makePatch(Model3dVertices vertices) {
    if (matchesLastPatch(vertices)) {
      return lastPatch;
    }
    
    synchronized(this) {
      lastVertices = getVertices(vertices.getVertices());
      lastCameraPosition = FeynRuntime.getView().getCamera().getPosition();
    }
    
    Polygon3dPatch newPatch;
    if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded) && options.isEnabled(Option.textured)) {
      newPatch = new GouraudTexturedPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()),
          textureData,
          alpha,
          zoom,
          options);
    } else if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded)) { 
      newPatch = new GouraudPolygon3dPatch(
          lastVertices, 
          getVertices(((Model3dGouraudVertices) vertices).getNormals()),
          color,
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
          color,
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
