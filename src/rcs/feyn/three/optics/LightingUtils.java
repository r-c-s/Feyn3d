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
      intensity += (1-ambient) * diffuse;
    }
    
    return intensity;
  } 
  
  public static final int applyLightning(int color) {
  	FeynColor lightingColor = FeynApp3d.getDiffuseLightSource().getColor();
  	return lightingColor == null 
  			?	color 
  			: applyLightiningColor(color, lightingColor.getRGBA());
  }
  
  private static final int applyLightiningColor(int objectColor, int lightColor) {
    int objectAlpha = ColorUtils.getAlphaFromRGBA(objectColor);
    int lightAlpha = ColorUtils.getAlphaFromRGBA(lightColor);
    int blendAlpha = Math.abs(objectAlpha - lightAlpha);
    int blended = ColorUtils.alphaBlend(ColorUtils.setAlphaToRGBA(objectColor, blendAlpha), lightColor);
    return ColorUtils.setAlphaToRGBA(blended, objectAlpha);
  }
}
