package rcs.feyn.three.geo;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Sphere3d implements Movable3d, Transformable3d {

  protected Vector3d point;
  protected double   radius;

  public Sphere3d(Vector3d point, double radius) {
    this.point = new Vector3d(point);
    this.radius = radius;
  }

  public Vector3d getOrigin() {
    return new Vector3d(point);
  }

  public void setOrigin(Vector3d position) {
    this.point.set(position);
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    if (radius <= 0) {
      throw new IllegalArgumentException("Error: radius must be greater than 0.");
    }
    this.radius = radius;
  } 

  @Override
  public void translate(Vector3d v3d) {
    this.point.addLocal(v3d);
  } 

  @Override
  public void transform(Matrix44 transform) {
    point.affineTransformLocal(transform);
    radius *= transform.extractScaleX();
  }

  public boolean contains(Vector3d p) {
    return point.distanceSquared(p) <= MathUtils.squared(radius);
  }

  public boolean collidedWith(Sphere3d that) {
    return point.distanceSquared(that.point) <= MathUtils.squared(this.radius + that.radius);
  } 

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + point.hashCode();
    result = 31 * result + MathUtils.hashDouble(radius);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Sphere3d)) {
      return false;  
    }  
    
    var that = (Sphere3d) obj;

    return this.point.equals(that.point) &&
           this.radius == that.radius;
  }

  @Override 
  public String toString() {
    return String.format("%s:[pos=%s,radius=%s]", this.getClass().getName(), point, radius);
  }
} 