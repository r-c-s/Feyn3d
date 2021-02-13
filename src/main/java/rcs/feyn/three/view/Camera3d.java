package rcs.feyn.three.view;

import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.entities.Particle3d;

public class Camera3d extends Particle3d {

  public Camera3d() {
    super();
  }

  @Override // overrides to synchronize
  public synchronized void translate(Vector3d v3d) {
    super.translate(v3d); 
  }

  @Override // overrides to synchronize
  public synchronized void transform(Matrix44 m4x4) {
    super.transform(m4x4); 
  }
}