package rcs.feyn.three.geo;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Segment3d implements Movable3d, Transformable3d {
  
  private Vector3d a;
  private Vector3d b;

  public Segment3d(Vector3d a, Vector3d b) {
    this.a = new Vector3d(a);
    this.b = new Vector3d(b);
  }

  public Vector3d getA() {
    return new Vector3d(a);
  }

  public void setA(Vector3d a) {
    this.a.set(a);
  }

  public Vector3d getB() {
    return new Vector3d(b);
  }

  public void setB(Vector3d b) {
    this.b.set(b);
  }

  public Vector3d midPoint() {
    return a.midPoint(b);
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return a.distanceSquared(b);
  }

  public Vector3d getOrigin() {
    return midPoint();
  }

  public void setOrigin(Vector3d position) {
    translate(position.sub(midPoint()));
  }

  @Override
  public void translate(Vector3d v3d) {
    a.addLocal(v3d);
    b.addLocal(v3d);
  } 
  
  @Override
  public void transform(Matrix44 transform) {
    a.affineTransformLocal(transform);
    b.affineTransformLocal(transform);
  }

  public Vector3d closestPoint(Vector3d q) {
    Vector3d w = q.sub(a);
    Vector3d diff = b.sub(a);
    double dot = w.dotProd(diff);
    if (dot <= 0) {
      return a;
    }
    else {
      double dotMax = diff.dotProd(diff);
      if (dot >= dotMax) {
        return b;
      }
      else {
        return a.add(diff.mul(dot/dotMax));
      }
    }
  }

  public double distance(Vector3d q) {
    return Math.sqrt(distanceSquared(q));
  }

  public double distanceSquared(Vector3d q) {
    return q.sub(closestPoint(q)).lengthSquared();
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + a.hashCode();
    result = 31 * result + b.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Segment3d)) {
      return false;
    }  
    
    var that = (Segment3d) obj;
    
    return this.a.equals(that.a) && 
           this.b.equals(that.b);
  }

  @Override
  public String toString() {
    return String.format("%s:[a=%s,b=%s]", this.getClass().getName(), a, b);
  }
}
