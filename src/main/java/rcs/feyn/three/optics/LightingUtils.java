package rcs.feyn.three.optics;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.kernel.FeynApp3d;

public class LightingUtils {
  
  public static final double computeLightingIntensity(
      Vector3d point, Vector3d normal, boolean bothSides) {
    return computeLightingIntensity(point, normal, null, bothSides);
  } 

  public static final double computeLightingIntensity(
      Vector3d point, Vector3d normal, Matrix44 view, boolean bothSides) {
    double ambient = FeynApp3d.getAmbientLight().getIntensity();
    double intensity = ambient;
    
    DiffuseLightSource3d[] lightSources = FeynApp3d.getDiffuseLightSources();
    
    for (int i = 0; i < lightSources.length; i++) {
      double diffuse;
      
      if (view != null) {
        diffuse = lightSources[i].getIntensityAt(point, normal, view);
      } else {
        diffuse = lightSources[i].getIntensityAt(point, normal);
      }
      
      if (bothSides) {
        diffuse = Math.abs(diffuse);
      } else {
        diffuse = Math.max(0, diffuse);
      }
      
      intensity += diffuse;
    }
    
    return intensity;
  } 
  
  public static final int applyLightsourceColorTo(Vector3d position, Vector3d normal, int objectColor) {
    return applyLightsourceColorTo(position, normal, null, objectColor);
  }
  
  public static final int applyLightsourceColorTo(Vector3d position, Vector3d normal, Matrix44 view, int objectColor) {
    DiffuseLightSource3d[] lightSources = FeynApp3d.getDiffuseLightSources();
    int color = objectColor;
    for (int i = 0; i < lightSources.length; i++) {
      double intensity;
      
      if (view != null) {
        intensity = lightSources[i].getIntensityAt(position, normal, view);
      } else {
        intensity = lightSources[i].getIntensityAt(position, normal);
      }
      
      FeynColor lsColor = lightSources[i].getColor();
      if (lsColor != null) {
        color = ColorUtils.blendRGB(color, lsColor.getRGBA(), intensity);
      }
    }
    return color;
  }
  
  public static final int applyLightsourceColorTo(int objectColor, double intensity) {
    DiffuseLightSource3d[] lightSources = FeynApp3d.getDiffuseLightSources();
    int color = objectColor;
    for (int i = 0; i < lightSources.length; i++) {
      color = ColorUtils.blendRGB(color, lightSources[i].getColor().getRGBA(), intensity);
    }
    return color;
  }
}
