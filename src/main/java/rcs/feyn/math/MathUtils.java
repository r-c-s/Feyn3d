package rcs.feyn.math;

import rcs.feyn.math.MathConsts;

public final class MathUtils {
  
  private static final double DEFAULT_EPSILON = MathConsts.EPSILON_0_001;
  
  private MathUtils() {
    throw new AssertionError();
  }
  
  public static double determinant(
          double a, double b, double c, double d,
          double e, double f, double g, double h,
          double i, double j, double k, double l,
          double m, double n, double o, double p) {
    return a*(determinant(f, g, h, j, k, l, n, o, p)) -
           b*(determinant(e, g, h, i, k, l, m, o, p)) +
           c*(determinant(e, f, h, i, j, l, m, n, p)) -
           d*(determinant(e, f, g, i, j, k, m, n, o));
  }
  
  public static double determinant(
          double a, double b, double c,
          double d, double e, double f,
          double g, double h, double i) {
    return a*(determinant(e, f, h, i)) - 
           b*(determinant(d, f, g, i)) + 
           c*(determinant(d, e, g, h));
  }
  
  public static double determinant(
          double a, double b,
          double c, double d) {
    return a*d - c*b;
  }
  
  public static int hashDouble(double d) { 
    long longBits = Double.doubleToLongBits(d);
    return (int) (longBits ^ (longBits >>> 32));
  }
  
  public static double invSqrt(double x) {
    double xhalf = 0.5d*x;
    long i = Double.doubleToLongBits(x);
    i = 0x5fe6ec85e7de30daL - (i>>1);
    x = Double.longBitsToDouble(i);
    x = x*(1.5d - xhalf*x*x);
    return x;
  }

  public static int roundToInt(double n) {
    return n < 0f ? (int) (n - 0.5f) 
                  : (int) (n + 0.5f);
  }
  
  public static double round(double num, int significantDigits) {
    if (significantDigits < 0) {
      throw new IllegalArgumentException();
    }
    double powerOfTen = Math.pow(10, significantDigits);
    double offset     = 0.5 / powerOfTen;
    return ((int) ((num + offset) * powerOfTen)) / (double) powerOfTen;
  }

  public static boolean isInRangeInclusive(double n, double lowerBound, double upperBound) {
    return n >= lowerBound && n <= upperBound;
  }

  public static boolean isInRangeInclusive(int n, int lowerBound, int upperBound) {
    return n >= lowerBound && n <= upperBound;
  }

  public static boolean isInRangeExclusive(double n, double lowerBound, double upperBound) {
    return n >  lowerBound && n <  upperBound;
  }

  public static boolean isInRangeExclusive(int n, int lowerBound, int upperBound) {
    return n >  lowerBound && n <  upperBound;
  }
  
  public static boolean epsilonZero(double n) {
      return epsilonZero(n, DEFAULT_EPSILON);
  }
  
  public static boolean epsilonZero(double n, double eps) {
    return Math.abs(n) <= eps;
  }

  public static boolean epsilonEquals(double n1, double n2) {
    return epsilonEquals(n1, n2, DEFAULT_EPSILON);
  }

  public static boolean epsilonEquals(double n1, double n2, double eps) {
    return Math.abs(n1 - n2) < eps;
  }
  
  public static boolean epsilonEquals(Vector3d a, Vector3d b) {
    return epsilonEquals(a, b, DEFAULT_EPSILON);
  }
  
  public static boolean epsilonEquals(Vector3d a, Vector3d b, double eps) {
    return epsilonEquals(a.x(), b.x(), eps) 
        && epsilonEquals(a.y(), b.y(), eps) 
        && epsilonEquals(a.z(), b.z(), eps);
  }

  public static int signum(double n) {
    return n < 0 ? -1 : (n > 0 ? 1 : 0);
  }

  public static long gcd(long a, long b) {
    if (a % b == 0) {
      return b;
    }
    return gcd(b, a % b);
  }

  public static long lcm(long a, long b) {
    if (Math.abs(a) == Math.abs(b)) {
      return Math.abs(a);
    }
    return Math.abs(a * b) / gcd(a, b);
  }

  public static boolean isOdd(long n) {
    return (n & 1) == 1;
  }

  public static boolean isEven(long n) {
    return !isOdd(n);
  }

  public static boolean isPrime(long n) {
    if (n == 2) {
      return true;
    }
    if (n < 2 || isEven(n)) {
      return false;
    }
    for (int i = 3; i*i <= n; i++){
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static boolean isComposite(long n) {
    return !isPrime(n);
  }

  public static boolean isPowerOfTwo(long n) {
    return n > 0 && (n & n - 1) == 0;
  }
  
  public static int squared(int d) {
    return d * d;
  }

  public static int cubed(int d) {
    return d * d * d;
  }

  public static double squared(double d) {
    return d * d;
  }

  public static double cubed(double d) {
    return d * d * d;
  }

  public static long fib(int n) {
    return n <= 1 ? n : fib(n-1) + fib(n-2);
  }

  public static long summation(long n) {
    if (n < 0) {
      throw new IllegalArgumentException("Error: cannot compute the summation of negative number.");
    }
    return n * (n + 1) / 2;
  }

  public static long factorial(long n) {
    if (n <  0) {
      throw new IllegalArgumentException("Error: cannot compute the factorial of negative number.");
    }
    if (n == 0) {
      return 1;
    }
    int fact = 1;
    for (int i = 2; i < n; i++) {
      fact *= i;
    }
    return fact;
  }
  
  public static double[] extent(double... arr) {
    return new double[]{min(arr), max(arr)};
  }
  
  public static float[] extent(float... arr) {
    return new float[]{min(arr), max(arr)};
  }
  
  public static int[] extent(int... arr) {
    return new int[]{min(arr), max(arr)};
  }
  
  public static long[] extent(long... arr) {
    return new long[]{min(arr), max(arr)};
  }

  public static double max(double a, double b) {
    return a > b ? a : b;
  }

  public static double max(double a, double b, double c) {
    return max(a, max(b, c));
  }
  
  public static double max(double... arr) {
    double max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    return max;
  }

  public static float max(float a, float b) {
    return a > b ? a : b;
  }

  public static float max(float a, float b, float c) {
    return max(a, max(b, c));
  }
  
  public static float max(float... arr) {
    float max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    return max;
  }

  public static int max(int a, int b) {
    return a > b ? a : b;
  }

  public static int max(int a, int b, int c) {
    return max(a, max(b, c));
  }
  
  public static int max(int... arr) {
    int max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    return max;
  }

  public static long max(long a, long b) {
    return a > b ? a : b;
  }

  public static long max(long a, long b, long c) {
    return max(a, max(b, c));
  } 
  
  public static long max(long... arr) {
    long max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    return max;
  }

  public static double min(double a, double b) {
    return a < b ? a : b;
  }

  public static double min(double a, double b, double c) {
    return min(a, min(b, c));
  }
  
  public static double min(double... arr) {
    double min = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      }
    }
    return min;
  }

  public static float min(float a, float b) {
    return a < b ? a : b;
  }

  public static float min(float a, float b, float c) {
    return min(a, min(b, c));
  }
  
  public static float min(float... arr) {
    float min = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      }
    }
    return min;
  }

  public static int min(int a, int b) {
    return a < b ? a : b;
  }

  public static int min(int a, int b, int c) {
    return min(a, min(b, c));
  }
  
  public static int min(int... arr) {
    int min = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      }
    }
    return min;
  }

  public static long min(long a, long b) {
    return a < b ? a : b;
  }

  public static long min(long a, long b, long c) {
    return min(a, min(b, c));
  } 
  
  public static long min(long... arr) {
    long min = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      }
    }
    return min;
  }
}
