package rcs.feyn.three.render.patches;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.renderers.Point3dRenderer;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Point3dPatch extends Patch3d {
  
  protected Vector3d point;

  public Point3dPatch(Vector3d point, FeynColor color, RenderOptions3d options) {
    super(color, options);
    this.point = point;
  }

  @Override
  public Vector3d getCenter() {
    return point;
  }

  @Override
  public final void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d[] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(new Vector3d[] {point}, view);
    Vector3d[] clippedViewSpaceCoordinates = Pipeline3d
        .clipViewSpaceCoordinates(viewSpaceCoordinates);

    if (clippedViewSpaceCoordinates.length == 0) {
      return;
    }
    
    Vector3d[] deviceCoordinates = Pipeline3d
        .toDeviceCoordinates(clippedViewSpaceCoordinates, projection, viewPort);
    
    Point3dRenderer.render(
        graphics,
        deviceCoordinates[0],
        color.getRGBA());
  }
}
