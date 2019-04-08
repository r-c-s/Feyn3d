package rcs.feyn.three.optics;

import rcs.feyn.three.IParticle3d;
import rcs.feyn.color.Colorable;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public interface DiffuseLightSource3d extends IParticle3d, Colorable {

  public double getIntensityAt(Vector3d position, Vector3d normal); 
  
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view); 
}
