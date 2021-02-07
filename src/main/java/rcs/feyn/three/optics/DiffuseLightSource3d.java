package rcs.feyn.three.optics;

import rcs.feyn.three.IParticle3d;
import rcs.feyn.color.Colorable;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public interface DiffuseLightSource3d extends IParticle3d, Colorable {

  /**
   * Used when position and normal are world-coordinates
   * 
   * @param position
   * @param normal
   * @return
   */
  public double getIntensityAt(Vector3d position, Vector3d normal); 
  
  /**
   * Used when position and normal are view-coordinates
   * 
   * @param position
   * @param normal
   * @param view matrix
   * @return
   */
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view); 
}
