package rcs.feyn.three.optics;

import rcs.feyn.three.Particle3d;
import rcs.feyn.color.Colorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class ConstantLightSource3d extends Particle3d implements Colorable, DiffuseLightSource3d {
  
  protected FeynColor color;
  
  public ConstantLightSource3d() {
  	this(null);
  }

  public ConstantLightSource3d(FeynColor color) {
    this.color = color;
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal) {
  	double alpha = color == null ? 255 : color.getAlpha();
    return this.position
        .sub(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mul(alpha / 255.0));
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view) {
  	double alpha = color == null ? 255 : color.getAlpha();
    return this.position
        .affineTransform(view)
        .subLocal(position)
        .normalizeLocal()
        .dotProd(normal.normalize().mul(alpha / 255.0));
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
