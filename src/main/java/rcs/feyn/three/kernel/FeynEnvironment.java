package rcs.feyn.three.kernel;

import rcs.feyn.gui.AbstractAnimationCanvas;
import rcs.feyn.physics.Conversions;
import rcs.feyn.physics.Gravity1d;
import rcs.feyn.physics.PhysicsConsts;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;

public class FeynEnvironment {

  public static final double PIXELS_PER_METER; 
  public static final Vector3d WORLD_UP;
  public static final Gravity1d GRAVITY;
  public static final Vector3d GRAVITY_ACCEL;
  
  static {
    PIXELS_PER_METER = 1;
    WORLD_UP = Vector3d.Y_AXIS;
    GRAVITY = new Gravity1d(
            Conversions.metersToPixels(
                    - Math.abs(PhysicsConsts.EARTH_GRAVITY_METERS_PER_SEC) 
                    * MathUtils.squared(AbstractAnimationCanvas.FPS_DELAY_MS / 1000.0)), 
            WORLD_UP);
    GRAVITY_ACCEL = GRAVITY.getAcceleration();
  }
}
