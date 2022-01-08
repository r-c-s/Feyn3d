package rcs.feyn.utils;

public final class ArrayUtils {

  private ArrayUtils() {
    throw new AssertionError();
  }
  
  public static double[] unbox(Double[] D) {
    double[] d = new double[D.length];
    for (int i = 0; i < D.length; i++) {
      d[i] = D[i];
    }
    return d;
  }

  public static int[][] unbox(Integer[][] N) {
    int[][] n = new int[N.length][];
    for (int i = 0; i < N.length; i++) {
      n[i] = unbox(N[i]);
    }
    return n;
  }
  
  public static int[] unbox(Integer[] N) {
    int[] n = new int[N.length];
    for (int i = 0; i < N.length; i++) {
      n[i] = N[i];
    }
    return n;
  }

  public static long[] unbox(Long[] L) {
    long[] l = new long[L.length];
    for (int i = 0; i < L.length; i++) {
      l[i] = L[i];
    }
    return l;
  }

  public static float[] unbox(Float[] F) {
    float[] f = new float[F.length];
    for (int i = 0; i < F.length; i++) {
      f[i] = F[i];
    }
    return f;
  }

  public static Integer[] box(int[] i) {
    Integer[] I = new Integer[i.length];
    for (int j = 0; j < i.length; j++) {
      I[j] = i[j];
    }
    return I;
  }
  
  public static Double[] box(double[] d) {
    Double[] D = new Double[d.length];
    for (int j = 0; j < d.length; j++) {
      D[j] = d[j];
    }
    return D;
  }
  
  public static Long[] box(long[] l) {
    Long[] L = new Long[l.length];
    for (int j = 0; j < l.length; j++) {
      L[j] = l[j];
    }
    return L;
  }
 
  public static Float[] box(float[] f) {
    Float[] F = new Float[f.length];
    for (int j = 0; j < f.length; j++) {
      F[j] = f[j];
    }
    return F;
  }
 
  public static <T> void reverse(T[] t) {
    int size = t.length;
    for (int i = 0; i < size / 2; i++) {
      swap(t, i, size-1-i);
    }
  }
 
  public static void reverse(int[] t) {
    int size = t.length;
    for (int i = 0; i < size / 2; i++) {
      swap(t, i, size-1-i);
    }
  }

  private static <T> void swap(T[] t, int i, int j) {
    T temp = (T) t[i];
    t[i] = t[j];
    t[j] = temp;
  }

  private static void swap(int[] t, int i, int j) {
    int temp = t[i];
    t[i] = t[j];
    t[j] = temp;
  }
}
