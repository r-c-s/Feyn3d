package rcs.feyn.utils;


public final class TimeUtils {

  public static final long DAYS_IN_YEAR      = 365;
  public static final long DAYS_IN_LEAP_YEAR = 366;
  
  public static final long HOURS_IN_DAY       = 24;
  public static final long HOURS_IN_YEAR      = HOURS_IN_DAY * DAYS_IN_YEAR;
  public static final long HOURS_IN_LEAP_YEAR = HOURS_IN_DAY * DAYS_IN_LEAP_YEAR;
  
  public static final long MINUTES_IN_HOUR      = 60;
  public static final long MINUTES_IN_DAY       = MINUTES_IN_HOUR * HOURS_IN_DAY;
  public static final long MINUTES_IN_YEAR      = MINUTES_IN_DAY * DAYS_IN_YEAR;
  public static final long MINUTES_IN_LEAP_YEAR = MINUTES_IN_DAY * DAYS_IN_LEAP_YEAR;

  public static final long SECONDS_IN_MINUTE    = 60;
  public static final long SECONDS_IN_HOUR      = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
  public static final long SECONDS_IN_DAY       = SECONDS_IN_HOUR * HOURS_IN_DAY;
  public static final long SECONDS_IN_YEAR      = SECONDS_IN_DAY * DAYS_IN_YEAR;
  public static final long SECONDS_IN_LEAP_YEAR = SECONDS_IN_DAY * DAYS_IN_LEAP_YEAR;
  
  public static final long MILLISECONDS_IN_SECOND    = 1000;
  public static final long MILLISECONDS_IN_MINUTE    = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;
  public static final long MILLISECONDS_IN_HOUR      = MILLISECONDS_IN_MINUTE * MINUTES_IN_HOUR;
  public static final long MILLISECONDS_IN_DAY       = MILLISECONDS_IN_HOUR * HOURS_IN_DAY;
  public static final long MILLISECONDS_IN_YEAR      = MILLISECONDS_IN_DAY * DAYS_IN_YEAR;
  public static final long MILLISECONDS_IN_LEAP_YEAR = MILLISECONDS_IN_DAY * DAYS_IN_LEAP_YEAR;
  
  public static final long NANOSECONDS_IN_MILLISECONDS = 1000000;
  public static final long NANOSECONDS_IN_SECOND       = NANOSECONDS_IN_MILLISECONDS * MILLISECONDS_IN_SECOND;
  public static final long NANOSECONDS_IN_MINUTE       = NANOSECONDS_IN_SECOND * SECONDS_IN_MINUTE;
  public static final long NANOSECONDS_IN_HOUR         = NANOSECONDS_IN_MINUTE * MINUTES_IN_HOUR;
  public static final long NANOSECONDS_IN_DAY          = NANOSECONDS_IN_HOUR * HOURS_IN_DAY;
  public static final long NANOSECONDS_IN_YEAR         = NANOSECONDS_IN_DAY * DAYS_IN_YEAR;
  public static final long NANOSECONDS_IN_LEAP_YEAR    = NANOSECONDS_IN_DAY * DAYS_IN_LEAP_YEAR;
  
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
  
  public static boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }
}
