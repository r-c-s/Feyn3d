package rcs.feyn.three.collision;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class BoundingSphere3d extends BoundingObject3d {
  
  protected double radius;
  
  public BoundingSphere3d(double radius) {
    this(radius, new Vector3d());
  }
  
  public BoundingSphere3d(double radius, Vector3d position) {
    super(position);
    this.radius = radius;
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  } 
  
  @Override
  public void transform(Matrix44 transform) { 
    super.transform(transform); 
    
    radius *= transform.extractScaleX();
  }
 
  @Override
  public boolean contains(Vector3d v3d) {
    return position.distanceSquared(v3d) <= radius*radius;
  }

  @Override
  public CollisionInfo3d computeCollision(BoundingObject3d bo) {
    if (bo instanceof BoundingSphere3d) {
      return CollisionDetection3d.computeCollision(this, (BoundingSphere3d) bo);
    }
    if (bo instanceof BoundingBox3d) { 
      return CollisionDetection3d.computeCollision(this, (BoundingBox3d) bo);
    }
    throw new UnsupportedOperationException();
  }
}
