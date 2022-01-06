package rcs.feyn.math;

import rcs.feyn.math.MathConsts;

public class TrigLookUp {
  
  private static int partitions;
  private static int inverseStep;

  private static double[] cos;
  private static double[] sin; 
  
  public static void init(double step) {
    if ((360 / step) % 360 != 0) {
      throw new IllegalArgumentException("Error: partition size must be multiple of 360.");
    }
    
    partitions  = (int) (360 / step);
    inverseStep = (int) (1 / step);
   
    int size = partitions / 4 + 1;
    
    cos = new double[size];
    sin = new double[size];
    
    for (int i = 0; i < size; i++) {
      double rad = MathConsts.DEGREES_TO_RADIANS * i * step;
      cos[i] = Math.cos(rad);
      sin[i] = Math.sin(rad);
    } 
  }
  
  public static final double cos(double radian) {
    double angle = MathConsts.RADIANS_TO_DEGREES * radian;
    if (angle < 0) {
      angle = -angle;
    }
    while (angle >= 360) {
      angle -= 360;
    }
    if (angle <= 90) return  cos[(int) (       angle  * inverseStep)];
    if (angle > 270) return  cos[(int) ((360 - angle) * inverseStep)];
    if (angle > 180) return -cos[(int) ((angle - 180) * inverseStep)];
    else             return -cos[(int) ((180 - angle) * inverseStep)];
  }

  public static final double sin(double radian) {
    double angle = MathConsts.RADIANS_TO_DEGREES * radian;
    if (angle < 0) {
      angle = 180 - angle;
    }
    while (angle >= 360) {
      angle -= 360;
    }
    if (angle <= 90) return  sin[(int) (       angle  * inverseStep)];
    if (angle > 270) return -sin[(int) ((360 - angle) * inverseStep)];
    if (angle > 180) return -sin[(int) ((angle - 180) * inverseStep)];
    else             return  sin[(int) ((180 - angle) * inverseStep)];
  }

  public static final double tan(double radian) {
    return sin(radian) / cos(radian);
  } 
}