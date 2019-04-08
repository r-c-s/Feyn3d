package rcs.feyn.two.geo;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.geo.Spherical;
import rcs.feyn.math.linalg.Matrix33;
import rcs.feyn.math.linalg.Vector2d;


public class Circle2d implements Spherical, Movable2d, Transformable2d {

  protected Vector2d point;
  protected double radius;
  
  public Circle2d(Vector2d position, double radius) {
    this.point = new Vector2d(position);
    this.radius = radius;
  }

  public Vector2d getOrigin() {
    return new Vector2d(point);
  }

  public void setOrigin(Vector2d position) {
    this.point.set(position);
  }

  @Override
  public double getRadius() {
    return radius;
  }

  @Override
  public void setRadius(double radius) {
    if (radius <= 0) {
      throw new IllegalArgumentException("Error: radius must be greater than 0.");
    }
    this.radius = radius;
  } 

  public void translate(Vector2d v3d) {
    this.point.addLocal(v3d);
  } 

  public void transform(Matrix33 transform) {
    point.affineTransform(transform);
    radius *= transform.extractScaleX();
  }

  public boolean contains(Vector2d p) {
    return point.distanceSquared(p) <= MathUtils.squared(radius);
  }

  public boolean collidedWith(Circle2d that) {
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
    if (!(obj instanceof Circle2d)) {
      return false;
    }
    
    Circle2d that = (Circle2d) obj;
    
    return this.point.equals(that.point) &&
           this.radius == that.radius;
  }

  @Override
  public String toString() {
    return String.format("%s:[pos=%s,radius=%s]", this.getClass().getName(), point, radius);
  }
} 