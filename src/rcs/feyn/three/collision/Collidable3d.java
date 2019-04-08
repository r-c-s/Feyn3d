package rcs.feyn.three.collision;

import rcs.feyn.three.IParticle3d;

public interface Collidable3d extends IParticle3d {
    
  public BoundingObject3d getOuterBoundingObject(); 
}
