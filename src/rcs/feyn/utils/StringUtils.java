package rcs.feyn.utils;

public final class StringUtils {
 
  private StringUtils() {
    throw new AssertionError();
  } 

  public static String[] split(String line, String delimiter) {
    String[] array = line.split(delimiter);
    int oldSize = array.length;
    int newSize = oldSize;
    for (String s : array) {
      if (s.length() == 0) {
        newSize--;
      }
    }
    if (newSize == oldSize) {
      return array;
    }
    String[] newArray = new String[newSize];
    int j = 0;
    for (String s : array) {
      if (s.length() != 0) {
        newArray[j++] = s;
      }
    }
    return newArray;
  }
}
