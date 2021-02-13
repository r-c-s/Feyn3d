package rcs.feyn.three.collision;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;

public class InelasticCollision3d extends ElasticCollision3d {
  
  private final double coeff;
  
  public InelasticCollision3d(double coeff) {
    if (!MathUtils.isInRangeInclusive(coeff, 0, 1)) {
      throw new IllegalArgumentException("Error: coefficient of energy loss must be between 0 and 1.");
    }
    this.coeff = coeff;
  }

  @Override
  public void handleCollision(Collidable3d a, Collidable3d b, CollisionInfo3d ci) {
    Vector3d av = a.getVelocity();
    Vector3d bv = b.getVelocity();
    
    super.handleCollision(a, b, ci);
    
    if (null == ci) {
      return;
    }
    
    Vector3d normal = ci.getNormal();

    double dotA = normal.dotProd(av);
    double dotB = normal.dotProd(bv);
    
    if (dotA > 0) {
      a.setVelocity(av.addLocal(normal.mul(dotA).mul(-coeff)));
    } 
    if (dotB > 0) {
      b.setVelocity(bv.addLocal(normal.mul(dotB).mul(-coeff)));
    } 
  }
}
