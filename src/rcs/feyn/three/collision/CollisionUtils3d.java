package rcs.feyn.three.collision;

import rcs.feyn.math.linalg.Vector3d;

public class CollisionUtils3d { 
  
  public static final double OVERLAP_CORRECTION = 0.51;
  
  public static final void fixOverlap(Collidable3d a, Collidable3d b, CollisionInfo3d ci) {
    Vector3d collisionNormal = ci.getNormal();  
    double   overlap         = ci.getOverlap();
    
    a.translate(collisionNormal.mul( OVERLAP_CORRECTION * overlap));
    b.translate(collisionNormal.mul(-OVERLAP_CORRECTION * overlap)); 
  }
}


//double sA = a.getSpeed();
//double sB = b.getSpeed(); 
//
//Vector3d collisionNormal = ci.getCollisionNormal(); 
//  
//if (sA == 0 && sB == 0) {
//a.translate(collisionNormal.mul( 0.501 * ci.getOverlap()));
//b.translate(collisionNormal.mul(-0.501 * ci.getOverlap())); 
//} 
//if (sA > 0) {
//a.translate(a.getVelocity().normalize().mul(-(sA / (sA + sB)) * ci.getOverlap())); 
//}
//if (sB > 0) {
//b.translate(b.getVelocity().normalize().mul(-(sB / (sA + sB)) * ci.getOverlap()));
//} 

//if (null != a.getOuterBoundingObject().computeCollision(b.getOuterBoundingObject())) {
//  System.exit(0);
//}

//double sA = a.getSpeed();
//double sB = b.getSpeed(); 
//
//if (sA != 0 && sB != 0) {
//CollidableModel3d A = sA > sB ? a : b;
//CollidableModel3d B = sA > sB ? b : a;
//
//if (A != a) {
//  collisionNormal.mulLocal(-1);
//}
//
//double d  = A.getPosition().distance(B.getPosition());
//double t  = A.getVelocity().angleBetween(collisionNormal);
//double r1 = ((BoundingSphere3d)A.getOuterBoundingObject()).getRadius();
//double r2 = ((BoundingSphere3d)B.getOuterBoundingObject()).getRadius(); 
//
//
//
//int sign = 1;
//if (t > MathConsts.HALF_PI) {
//  sign = -1;
//  t = MathConsts.PI - t;
//} 
//else {
//  return;
//}
//
//double c = Math.sqrt(MathUtils.squared(r1 + r2) 
//                   - MathUtils.squared(d * Math.sin(t))) 
//                   + sign * d * Math.cos(t); 
//
//A.translate(A.getVelocity().normalize().mul(-c)); 
//} 
//
//collisionNormal = a.getPosition().sub(b.getPosition()).normalize();