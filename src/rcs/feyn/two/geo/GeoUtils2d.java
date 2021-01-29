package rcs.feyn.two.geo;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.MathConsts;

public class GeoUtils2d {
  
  public static final double DEFAULT_CIRCLE_SIDES_ERROR_THRESHOLD = 0.001;

  private GeoUtils2d() {
    throw new AssertionError();
  }

  public static final int getNumberOfSidesOfCircle(double radius) {
    return getNumberOfSidesOfCircle(radius, DEFAULT_CIRCLE_SIDES_ERROR_THRESHOLD);
  }

  public static final int getNumberOfSidesOfCircle(double radius, double error) {
    return (int) Math.ceil(MathConsts.TWO_PI / Math.acos(2 * MathUtils.squared(1 - error / radius) - 1));
  }
}
