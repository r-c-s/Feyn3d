package rcs.feyn.two.render;

import rcs.feyn.two.Particle2d;
import rcs.feyn.struct.FeyngGarbageCollectable;
import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.math.linalg.Vector2d;

public abstract class Sprite2d extends Particle2d implements Renderable3d, FeyngGarbageCollectable {
  
  private boolean active = true;

  public Sprite2d() {
    super();
  }

  public abstract Vector2d getCenterOfMass();

  public final void spin(double deg) {
    super.rotate(getCenterOfMass(), deg);
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
