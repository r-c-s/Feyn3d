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
      
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, normal);
    
    if (isBackfaceToCamera && shouldCullIfBackface(center, normal)) {
      return;
    }

    Vector3d[] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(vertices, view);
    Vector3d[] clippedViewSpaceCoordinates = Pipeline3d
        .clipViewSpaceCoordinates(viewSpaceCoordinates);
    
    if (clippedViewSpaceCoordinates.length < 3) {
      return;
    }

    Vector3d[] deviceCoordinates = Pipeline3d
        .toDeviceCoordinates(clippedViewSpaceCoordinates, projection, viewPort);

    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    Vector3d normalForLighting = shouldReverseNormalForLighting ? normal.mul(-1) : normal;

    double intensity = 1.0;
    if (options.isEnabled(Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(center, normalForLighting);
    }

    boolean applyLightingColor = options.isEnabled(Option.applyLightingColor)
        && LightingUtils.hasColoredLightsources();
   
    int[] colors = null;
    if (applyLightingColor) {
      colors = new int[clippedViewSpaceCoordinates.length];
      for (int i = 0; i < colors.length; i++) {
        int initialColor = ColorUtils.mulRGB(color.getRGBA(), intensity);
        colors[i] = LightingUtils.applyLightsourceColorTo(
            vertices[i], 
            normalForLighting,
            initialColor);
      }
    }
    
    TexturedPolygon3dRenderer.render(
      graphics,
      deviceCoordinates,
      intensity,
      Optional.ofNullable(colors),
      textureData,
      alpha, 
      zoom);
  }
}
