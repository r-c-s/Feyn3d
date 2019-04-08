package rcs.feyn.utils;

import java.util.Stack;

public final class StringUtils {
 
  private StringUtils() {
    throw new AssertionError();
  } 

  public static String replaceFirst(String str, String substr, String replacement) {
    return str.replace(substr, replacement);
  }

  public static String replaceLast(String str, String substr, String replacement) {
    int index = str.lastIndexOf(substr);
    return str.substring(0, index) + replacement + str.substring(index + substr.length(), str.length());
  } 

  public static String reverse(String str) { 
    int len = str.length();
    if (len == 0) {
      return str; //or ""
    }
    else {
      return str.charAt(len-1) + reverse(str.substring(0, len-1));
    }
  } 

  public static String join(Object[] str, String delimiter) { 
    String s = str.length > 0 ? str[0].toString() : "";
    for (int i = 0; i < str.length-1; i++) {
      s += delimiter + str[i];
    }
    return s;
  }

  public static String[] split(String line) {
    return split(line, " ");
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

  public static boolean isDouble(String s) {
    try { 
      Double.parseDouble(s);
      return true;
    }
    catch (Exception e){
      return false;
    }
  }

  public static boolean isInteger(String s) {
    try { 
      Integer.parseInt(s);
      return true;
    }
    catch (Exception e){
      return false;
    }
  }

  public static boolean isNumeric(String s) {
    return isInteger(s) || isDouble(s);
  }

  public static boolean isPalindrome(String text) {
    int len = text.length();
    
    if (len <= 1) {
      return true;
    }
    
    return text.charAt(0) == text.charAt(len-1) && isPalindrome(text.substring(1, len-1));
  }

  public static boolean validateBrackets(String str) {
    char[] keys = str.replaceAll("[^()\\[\\]{}<>]", "").toCharArray();
    
    Stack<Character> stack = new Stack<>();
    
    for (char key : keys) {
      switch (key) {
        case ')': if (stack.isEmpty() || stack.pop() != '(') { return false; } break; 
        case ']': if (stack.isEmpty() || stack.pop() != '[') { return false; } break; 
        case '}': if (stack.isEmpty() || stack.pop() != '{') { return false; } break; 
        case '>': if (stack.isEmpty() || stack.pop() != '<') { return false; } break; 
        default : stack.push(key); 
      } 
    }

    return stack.size() == 0;
  }
}
