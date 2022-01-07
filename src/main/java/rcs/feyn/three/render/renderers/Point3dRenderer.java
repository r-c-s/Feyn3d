package rcs.feyn.three.render.renderers;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;

public class Point3dRenderer {

  public static void render(Graphics3d graphics, Vector3d deviceCoordinate, int color) { 
    graphics.putPixel(
        MathUtils.roundToInt(deviceCoordinate.x()), 
        MathUtils.roundToInt(deviceCoordinate.y()), 
        deviceCoordinate.z(), 
        color);
  }
}