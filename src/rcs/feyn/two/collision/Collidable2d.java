package rcs.feyn.two.collision;

import rcs.feyn.two.IParticle2d;

public interface Collidable2d extends IParticle2d {
    
  public BoundingObject2d getOuterBoundingObject();
}
