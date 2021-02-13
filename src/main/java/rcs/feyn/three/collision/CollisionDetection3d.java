package rcs.feyn.three.collision;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.Plane3d;

public class CollisionDetection3d {
  
  private CollisionDetection3d() {
    throw new AssertionError();
  }
  
  public static final CollisionInfo3d computeCollision(Collidable3d c1, Collidable3d c2) {
    return c1.getOuterBoundingObject().computeCollision(c2.getOuterBoundingObject());
  }
  
  public static final CollisionInfo3d computeCollision(ComplexCollidable3d cc1, ComplexCollidable3d cc2) {
    if (null == cc1.getOuterBoundingObject().computeCollision(cc2.getOuterBoundingObject())) {
      return null;
    }
    CollisionInfo3d ci;
    for (var bo1 : cc1.getInnerBoundingObjects()) {
      for (var bo2 : cc2.getInnerBoundingObjects()) {
        if (null != (ci = bo1.computeCollision(bo2))) {
          return ci;
        }
      }
    }
    return null;
  }
  
  public static final CollisionInfo3d computeCollision(Collidable3d c, ComplexCollidable3d cc) {
    return computeCollision(cc, c);
  }
  
  public static final CollisionInfo3d computeCollision(ComplexCollidable3d cc, Collidable3d c) {
    var bo1 = c.getOuterBoundingObject();
    if (null == cc.getOuterBoundingObject().computeCollision(bo1)) {
      return null;
    } 
    CollisionInfo3d ci;
    for (var bo2 : cc.getInnerBoundingObjects()) {
      if (null != (ci = bo2.computeCollision(bo1))) {
        return ci;
      }
    }
    return null;
  }
  

  public static CollisionInfo3d computeCollision(BoundingSphere3d a, BoundingSphere3d b) {
    Vector3d posA = a.getPosition();
    Vector3d posB = b.getPosition();
    
    double rA = a.getRadius();
    double rB = b.getRadius();
    
    Vector3d distance = posA.sub(posB);
    
    double distNormSquared = distance.lengthSquared();
    double radiusSum       = rA + rB;
    
    int orientation = a.getOrientation();
    
    if (orientation*distNormSquared < orientation*radiusSum*radiusSum) {
      double distNorm = Math.sqrt(distNormSquared);
      double overlap  = radiusSum - distNorm;
      
      Vector3d collisionNormal = distance.div(distNorm);
      Vector3d collisionPoint = posA.add(collisionNormal.mul(rA)).div(2)
                           .add(posB.add(collisionNormal.mul(rB)).div(2));
      
      return new CollisionInfo3d(collisionPoint, collisionNormal, overlap);
    }
    else {
      return null;
    }
  }
  
  public static CollisionInfo3d computeCollision(BoundingBox3d a, BoundingBox3d b) {
    return null;
  }
  
  public static CollisionInfo3d computeCollision(BoundingSphere3d a, BoundingBox3d b) {
    return computeCollision(b, a);
  }

  public static CollisionInfo3d computeCollision(BoundingBox3d a, BoundingSphere3d b) {
    Vector3d pos = b.getPosition();
    double r = b.getRadius();
    
    if (a.getOrientation() == BoundingObject3d.ORIENTATION_OUTSIDE) { 
      for (Plane3d side : a.getPlanes()) {
        Vector3d normal = side.getNormal();
        Vector3d point = pos.subLocal(normal.mul(r));
        if (a.contains(point)) {
          return new CollisionInfo3d(side.closestPoint(pos), normal, -side.signedDistance(point));
        }
      }
    }
    else {
      for (Plane3d side : a.getPlanes()) {
        double sd = side.signedDistance(pos);
        if (sd < r) { 
          return new CollisionInfo3d(side.closestPoint(pos), side.getNormal(), r - sd);
        }
      }
    }
    
    return null;
  }
}