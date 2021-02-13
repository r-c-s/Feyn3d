package rcs.feyn.physics;

import rcs.feyn.math.Vector3d;

public class Gravity1d {

  private final Vector3d accel = new Vector3d();

  public Gravity1d(double g, Vector3d up) {
    set(g, up);
  }

  public void set(double g, Vector3d up) {
    accel.set(up.normalize().mul(-Math.abs(g)));
  }

  public Vector3d getAcceleration() {
    return new Vector3d(accel);
  }

  public Vector3d getForce(double mass) {
    return getAcceleration().mul(mass);
  }

  public Vector3d applyTo(Vector3d velocity) {
    return velocity.addLocal(accel);
  }
}
