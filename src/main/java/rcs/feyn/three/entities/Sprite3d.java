package rcs.feyn.three.entities;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.utils.struct.FeynGarbageCollectable;

public abstract class Sprite3d extends Particle3d implements Renderable3d, FeynGarbageCollectable {
  
  private long timeOfCreation = System.currentTimeMillis();
  
  private boolean hidden = false;
  private boolean destroyed = false;

  public abstract Vector3d getCenterOfMass();

  public final void spin(Vector3d axis, double deg) {
    super.rotate(getCenterOfMass(), axis, deg);
  }
  
  public final long getTimeOfCreation() {
    return timeOfCreation;
  }
  
  public final void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  
  public final boolean isHidden() {
    return hidden;
  }

  @Override
  public final void destroy() {
    destroyed = true;
  }

  @Override
  public final boolean isDestroyed() {
    return destroyed;
  }
}
