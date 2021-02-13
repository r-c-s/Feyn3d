package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.renderers.TexturedPolygon3dRenderer;

import java.util.Optional;

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
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    if (options.isEnabled(RenderOptions3d.Option.meshOnly)) {
      super.render(graphics, view, projection, viewPort);
      return;
    }
      
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    if (cullIfBackface(center, normal)) {
      return;
    }

    Vector3d[] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(vertices, view);
    
    // can't clip textured polygons

    Vector3d[] deviceCoordinates = Pipeline3d
        .getDeviceCoordinates(viewSpaceCoordinates, projection, viewPort);

    double intensity = 1.0;
    if (options.isEnabled(RenderOptions3d.Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(
            center, 
            normal, 
            options.isEnabled(RenderOptions3d.Option.bothSidesShaded) || options.isEnabled(RenderOptions3d.Option.meshOnly));
    }
   
    int[] colors = null;
    if (options.isEnabled(RenderOptions3d.Option.applyLightingColor) 
        && LightingUtils.hasColoredLightsources()) {
      colors = new int[viewSpaceCoordinates.length];
      for (int i = 0; i < colors.length; i++) {
        if (options.isEnabled(RenderOptions3d.Option.applyLightingColor)) {
          colors[i] = LightingUtils.applyLightsourceColorTo(
              vertices[i], 
              normal,
              colors[i]);
        }
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
