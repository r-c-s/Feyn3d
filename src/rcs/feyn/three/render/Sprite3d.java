package rcs.feyn.three.render;

import rcs.feyn.three.Particle3d;
import rcs.feyn.utils.struct.FeyngGarbageCollectable;
import rcs.feyn.math.linalg.Vector3d;

public abstract class Sprite3d extends Particle3d implements Renderable3d, FeyngGarbageCollectable {
  
  private boolean active = true;

  public abstract Vector3d getCenterOfMass();

  public final void spin(Vector3d axis, double deg) {
    super.rotate(getCenterOfMass(), axis, deg);
  }

  @Override
  public final void destroy() {
    active = false;
  }

  @Override
  public final boolean isDestroyed() {
    return !active;
  }

  @Override
  public String toString() {
    return String.format("%s [c: %s]", super.toString());
  }
}
