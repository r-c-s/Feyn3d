package rcs.feyn.three.collision;

import rcs.feyn.math.Vector3d;
import rcs.feyn.math.Vector3d.ImmutableVector3d;

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
  public String toString() {
    return String.format("%s:{point=%s, normal=%s, overlap=%s}", 
        super.toString(), point, normal, overlap);
  }
}
