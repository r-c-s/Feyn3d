package rcs.feyn.three.optics;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.kernel.FeynApp3d;

public class LightingUtils {
  
  public static final double computeLightningIntensity(
      Vector3d point, Vector3d normal, boolean bothSides) {
    return computeLightningIntensity(point, normal, null, bothSides);
  } 

  public static final double computeLightningIntensity(
      Vector3d point, Vector3d normal, Matrix44 view, boolean bothSides) {
    double ambient = FeynApp3d.getAmbientLight().getIntensity();
    double intensity = ambient;
    
    DiffuseLightSource3d lightSource = FeynApp3d.getDiffuseLightSource();
    
    if (lightSource != null) {
      double diffuse;
      
      if (view != null) {
        diffuse = lightSource.getIntensityAt(point, normal, view);
      } else {
        diffuse = lightSource.getIntensityAt(point, normal);
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
  
  public static final int applyLightsourceColorTo(Vector3d position, Vector3d normal, Matrix44 view, int objectColor) {
    DiffuseLightSource3d lightSource = FeynApp3d.getDiffuseLightSource();
    if (lightSource == null) {
      return objectColor;
    }
    
    FeynColor lightColor = lightSource.getColor();
    if (lightColor == null) {
      return objectColor;
    }
    
    double intensity = lightSource.getIntensityAt(position, normal);
    return ColorUtils.blendColors(objectColor, lightColor.getRGBA(), intensity);
  }
  
  public static final int applyLightsourceColorTo(int objectColor) {
    DiffuseLightSource3d lightSource = FeynApp3d.getDiffuseLightSource();
    if (lightSource == null) {
      return objectColor;
    }
    
    FeynColor lightColor = lightSource.getColor();
    if (lightColor == null) {
      return objectColor;
    }
 
    return ColorUtils.blendColors(objectColor, lightColor.getRGBA(), 1);
  }
}
