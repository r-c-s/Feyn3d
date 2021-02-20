package rcs.feyn.three.collision;

public interface ComplexCollidable3d extends Collidable3d {
    
  BoundingObject3d[] getInnerBoundingObjects(); 
}
