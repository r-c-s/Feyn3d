package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.Line3dRenderer;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.three.render.renderers.Polygon3dRenderer;
import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class Polygon3dPatch extends Patch3d {
  
  protected Vector3d[] vertices;

  public Polygon3dPatch(Vector3d[] vertices, FeynColor color, RenderOptions3d options) {
    super(color, options);
    this.vertices = vertices;
  } 

  @Override
  protected final Vector3d getCenter() {
    return GeoUtils3d.getCenter(vertices);
  }
  
  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    
    if (!options.isEnabled(RenderOptions3d.Option.meshOnly)
      && options.isEnabled(RenderOptions3d.Option.cullIfBackface) 
      && !color.isTransparent()
      && ViewUtils.isBackFace(FeynApp3d.getCamera().getPosition(), center, normal)) {
      return;
    }

    Vector3d[] viewSpaceCoordinates = Pipeline3d.getClippedViewSpaceCoordinates(vertices, view);
    Vector3d[] deviceCoordinates = Pipeline3d.getDeviceCoordinates(viewSpaceCoordinates, projection, viewPort);

    double intensity = 1.0;
    if (options.isEnabled(RenderOptions3d.Option.flatShaded)) {
      intensity = LightingUtils.computeLightningIntensity(
            center, 
            normal, 
            options.isEnabled(RenderOptions3d.Option.bothSidesShaded) || options.isEnabled(RenderOptions3d.Option.meshOnly));
    }

    int finalColor;
    if (options.isEnabled(RenderOptions3d.Option.applyLightingColor)) {
      finalColor = LightingUtils.applyLightsourceColorTo(center, normal, color.getRGBA());
    } else {
      finalColor = color.getRGBA();
    }
    finalColor = ColorUtils.mulRGBA(finalColor, intensity);
    
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
    for (int i = 0, j = 1; i < vpcVertices.length; i++, j++, j%=vpcVertices.length) {
      Line3dRenderer.render(
          graphics, 
          vpcVertices[i], 
          vpcVertices[j], 
          ColorUtils.mulRGBA(color, options.isEnabled(RenderOptions3d.Option.meshShaded) ? intensity : 1));
    }
  }
}
