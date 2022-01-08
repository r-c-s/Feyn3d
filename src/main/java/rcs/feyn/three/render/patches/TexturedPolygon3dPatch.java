package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.renderers.TexturedPolygon3dRenderer;
import rcs.feyn.three.view.ViewUtils;

import java.util.Optional;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector2d;
import rcs.feyn.math.Vector3d;

public class TexturedPolygon3dPatch extends Polygon3dPatch {
  
  protected Raster textureData;
  protected int alpha;
  protected double zoom;

  public TexturedPolygon3dPatch(
      Vector3d[] vertices, 
      Raster data, 
      int alpha, 
      double zoom, 
      RenderOptions3d options) {
    super(vertices, FeynColor.white, options);
    this.textureData = data;
    this.alpha = alpha;
    this.zoom = zoom;
  } 
  
  @Override
  public final boolean isTransparent() {
    return alpha < 255;
  }

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    if (!options.isEnabled(Option.textured) 
        || options.isEnabled(Option.meshOnly)) {
      super.render(graphics, view, projection, viewPort);
      return;
    }
    
    Vector3d center = GeoUtils3d.getCenter(vertices);
    Vector3d surfaceNormal = GeoUtils3d.getNormal(vertices);
    boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, surfaceNormal);
    
    if (isBackfaceToCamera && shouldCullIfBackface()) {
      return;
    }
    
    Vector3d[] viewVertices = Pipeline3d.toViewSpaceCoordinates(vertices, view);
    Vector3d[] clippedViewVertices = Pipeline3d.clipViewSpaceCoordinates(viewVertices);
    
    int numClippedViewVertices = clippedViewVertices.length;
    
    if (numClippedViewVertices < 3) {
      return;
    }
    
    Vector3d[][] triangulatedClippedViewVertices = GeoUtils3d.triangulate(clippedViewVertices);
    
    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    Vector3d normalForLighting = shouldReverseNormalForLighting ? surfaceNormal.mul(-1) : surfaceNormal;

    double intensity = 1.0;
    if (options.isEnabled(Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(center, normalForLighting);
    }

    boolean shouldApplyLightingColor = options.isEnabled(Option.applyLightingColor) && LightingUtils.hasColoredLightsources();
    
    int[] colors = null;
    if (shouldApplyLightingColor) {
      colors = new int[numClippedViewVertices];
      int initialColor = ColorUtils.mulRGB(color.getRGBA(), intensity);
      for (int i = 0; i < numClippedViewVertices; i++) {
        colors[i] = LightingUtils.applyLightsourceColorTo(
            clippedViewVertices[i], 
            surfaceNormal.affineTransformAsVector(view), 
            view, 
            initialColor);
      }
    }
    
    int[][] triangulatedColors = shouldApplyLightingColor ? GeoUtils3d.triangulate(colors) : null;

    int tdw = textureData.getWidth();    
    int tdh = textureData.getHeight();

    for (int i = 0; i < triangulatedClippedViewVertices.length; i++) {      
      // todo: improve this by using triangle; this distorts the shape of the texture
      Vector2d[] textureCoordinates = new Vector2d[] {
          new Vector2d(0, 0),
          new Vector2d(0, tdh - 1),
          new Vector2d(tdw - 1, tdh - 1)
      }; 
      
      TexturedPolygon3dRenderer.render(
        graphics,
        Pipeline3d.toDeviceCoordinates(triangulatedClippedViewVertices[i], projection, viewPort),
        intensity,
        Optional.ofNullable(shouldApplyLightingColor ? triangulatedColors[i] : null),
        textureData,
        textureCoordinates,
        alpha, 
        zoom);
    }
  }
}
