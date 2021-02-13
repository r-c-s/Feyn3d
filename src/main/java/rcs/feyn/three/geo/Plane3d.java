package rcs.feyn.three.geo;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Plane3d implements Movable3d, Transformable3d {
  
  private Vector3d origin;
  private Vector3d normal;
  private double d; 

  public static Plane3d xyPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.Z_AXIS);
  }

  public static Plane3d yzPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.X_AXIS);
  }

  public static Plane3d zxPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.Y_AXIS);
  }

  public static Plane3d yxPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.NEG_Z_AXIS);
  }

  public static Plane3d zyPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.NEG_X_AXIS);
  }

  public static Plane3d xzPlane() {
    return new Plane3d(Vector3d.ZERO, Vector3d.NEG_Y_AXIS);
  }

  public Plane3d(Plane3d p3d) {
    this(p3d.origin, p3d.normal);
  }

  public Plane3d(Vector3d origin, Vector3d normal) {
    this.origin = new Vector3d(origin);
    this.normal = new Vector3d(normal).normalizeLocal();
    this.d = -normal.dotProd(origin);
  } 

  public Plane3d(Vector3d p, Vector3d q, Vector3d r) {
    this(p, GeoUtils3d.getNormal(p, q, r));
  }

  public Vector3d getOrigin() {
    return new Vector3d(origin);
  }

  public void setOrigin(Vector3d origin) {
    this.origin.set(origin);
    this.d = -normal.dotProd(origin);
  }

  public Vector3d getNormal() {
    return new Vector3d(normal);
  }

  public void setNormal(Vector3d normal) {
    this.normal.set(normal).normalizeLocal();
    this.d = -normal.dotProd(origin);
  }

  @Override
  public void translate(Vector3d v3d) {
    origin.addLocal(v3d);
    d = -normal.dotProd(origin);
  } 

  @Override
  public void transform(Matrix44 transform) {
    origin.affineTransformLocal(transform);
    normal.affineTransformLocal(transform.extractRotation());
    d = -normal.dotProd(origin);
  }

  public void reverseOrientation() {
    normal.mulLocal(-1);
    d = -d;
  }

  public double distance(Vector3d q) {
    return Math.abs(signedDistance(q));
  }

  public double signedDistance(Vector3d q) {
    return normal.dotProd(q) + d;
  }

  public Vector3d closestPoint(Vector3d q) {
    return q.sub(normal.mul(signedDistance(q)));
  }

  public Vector3d intersection(final Vector3d p, final Vector3d q) {
    double P = signedDistance(p);
    double Q = signedDistance(q);
    if ((P > 0 && Q > 0) || (P < 0 && Q < 0)) {
      return null;
    }
    else {
      return p.add(q.sub(p).mul(P/(P-Q)));
    }
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + origin.hashCode();
    result = 31 * result + normal.hashCode();
    result = 31 * result + MathUtils.hashDouble(d);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Plane3d)) {
      return false;
    }
    
    var that = (Plane3d) obj;

    return this.origin.equals(that.origin) &&
           this.normal.equals(that.normal);
  }

  @Override
  public String toString() {
    return String.format("%s:[p=%s,n=%s]", this.getClass().getName(), origin, normal);
  }
}
