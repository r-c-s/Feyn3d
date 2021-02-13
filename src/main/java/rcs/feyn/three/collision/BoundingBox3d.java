package rcs.feyn.three.collision;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.Plane3d;

public class BoundingBox3d extends BoundingObject3d {

  protected Plane3d[] sides = new Plane3d[6];

  public BoundingBox3d(double xDim, double yDim, double zDim) {
    this(xDim, yDim, zDim, new Vector3d());
  }
  
  public BoundingBox3d(double xDim, double yDim, double zDim, Vector3d position) {
    super(position);
    
    sides[0] = new Plane3d(position, Vector3d.    X_AXIS);
    sides[1] = new Plane3d(position, Vector3d.NEG_X_AXIS);
    sides[2] = new Plane3d(position, Vector3d.    Y_AXIS);
    sides[3] = new Plane3d(position, Vector3d.NEG_Y_AXIS);
    sides[4] = new Plane3d(position, Vector3d.    Z_AXIS);
    sides[5] = new Plane3d(position, Vector3d.NEG_Z_AXIS);

    sides[0].translate(Vector3d.    X_AXIS.mul(xDim/2));
    sides[1].translate(Vector3d.NEG_X_AXIS.mul(xDim/2));
    sides[2].translate(Vector3d.    Y_AXIS.mul(yDim/2));
    sides[3].translate(Vector3d.NEG_Y_AXIS.mul(yDim/2));
    sides[4].translate(Vector3d.    Z_AXIS.mul(zDim/2));
    sides[5].translate(Vector3d.NEG_Z_AXIS.mul(zDim/2));
  }
  
  public Plane3d[] getPlanes() {
    return sides;
  }

  @Override
  public void translate(Vector3d delta) {
    super.translate(delta);
    
    for (Plane3d side : sides) {
      side.translate(delta);
    }
  }
  
  @Override
  public void transform(Matrix44 transform) {
    super.transform(transform);
    
    for (Plane3d side : sides) {
      side.transform(transform);
    }
  }
 
  @Override
  public boolean contains(Vector3d v3d) {
    for (Plane3d side : sides) {
      if (side.signedDistance(v3d) > 0) {
        return orientation == -1; 
      }
    } 
    return true;
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
  
  @Override
  public void inverse() {
    super.inverse();
    
    for (Plane3d side : sides) {
      side.reverseOrientation();
    }
  }
}
