package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.RenderOptions3d.Option;
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

    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.meshOnly) && !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    Vector3d normalForLighting = shouldReverseNormalForLighting ? normal.mul(-1) : normal;

    double intensity = 1.0;
    if (options.isEnabled(RenderOptions3d.Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(center, normalForLighting);
    }

    int finalColor = ColorUtils.mulRGB(color.getRGBA(), intensity);
    if (options.isEnabled(RenderOptions3d.Option.applyLightingColor)) {
      finalColor = LightingUtils.applyLightsourceColorTo(center, normalForLighting, finalColor);
    } 
    
    if (options.isEnabled(RenderOptions3d.Option.meshOnly)) {
      renderMesh(graphics, deviceCoordinates, finalColor);
    } else {
      Polygon3dRenderer.render(
        graphics,
        deviceCoordinates,
        finalColor);
    }
  }

  private void renderMesh(Graphics3d graphics, Vector3d[] vpcVertices, int color) {
    for (int i = 0, j = 1; i < vpcVertices.length; i++, j++, j%=vpcVertices.length) {
      Line3dRenderer.render(
          graphics, 
          vpcVertices[i], 
          vpcVertices[j], 
          color);
    }
  }
  
  protected boolean shouldCullIfBackface(Vector3d center, Vector3d normal) {
    return !options.isEnabled(Option.meshOnly)
        && options.isEnabled(Option.cullIfBackface) 
        && !color.isTransparent();
  }
}
