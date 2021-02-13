package rcs.feyn.three.optics;

import rcs.feyn.color.Colorable;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.IParticle3d;

public interface DiffuseLightSource3d extends IParticle3d, Colorable {

  /**
<<<<<<< HEAD
   * Used when position and normal are world-coordinates
   * 
   * @param position
   * @param normal
   * @return
=======
   * Used when position and normal are world coordinates (before applying Pipeline3d.getClippedViewSpaceCoordinates)
>>>>>>> master
   */
  public double getIntensityAt(Vector3d position, Vector3d normal); 
  
  /**
<<<<<<< HEAD
   * Used when position and normal are view-coordinates
   * 
   * @param position
   * @param normal
   * @param view matrix
   * @return
=======
   * Used when position and normal are view coordinates (after applying Pipeline3d.getClippedViewSpaceCoordinates)
>>>>>>> master
   */
  public double getIntensityAt(Vector3d position, Vector3d normal, Matrix44 view); 
}
