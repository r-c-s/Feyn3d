package rcs.feyn.three.optics;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.kernel.FeynRuntime;

public class LightingUtils {
  
  public static final double computeLightingIntensity(
      Vector3d point, Vector3d normal, boolean bothSides) {
    return computeLightingIntensity(point, normal, null, bothSides);
  } 

  public static final double computeLightingIntensity(
      Vector3d point, Vector3d normal, Matrix44 view, boolean bothSides) {
    double ambient = FeynRuntime.getAmbientLight().getIntensity();
    double intensity = ambient;
    
    DiffuseLightSource3d[] lightSources = FeynRuntime.getDiffuseLightSources();
    
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
    DiffuseLightSource3d[] lightSources = FeynRuntime.getDiffuseLightSources();
    int color = objectColor;
    for (var lightSource : lightSources) {      
      FeynColor lsColor = lightSource.getColor();
      if (lsColor != null) {
        double intensity;
        if (view != null) {
          intensity = lightSource.getIntensityAt(position, normal, view);
        } else {
          intensity = lightSource.getIntensityAt(position, normal);
        }
        if (intensity > 0) {
          color = ColorUtils.blendRGB(color, lsColor.getRGBA(), intensity);
        }
      }
    }
    return color;
  }
  
  public static final int applyLightsourceColorTo(int objectColor, double intensity) {
    DiffuseLightSource3d[] lightSources = FeynRuntime.getDiffuseLightSources();
    int color = objectColor;
    for (var lightSource : lightSources) {      
      FeynColor lsColor = lightSource.getColor();
      if (lsColor != null) {
        color = ColorUtils.blendRGB(color, lsColor.getRGBA(), intensity);
      }
    }
    return color;
  }
  
  public static final boolean hasColoredLightsources() {
    return Arrays.stream(FeynRuntime.getDiffuseLightSources())
        .map(DiffuseLightSource3d::getColor)
        .anyMatch(Predicate.not(Objects::isNull));
  }
}
