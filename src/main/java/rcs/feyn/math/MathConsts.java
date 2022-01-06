package rcs.feyn.math;

public final class MathConsts {

  public static final double SQURT_2            = Math.sqrt(2);
  public static final double SQURT_3            = Math.sqrt(3);
  public static final double ONE_QUARTER        = 0.25;
  public static final double ONE_HALF           = 0.50;
  public static final double THREE_HALFS        = 1.50;
  public static final double THREE_QUARTERS     = 0.75;
  public static final double EPSILON_0_1        = 0.1d;
  public static final double EPSILON_0_01       = 0.01d;
  public static final double EPSILON_0_001      = 0.001d;
  public static final double EPSILON_0_0001     = 0.0001d;
  public static final double EPSILON_0_00001    = 0.00001d;
  public static final double PHI                = 1.618033988749895;
  public static final double E                  = Math.E;
  public static final double PI                 = Math.PI; 
  public static final double HALF_PI            = Math.PI / 2; 
  public static final double QUARTER_PI         = Math.PI / 4;
  public static final double THREE_QUARTER_PI  = 3 * Math.PI / 4;
  public static final double TWO_PI             = Math.PI * 2;
  public static final double THREE_HALFS_PI     = 3 * Math.PI / 2;
  public static final double DEGREES_TO_RADIANS = Math.PI / 180;
  public static final double RADIANS_TO_DEGREES = 180 / Math.PI;
  
  private MathConsts() {
    throw new AssertionError();
  }
}
