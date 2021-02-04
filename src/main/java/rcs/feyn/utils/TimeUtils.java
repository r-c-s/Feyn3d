package rcs.feyn.utils;

public final class TimeUtils {

  private TimeUtils() {
    throw new AssertionError();
  }

  public static long getElapsedTimeMillis(Runnable r) {
    long start = System.currentTimeMillis();
    r.run();
    return System.currentTimeMillis() - start;
  }  
  
  public static long getElapsedTimeNanos(Runnable r) {
    long start = System.nanoTime();
    r.run();
    return System.nanoTime() - start;
  }
}
