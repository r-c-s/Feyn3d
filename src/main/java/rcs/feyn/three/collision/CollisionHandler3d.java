package rcs.feyn.three.collision;

public interface CollisionHandler3d<T extends Collidable3d, U extends Collidable3d> {
  
    void handleCollision(T t, U u, CollisionInfo3d ci);
}
