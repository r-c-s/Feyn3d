package rcs.feyn.three.render.patches;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.Line3dRenderer;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class Line3dPatch extends Patch3d {
  
  protected Vector3d a, b;

  public Line3dPatch(Vector3d a, Vector3d b, FeynColor color) {
    super(color);
    this.a = a;
    this.b = b;
  }

  @Override
  protected Vector3d getCenter() {
    return a.midPoint(b);
  }

  @Override
  public final void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d[] viewSpaceCoordinates = Pipeline3d.getClippedViewSpaceCoordinates(new Vector3d[] {a, b}, view);
    
    if (viewSpaceCoordinates.length < 2) {
      return;
    }
    
    Vector3d[] deviceCoordinates = Pipeline3d.getDeviceCoordinates(viewSpaceCoordinates, projection, viewPort);

    int colorWithLighting = options.isEnabled(RenderOptions3d.Option.applyLightingColor) 
    		? LightingUtils.applyLightsourceColorTo(color.getRGBA())
    		: color.getRGBA();
    
    Line3dRenderer.render(
        graphics,
        deviceCoordinates[0], 
        deviceCoordinates[1],
        colorWithLighting);
  }
}
