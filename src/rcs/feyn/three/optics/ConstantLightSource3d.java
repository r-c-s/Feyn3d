package rcs.feyn.three.optics;

import rcs.feyn.three.Particle3d;
import rcs.feyn.color.Colorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class ConstantLightSource3d extends Particle3d implements Colorable, LightSource3d, DiffuseLightSource3d {
  
  protected FeynColor color;
  protected double intensity;
  
  public ConstantLightSource3d(double intensity) {
    this(FeynColor.white, intensity);
  }

  public ConstantLightSource3d(FeynColor color, double intensity) {
    this.color = color;
    this.intensity = intensity;
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal) {
    return this.position
        .sub(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mul(color.getAlpha() / 255.0));
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view) {
    return this.position
        .affineTransform(view)
        .subLocal(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mul(color.getAlpha() / 255.0));
  }

  @Override
  public FeynColor getColor() {
    return color;
  }

  @Override
  public void setColor(FeynColor color) {
    this.color = color;
  }

  @Override
  public double getIntensity() {
    return intensity;
  }
}
