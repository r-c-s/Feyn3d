package rcs.feyn.three.entities;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.utils.struct.FeynGarbageCollectable;

public abstract class Sprite3d extends Particle3d implements Renderable3d, FeynGarbageCollectable {
  
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
}
