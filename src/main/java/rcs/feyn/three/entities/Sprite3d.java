package rcs.feyn.three.entities;

import rcs.feyn.three.Particle3d;
import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.utils.struct.FeynGarbageCollectable;
import rcs.feyn.math.linalg.Vector3d;

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

  @Override
  public String toString() {
    return String.format("%s [c: %s]", super.toString());
  }
}