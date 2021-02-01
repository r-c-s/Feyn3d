package rcs.feyn.three;

import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class Particle3d implements IParticle3d {
  
  protected final Vector3d position;
  protected final Vector3d velocity;
  
  protected final ReferenceFrame3d rf = new ReferenceFrame3d();
  protected final Vector3d sd = rf.sd;
  protected final Vector3d up = rf.up;
  protected final Vector3d fw = rf.fw;
  
  protected double mass;

  public Particle3d() {
    this(Vector3d.ZERO, Vector3d.ZERO, 1);
  }

  public Particle3d(Vector3d pos) {
    this(pos, Vector3d.ZERO, 1);
  } 

  public Particle3d(Vector3d pos, Vector3d vel) {
    this(pos, vel, 1);
  } 

  public Particle3d(Vector3d pos, Vector3d vel, double mass) {
    this.position = new Vector3d(pos);
    this.velocity = new Vector3d(vel);
    setMass(mass);
  }

  public final double getPosX() {
    return position.x();
  }

  public final double getPosY() {
    return position.y();
  }

  public final double getPosZ() {
    return position.z();
  }

  public final Vector3d getPosition() {
    return new Vector3d(position);
  }

  public final void setPosX(double x) {
    setPosition(x, position.y(), position.z());
  }

  public final void setPosY(double y) {
    setPosition(position.x(), y, position.z());
  }

  public final void setPosZ(double z) {
    setPosition(position.x(), position.y(), z);
  }

  public final void setPosition(Vector3d v) {
    setPosition(v.x(), v.y(), v.z());
  }

  public final void setPosition(double x, double y, double z) {
    translate(x-position.x(), y-position.y(), z-position.z());
  }

  public final double getVelX() {
    return velocity.x();
  }

  public final double getVelY() {
    return velocity.y();
  }

  public final double getVelZ() {
    return velocity.z();
  }

  public final Vector3d getVelocity() {
    return new Vector3d(velocity);
  }

  public final void setVelX(double x) {
    setVelocity(x, velocity.y(), velocity.z());
  }

  public final void setVelY(double y) {
    setVelocity(velocity.x(), y, velocity.z());
  }

  public final void setVelZ(double z) {
    setVelocity(velocity.x(), velocity.y(), z);
  }

  public final void setVelocity(Vector3d v) {
    setVelocity(v.x(), v.y(), v.z());
  }

  public final void setVelocity(double x, double y, double z) {
    accelerate(x-velocity.x(), y-velocity.y(), z-velocity.z());
  }

  public final Vector3d getMomentum() {
    return velocity.mul(mass);
  }

  public final double getSpeed() {
    return velocity.length();
  }

  public final double getSpeedSquared() {
    return velocity.lengthSquared();
  }

  public final ReferenceFrame3d getReferenceFrame() {
    return rf;
  }

  public final void setReferenceFrame(ReferenceFrame3d rf) {
    this.rf.set(rf);
  }

  public void setOrientation(Particle3d that) {
    this.setReferenceFrame(that.rf);
  }

  public final Vector3d getSideVector() {
    return new Vector3d(sd);
  }

  public final Vector3d getUpVector() {
    return new Vector3d(up);
  }

  public final Vector3d getForwardVector() {
    return new Vector3d(fw);
  }

  public final Vector3d getUnitVectorTo(Vector3d point) {
    return point.sub(position).normalizeLocal();
  }

  public final Vector3d getUnitVectorFrom(Vector3d point) {
    return position.sub(point).normalizeLocal();
  } 

  public final double getMass() {
    return mass;
  }

  public void setMass(double mass) {
    if (mass <= 0) {
      throw new IllegalArgumentException("Error: mass must be greater than 0.");
    }
    this.mass = mass;
  } 

  public final void move() {
    translate(velocity);
  }

  public final void accelerate(double x, double y, double z) {
    accelerate(new Vector3d(x, y, z));
  }

  public final void accelerate(Vector3d accel) {
    velocity.addLocal(accel);
  } 

  public final void translate(double x, double y, double z) {
    translate(new Vector3d(x, y, z));
  }

  @Override
  public void translate(Vector3d delta) { 
    position.addLocal(delta);
  }

  @Override
  public void transform(Matrix44 m4x4) {
    position.affineTransformLocal(m4x4);
    //velocity.affineTransformAsVectorLocal(m4x4);
    rf.transform(m4x4);
  }

  public final void scale(double scale) {
    scale(position, scale);
  }

  public final void scale(Vector3d point, double scale) {
    transform(Matrix44.createScaleMatrix(scale, point));
  }

  public final void rotate(Vector3d axis, double deg) {
    rotate(position, axis, deg);
  }

  public final void rotate(Vector3d point, Vector3d axis, double deg) {
    transform(Matrix44.createRotateMatrix(point, axis, deg));
  }

  public final void accelerateForward(double accel) {
    velocity.addLocal(fw.mul(accel));
  }

  public final void accelerateBackward(double accel) {
    velocity.addLocal(fw.mul(-accel));
  } 

  public final void roll(double deg) {
    rotate(fw, deg);
  }

  public final void pitch(double deg) {
    rotate(sd, deg);
  }

  public final void yaw(double deg) {
    rotate(up, deg);
  }

  @Override
  public String toString() {
    return String.format("%s:[r=%s,v=%s,rf=%s]", this.getClass().getName(), position, velocity, rf);
  } 
}
