package rcs.feyn.two.collision;

import rcs.feyn.math.linalg.Vector2d;
import rcs.feyn.three.collision.CollisionInfo3d;
import rcs.feyn.two.geo.Transformable2d;

public interface BoundingObject2d extends Transformable2d {
  
  public CollisionInfo3d computeCollision(BoundingObject2d bo);
  
  public boolean collidedWith(BoundingObject2d bo);
  
  public boolean contains(Vector2d v2d);
  
  public void reverse();
}