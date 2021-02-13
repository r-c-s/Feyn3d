package rcs.feyn.three.geo;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Ray3d implements Movable3d, Transformable3d {

  private final Vector3d origin;
  private final Vector3d normal;

  public static Ray3d xAxis() {
    return new Ray3d(Vector3d.ZERO, Vector3d.X_AXIS);
  }  

  public static Ray3d yAxis() {
    return new Ray3d(Vector3d.ZERO, Vector3d.Y_AXIS);
  }  

  public static Ray3d zAxis() {
    return new Ray3d(Vector3d.ZERO, Vector3d.Z_AXIS);
  }

  public Ray3d(Vector3d point, Vector3d normal) {
    this.origin = new Vector3d(point);
    this.normal = new Vector3d(normal).normalizeLocal();
  }

  public Vector3d getOrigin() {
    return new Vector3d(origin);
  }

  public void setOrigin(Vector3d origin) {
    this.origin.set(origin);
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
  public void transform(Matrix44 transform) {
    origin.affineTransformLocal(transform);
    normal.affineTransformLocal(transform.extractRotation());
  }

  public Vector3d closestPoint(Vector3d q) {
    Vector3d w = q.sub(origin);
    double dot = w.dotProd(normal);
    if (dot <= 0) {
      return origin;
    }
    else {
      return origin.add(normal.mul(dot));
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
    result = 31 * result + origin.hashCode();
    result = 31 * result + normal.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Ray3d)) {
      return false;
    }
    
    var that = (Ray3d) obj;
    
    return this.origin.equals(that.origin) && 
           this.normal.equals(that.normal);
  }

  @Override
  public String toString() {
    return String.format("%s:[p=%s,n=%s]", this.getClass().getName(), origin, normal);
  }
}
