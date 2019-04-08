package rcs.feyn.three.collision;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.math.linalg.Vector3d.ImmutableVector3d;

public class CollisionInfo3d {
  
  private final ImmutableVector3d point;
  private final ImmutableVector3d normal;
  private final double overlap; 
  
  public CollisionInfo3d(Vector3d point, Vector3d normal, double overlap) {
    this.point   = point.freeze();
    this.normal  = normal.freeze();
    this.overlap = overlap;
  }
  
  public Vector3d getNormal() {
    return new Vector3d(normal);
  }
  
  public Vector3d getPoint() {
    return new Vector3d(point);
  }
  
  public double getOverlap() {
    return overlap;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + point.hashCode();
    result = 31 * result + normal.hashCode(); 
    result = 31 * result + MathUtils.hashDouble(overlap);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CollisionInfo3d)) {
      return false;
    }
    
    CollisionInfo3d that = (CollisionInfo3d) obj;
    
    return this.point.equals(that.point)
        && this.normal.equals(that.normal)
        && MathUtils.epsilonEquals(this.overlap, that.overlap);
  }

  @Override
  public String toString() {
    return String.format("%s:{point=%s, normal=%s, overlap=%s}", 
        super.toString(), point, normal, overlap);
  }
}
