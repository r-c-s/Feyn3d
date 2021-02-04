package rcs.feyn.utils;

public final class XORShift {
  
  private static XORShift INSTANCE;
  
  public static final int RAND_MAX = Integer.MAX_VALUE;
 
  private int r;

  public static XORShift getInstance() {
    return null == INSTANCE ? (INSTANCE = new XORShift()) : INSTANCE;
  }
  
  private XORShift() {
    reseed();
  }

  public final void reseed() {
    r = (int) (System.currentTimeMillis() % RAND_MAX);
  }

  public final int randomInt() {
    r ^= r <<  21;
    r ^= r >>> 35;
    r ^= r <<   4;
    return r;
  }

  public final int randomInt(int max) {
    return Math.abs(randomInt()) % max;
  }

  public final int randomInt(int min, int max) {
    return min + randomInt(max-min);
  } 

  public final double randomDouble() {
    return (0x7fffffff & randomInt()) / (double) RAND_MAX;
  }

  public final double randomDouble(double max) {
    return randomDouble() * max;
  }

  public final double randomDouble(double min, double max) {
    return min + randomDouble(max-min);
  } 
}