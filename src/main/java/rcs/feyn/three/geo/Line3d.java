package rcs.feyn.three.geo;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Line3d implements Movable3d, Transformable3d {

  private final Vector3d origin;
  private final Vector3d normal;
  
  public static Line3d xAxis() {
    return new Line3d(Vector3d.ZERO, Vector3d.X_AXIS);
  }  

  public static Line3d yAxis() {
    return new Line3d(Vector3d.ZERO, Vector3d.Y_AXIS);
  }  

  public static Line3d zAxis() {
    return new Line3d(Vector3d.ZERO, Vector3d.Z_AXIS);
  }

  public Line3d(Vector3d origin, Vector3d normal) {
    this.origin = new Vector3d(origin);
    this.normal = new Vector3d(normal).normalizeLocal();
  }

  public Vector3d getOrigin() {
    return new Vector3d(origin);
  }

  public void setOrigin(Vector3d point) {
    this.origin.set(point);
  }
  
  public Vector3d getNormal() {
    return new Vector3d(normal);
  }

  public void setNormal(Vector3d normal) {
    this.normal.set(normal).normalizeLocal();
  }
  
  @Override
  public void translate(Vector3d v3d) {
    origin.addLocal(v3d);
  }

  @Override
  public void transform(Matrix44 m44) {
    origin.affineTransformLocal(m44);
    normal.affineTransformLocal(m44.extractRotation());
  }

  public Vector3d closestPoint(Vector3d q) {
    return origin.add(normal.mul(q.sub(origin).dotProd(normal)));
  }

  public Vector3d[] closestPoints(Line3d that) {
    Vector3d w = this.origin.sub(that.origin);
    double A = this.normal.dotProd(that.normal);
    double B = this.normal.dotProd(w);
    double C = that.normal.dotProd(w);
    double D = 1 - A*A;
    
    if (MathUtils.epsilonEquals(D, 0)) {
      return new Vector3d[]{ 
        this.origin, 
        that.origin.add(that.normal.mul(C)) 
      };
    }
    else {
      return new Vector3d[] {
        this.origin.add(this.normal.mul((A*C - B)/D)),
        that.origin.add(that.normal.mul((C - A*B)/D))
      };
    }
  }

  public double distance(Vector3d q) {
    return Math.sqrt(distanceSquared(q));
  }

  public double distanceSquared(Vector3d q) {
    Vector3d w = q.sub(origin);
    return w.dotProd(w) - MathUtils.squared(w.dotProd(normal));
  }

  public double distance(Line3d l3d) {
    return Math.sqrt(distanceSquared(l3d));
  }

  public double distanceSquared(Line3d that) {
    Vector3d points[] = closestPoints(that);
    return points[0].distanceSquared(points[1]);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + origin.hashCode();
    result = 31 * result + normal.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Line3d)) {
      return false;
    }
    
    var that = (Line3d) obj;

    return this.origin.equals(that.origin) && 
           this.normal.equals(that.normal);
  }

  @Override
  public String toString() {
    return String.format("%s:[p=%s,n=%s]", this.getClass().getName(), origin, normal);
  } 
}
