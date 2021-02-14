package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.renderers.Line3dRenderer;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.three.render.renderers.Polygon3dRenderer;
import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Polygon3dPatch extends Patch3d {
  
  protected Vector3d[] vertices;

  public Polygon3dPatch(Vector3d[] vertices, FeynColor color, RenderOptions3d options) {
    super(color, options);
    this.vertices = vertices;
  } 

  @Override
  public final Vector3d getCenter() {
    return GeoUtils3d.getCenter(vertices);
  }
  
  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    if (cullIfBackface(center, normal)) {
      return;
    }

    Vector3d[] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(vertices, view);

    Vector3d[] clippedViewSpaceCoordinates = Pipeline3d
        .clipViewSpaceCoordinates(viewSpaceCoordinates);

    Vector3d[] deviceCoordinates = Pipeline3d
        .toDeviceCoordinates(clippedViewSpaceCoordinates, projection, viewPort);

    double intensity = 1.0;
    if (options.isEnabled(RenderOptions3d.Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(
            center, 
            normal, 
            options.isEnabled(RenderOptions3d.Option.bothSidesShaded) || options.isEnabled(RenderOptions3d.Option.meshOnly));
    }

    int finalColor = ColorUtils.mulRGB(color.getRGBA(), intensity);
    if (options.isEnabled(RenderOptions3d.Option.applyLightingColor)) {
      finalColor = LightingUtils.applyLightsourceColorTo(center, normal, finalColor);
    } 
    
    if (options.isEnabled(RenderOptions3d.Option.meshOnly)) {
      renderMesh(graphics, deviceCoordinates, intensity, finalColor);
    } else {
      Polygon3dRenderer.render(
        graphics,
        deviceCoordinates,
        finalColor);
    }
  }

  private void renderMesh(Graphics3d graphics, Vector3d[] vpcVertices, double intensity, int color) {
    intensity = options.isEnabled(RenderOptions3d.Option.meshShaded) ? intensity : 1;
    for (int i = 0, j = 1; i < vpcVertices.length; i++, j++, j%=vpcVertices.length) {
      Line3dRenderer.render(
          graphics, 
          vpcVertices[i], 
          vpcVertices[j], 
          ColorUtils.mulRGBA(color, intensity));
    }
  }
  
  protected boolean cullIfBackface(Vector3d center, Vector3d normal) {
    return !options.isEnabled(RenderOptions3d.Option.meshOnly)
        && options.isEnabled(RenderOptions3d.Option.cullIfBackface) 
        && !color.isTransparent()
        && ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, normal);
  }
}
