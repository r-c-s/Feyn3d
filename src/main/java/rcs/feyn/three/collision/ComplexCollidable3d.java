package rcs.feyn.three.collision;

public interface ComplexCollidable3d extends Collidable3d {
    
  public BoundingObject3d[] getInnerBoundingObjects(); 
}
