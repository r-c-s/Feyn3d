package rcs.feyn.three.optics;

import rcs.feyn.color.Colorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.Particle3d;

public class ConstantLightSource3d extends Particle3d implements Colorable, DiffuseLightSource3d {
  
  protected FeynColor color;
  protected double intensity;
  
  public ConstantLightSource3d(double intensity) {
    this(intensity, null);
  }

  public ConstantLightSource3d(double intensity, FeynColor color) {
    this.intensity = intensity;
    this.color = color;
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal) {
    double alpha = color == null ? 255 : color.getAlpha();
    return intensity * this.position
        .sub(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mulLocal(alpha / 255.0));
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view) {
    double alpha = color == null ? 255 : color.getAlpha();
    return intensity * this.position
        .affineTransform(view)
        .subLocal(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mulLocal(alpha / 255.0));
  }

  @Override
  public FeynColor getColor() {
    return color;
  }

  @Override
  public void setColor(FeynColor color) {
    this.color = color;
  }
}
