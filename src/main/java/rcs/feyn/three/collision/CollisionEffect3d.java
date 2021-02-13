package rcs.feyn.three.collision;

import rcs.feyn.math.Vector3d;
import rcs.feyn.math.Vector3d.ImmutableVector3d;

public class CollisionEffect3d {
  
  protected final ImmutableVector3d deltaPosition;
  protected final ImmutableVector3d deltaVelocity;
  
  public CollisionEffect3d(Vector3d deltaPosition, Vector3d deltaVelocity) {
    this.deltaPosition = deltaPosition.freeze();
    this.deltaVelocity = deltaVelocity.freeze();
  }
  
  public Vector3d getChangeInPosition() {
    return deltaPosition;
  }
  
  public Vector3d getChangeInVelocity() {
    return deltaVelocity;
  }
  
  public void applyTo(Collidable3d obj) {
    obj.translate(deltaPosition);
    obj.setVelocity(obj.getVelocity().addLocal(deltaVelocity));
  }
}
