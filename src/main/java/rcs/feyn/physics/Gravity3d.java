package rcs.feyn.physics;

import rcs.feyn.math.Vector3d;

public class Gravity3d {
  
  private Vector3d position = new Vector3d();
  
  private double K;

  public Gravity3d(Vector3d position, double mass) {
    this(position, mass, PhysicsConsts.G);
  }

  public Gravity3d(Vector3d position, double mass, double constant) {
    this.position.set(position);
    this.K = constant * mass;
  }

  public Vector3d getPosition() {
    return new Vector3d(position);
  }

  public void setPosition(Vector3d position) {
    this.position.set(position);
  }
  
  public void trackPosition(Vector3d position) {
    this.position = position;
  }

  public Vector3d getAccelerationAt(Vector3d point) {
    Vector3d r = position.sub(point);
    Vector3d rh = r.normalize();
    double r2 = r.lengthSquared();
    return rh.mul(K/r2);
  }

  public Vector3d getForceOn(Vector3d position, double mass) {
    return getAccelerationAt(position).mul(mass);
  } 
}
