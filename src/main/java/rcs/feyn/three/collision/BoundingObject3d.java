package rcs.feyn.three.collision;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.Movable3d;
import rcs.feyn.three.geo.Transformable3d;

public abstract class BoundingObject3d implements Movable3d, Transformable3d {
  
  public static final int ORIENTATION_INSIDE  = -1;
  public static final int ORIENTATION_OUTSIDE =  1;
  
  protected Vector3d position = new Vector3d();
  protected int orientation = 1; 
  
  public BoundingObject3d(Vector3d position) {
    this.position = new Vector3d(position);
  }
  
  public Vector3d getPosition() {
    return new Vector3d(position);
  }
  
  @Override
  public void translate(Vector3d delta) {
    position.addLocal(delta); 
  } 
  
  @Override
  public void transform(Matrix44 transform) {
    position.affineTransformLocal(transform);
  }
  
  public final int getOrientation() {
    return orientation;
  }
  
  public void inverse() {
    orientation = -orientation;
  }

  public final boolean collidedWith(BoundingObject3d bo) {
    return computeCollision(bo) != null;
  }
  
  public abstract CollisionInfo3d computeCollision(BoundingObject3d bo);
  
  public abstract boolean contains(Vector3d v3d);
}