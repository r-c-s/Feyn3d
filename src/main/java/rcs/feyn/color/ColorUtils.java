package rcs.feyn.color;

import rcs.feyn.math.MathUtils;

public final class ColorUtils {

  private ColorUtils() {
    throw new AssertionError();
  }

  public static int alphaBlend(int inFront, int behind) {
    int alphaInFront = getAlphaFromRGBA(inFront);
    
    // trivial cases
    if (alphaInFront == 0) {
      return behind;
    }
    if (alphaInFront == 255) {
      return inFront;
    }
    
    double prcInFront = (double) alphaInFront / 255;
    double prcBehind = 1 - prcInFront;

    int newr = (int) (prcBehind * getRedFromRGBA(behind)   + prcInFront * getRedFromRGBA(inFront));
    int newg = (int) (prcBehind * getGreenFromRGBA(behind) + prcInFront * getGreenFromRGBA(inFront));
    int newb = (int) (prcBehind * getBlueFromRGBA(behind)  + prcInFront * getBlueFromRGBA(inFront));
    int newa = (int) (prcBehind * getAlphaFromRGBA(behind) + prcInFront * alphaInFront);
    
    return getRGBA(newr, newg, newb, newa);
  }
  
  public static boolean isTransparent(int rgba) {
    return getAlphaFromRGBA(rgba) < 255;
  }

  public static int getRGBA(int r, int g, int b, int a) {
    int A = (a << 24) & 0xFF000000;
    int R = (r << 16) & 0x00FF0000;
    int G = (g <<  8) & 0x0000FF00;
    int B =  b        & 0x000000FF;
    
    return A | R | G | B;
  }

  public static int getAlphaFromRGBA(int rgba) {
    return (rgba >> 24) & 0x000000FF;
  }

  public static int getRedFromRGBA(int rgba) {
    return (rgba >> 16) & 0x000000FF;
  }

  public static int getGreenFromRGBA(int rgba) {
    return (rgba >>  8) & 0x000000FF;
  }

  public static int getBlueFromRGBA(int rgba) {
    return  rgba & 0x000000FF;
  }

  public static int setAlphaToRGBA(int rgba, int a) {
    return (a << 24) | (0x00FFFFFF & rgba);
  }

  public static int setRedToRGBA(int rgba, int r) {
    return (r << 16) | (0xFF00FFFF & rgba);
  }

  public static int setGreenToRGBA(int rgba, int g) {
    return (g <<  8) | (0xFFFF00FF & rgba);
  }

  public static int setBlueToRGBA(int rgba, int b) {
    return  b | (0xFFFFFF00 & rgba);
  }

  public static final int mulRGB(int rgba, double factor) {
    return getRGBA(
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getRedFromRGBA(rgba) * factor))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getGreenFromRGBA(rgba) * factor))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getBlueFromRGBA(rgba) * factor))),
            getAlphaFromRGBA(rgba));
  }

  public static final int mulRGBA(int rgba, double factor) {
    return getRGBA(
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getRedFromRGBA(rgba) * factor))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getGreenFromRGBA(rgba) * factor))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getBlueFromRGBA(rgba) * factor))),
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getAlphaFromRGBA(rgba) * factor))));
  }

  public static final int addRGBA(int rgba0, int rgba1) {
    return getRGBA(
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getRedFromRGBA(rgba0) + getRedFromRGBA(rgba1)))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getGreenFromRGBA(rgba0) + getGreenFromRGBA(rgba1)))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getBlueFromRGBA(rgba0) + getBlueFromRGBA(rgba1)))), 
            MathUtils.min(255, MathUtils.max(0, MathUtils.roundToInt(getAlphaFromRGBA(rgba0) + getAlphaFromRGBA(rgba1)))));
  }
  
  public static int blendRGB(int a, int b, double factor) {
    if (factor < 0) {
      factor = 0;
    } 
    if (factor > 1) {
      factor = 1;
    }
    int blended = alphaBlend(
        setAlphaToRGBA(a, MathUtils.roundToInt((1 - factor) * 255)), 
        setAlphaToRGBA(b, MathUtils.roundToInt(factor * 255)));
    return setAlphaToRGBA(blended, getAlphaFromRGBA(a));
  }
}
