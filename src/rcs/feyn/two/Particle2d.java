package rcs.feyn.two;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.linalg.Matrix33;
import rcs.feyn.math.linalg.Vector2d;
import rcs.feyn.three.Particle3d;

public class Particle2d implements IParticle2d {
  
  protected final Vector2d position;
  protected final Vector2d velocity;
  
  protected final ReferenceFrame2d rf = new ReferenceFrame2d();
  protected final Vector2d sd = rf.sd;
  protected final Vector2d up = rf.up; 
  
  protected double mass;

  public Particle2d() {
    this(Vector2d.ZERO, Vector2d.ZERO, 1);
  }

  public Particle2d(Vector2d pos) {
    this(pos, Vector2d.ZERO, 1);
  } 

  public Particle2d(Vector2d pos, Vector2d vel) {
    this(pos, vel, 1);
  } 

  public Particle2d(Vector2d pos, Vector2d vel, double mass) {
    this.position = new Vector2d(pos);
    this.velocity = new Vector2d(vel);
    setMass(mass);
  } 

  public Particle3d toParticle3d() {
    return new Particle3d(position.toVector3d(), velocity.toVector3d(), mass);
  }

  public final double getPosX() {
    return position.x();
  }

  public final double getPosY() {
    return position.y();
  } 

  public final Vector2d getPosition() {
    return new Vector2d(position);
  }

  public final void setPosX(double x) {
    setPosition(x, position.y());
  }

  public final void setPosY(double y) {
    setPosition(position.x(), y);
  } 

  public final void setPosition(Vector2d v) {
    setPosition(v.x(), v.y());
  }

  public final void setPosition(double x, double y) {
    translate(x-position.x(), y-position.y());
  }

  public final double getVelX() {
    return velocity.x();
  }

  public final double getVelY() {
    return velocity.y();
  } 

  public final Vector2d getVelocity() {
    return new Vector2d(velocity);
  }

  public final void setVelX(double x) {
    setVelocity(x, velocity.y());
  }

  public final void setVelY(double y) {
    setVelocity(velocity.x(), y);
  } 

  public final void setVelocity(Vector2d v) {
    setVelocity(v.x(), v.y());
  }

  public final void setVelocity(double x, double y) {
    accelerate(x-velocity.x(), y-velocity.y());
  } 

  public final double getSpeed() {
    return velocity.norm();
  }

  public final double getSpeedSquared() {
    return velocity.normSquared();
  }

  public final ReferenceFrame2d getReferenceFrame() {
    return rf;
  }

  public final void setReferenceFrame(ReferenceFrame2d rf) {
    this.rf.set(rf);
  }

  public void setOrientation(Particle2d that) {
    this.setReferenceFrame(that.rf);
  }

  public final Vector2d getSideVector() {
    return new Vector2d(sd);
  }

  public final Vector2d getUpVector() {
    return new Vector2d(up);
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

  public final void accelerate(double x, double y) {
    accelerate(new Vector2d(x, y));
  }

  public void accelerate(Vector2d accel) {
    velocity.addLocal(accel);
  } 

  public final void translate(double x, double y) {
    translate(new Vector2d(x, y));
  }

  public void translate(Vector2d delta) {
    position.addLocal(delta);
  }

  @Override
  public void transform(Matrix33 m3x3) {
    position.affineTransformLocal(m3x3);
    velocity.affineTransformLocal(m3x3.extractRotation());
    rf.transform(m3x3);
  }

  public void scale(Vector2d point, double scale) {
    transform(Matrix33.createScaleMatrix(scale, point));
  } 

  public final void rotate(double deg) {
    rotate(position, deg);
  }

  public final void rotate(Vector2d point, double deg) {
    transform(Matrix33.createRotateMatrix(point, deg));
  } 

  public final Vector2d getUnitVectorTo(Vector2d point) {
    return point.sub(position).normalizeLocal();
  }

  public final Vector2d getUnitVectorFrom(Vector2d point) {
    return position.sub(point).normalizeLocal();
  }  

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + position.hashCode();
    result = 31 * result + velocity.hashCode();
    result = 31 * result + rf.hashCode();
    result = 31 * result + MathUtils.hashDouble(mass);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Particle2d)) {
      return false;
    }
    
    Particle2d that = (Particle2d) obj;

    return this.position.equals(that.position)
        && this.velocity.equals(that.velocity)
        && this.rf.equals(that.rf)
        && this.mass == that.mass;
  }

  @Override
  public String toString() {
    return String.format("%s:[r=%s,v=%s,rf=%s]", this.getClass().getName(), position, velocity, rf);
  }
}