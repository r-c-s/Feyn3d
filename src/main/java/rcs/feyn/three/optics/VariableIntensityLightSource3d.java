package rcs.feyn.three.optics;

import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class VariableIntensityLightSource3d extends ConstantLightSource3d {
  
  public VariableIntensityLightSource3d(double intensity) {
    this(intensity, null);
  }
  
  public VariableIntensityLightSource3d(double intensity, FeynColor color) {
    super(intensity, color);
  }

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal) {
    Vector3d direction = this.position.sub(position);
    double distSquared = direction.lengthSquared();
    return intensity * direction.divLocal(Math.sqrt(distSquared)).dotProd(normal) / distSquared;
  } 

  @Override
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view) {
    Vector3d direction = this.position.affineTransform(view).subLocal(position);
    double distSquared = direction.lengthSquared();    
    return intensity * direction.divLocal(Math.sqrt(distSquared)).dotProd(normal) / distSquared;
  } 
}
