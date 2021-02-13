package rcs.feyn.three.collision;

import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.Vector3d;
import rcs.feyn.math.MathConsts;

public class ElasticCollision3d implements CollisionHandler3d<Collidable3d, Collidable3d> {
  
  public ElasticCollision3d() { }

  @Override
  public void handleCollision(Collidable3d a, Collidable3d b, CollisionInfo3d ci) {
    if (null == ci || a == b) {
      return;
    }     
    
//    CollisionUtils.fixOverlap(a, b, ci);

    Vector3d collisionNormal = ci.getNormal();
    
    Vector3d vAFinal = new Vector3d();
    Vector3d vBFinal = new Vector3d();
    
    assumeSecondIsAtRest(a, b, vAFinal, vBFinal, collisionNormal);
    assumeSecondIsAtRest(b, a, vBFinal, vAFinal, collisionNormal.mul(-1)); 

    a.setVelocity(vAFinal);
    b.setVelocity(vBFinal);
  }
  
  private void assumeSecondIsAtRest(Collidable3d a, Collidable3d b, Vector3d vA, Vector3d vB, Vector3d collisionNormal) {
    double s1 = a.getVelocity().lengthSquared();
    
    if (s1 == 0) {
      return;
    }
    
    s1 = Math.sqrt(s1);
    
    Vector3d v1 = a.getVelocity();
    Vector3d v1Normal = v1.div(s1); 
    
    double theta = MathConsts.PI - v1.angleBetween(collisionNormal);
    double phi   = 90 - theta;
    
    double s1f = s1 * TrigLookUp.sin(theta);
    double s2f = s1 * TrigLookUp.cos(theta) * a.getMass() / b.getMass();
  
    vA.addLocal(v1Normal.rotate(v1Normal.crossProd(collisionNormal), phi).mul(s1f));
    vB.addLocal(collisionNormal.mul(-s2f));
  }

}
