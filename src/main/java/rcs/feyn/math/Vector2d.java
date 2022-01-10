package rcs.feyn.math;

import rcs.feyn.utils.Freezable;
import rcs.feyn.utils.ImmutableModificationException;
import rcs.feyn.utils.XORShift;

public class Vector2d implements Freezable<Vector2d> {

  public static final ImmutableVector2d ZERO = new Vector2d( 0,  0).freeze();
  public static final ImmutableVector2d X_AXIS = new Vector2d( 1,  0).freeze();
  public static final ImmutableVector2d Y_AXIS = new Vector2d( 0,  1).freeze();
  public static final ImmutableVector2d NEG_X_AXIS = new Vector2d(-1,  0).freeze();
  public static final ImmutableVector2d NEG_Y_AXIS = new Vector2d( 0, -1).freeze();

  private double x, y = 0;

  public Vector2d() { }
  
  public Vector2d(double x, double y) {
    set(x, y);
  } 

  public Vector2d(Vector2d that) {
    set(that);
  }

  public Vector3d toVector3d() {
    return new Vector3d(x, y, 0);
  }

  public Vector4d toVector4d() {
    return new Vector4d(x, y, 0, 0);
  }

  public Vector2d set(Vector2d that) {
    return set(that.x, that.y);
  }

  public Vector2d set(double x, double y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }  
  
  public double x(double x) {
    return this.x = x;
  }

  public double y(double y) {
    return this.y = y;
  }

  public Vector2d addLocal(Vector2d that) {
    return addLocal(that.x, that.y);
  }

  public Vector2d addLocal(double x, double y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vector2d add(Vector2d that) {
    return add(that.x, that.y);
  }

  public Vector2d add(double x, double y) {
    return new Vector2d(this.x + x,
                        this.y + y);
  }

  public Vector2d subLocal(Vector2d that) {
    return subLocal(that.x, that.y);
  }

  public Vector2d subLocal(double x, double y) {
    this.x -= x;
    this.y -= y;
    return this;
  }

  public Vector2d sub(Vector2d that) {
    return sub(that.x, that.y);
  }

  public Vector2d sub(double x, double y) {
    return new Vector2d(this.x - x,
                        this.y - y);
  }

  public Vector2d pointWiseMulLocal(double x, double y) {
    this.x *= x;
    this.y *= y;
    return this;
  }

  public Vector2d pointWiseMulLocal(Vector2d that) {
    return pointWiseMulLocal(that.x, that.y);
  }

  public Vector2d pointWiseMul(Vector2d that) {
    return pointWiseMul(that.x, that.y);
  }

  public Vector2d pointWiseMul(double x, double y) {
    return new Vector2d(this.x * x,
                        this.y * y);
  }

  public Vector2d pointWiseDivLocal(double x, double y) {
    this.x /= x;
    this.y /= y;
    return this;
  }

  public Vector2d pointWiseDivLocal(Vector2d that) {
    return pointWiseDivLocal(that.x, that.y);
  }

  public Vector2d pointWiseDiv(Vector2d that) {
    return pointWiseDiv(that.x, that.y);
  }

  public Vector2d pointWiseDiv(double x, double y) {
    return new Vector2d(this.x / x,
                        this.y / y);
  }

  public Vector2d mulLocal(double scalar) {
    x *= scalar;
    y *= scalar;
    return this;
  }

  public Vector2d mul(double scalar) {
    return new Vector2d(x * scalar,
                        y * scalar);
  }

  public Vector2d divLocal(double scalar) {
    x /= scalar;
    y /= scalar;
    return this;
  }

  public Vector2d div(double scalar) {
    return new Vector2d(x / scalar,
                        y / scalar);
  } 
  
//  public Vector2d mulLocal(Matrix22 transform) {
//    return set(
//      x * transform.m00 + y * transform.m10,
//      x * transform.m01 + y * transform.m11 
//    );
//  }
//  
//  public Vector2d mul(Matrix22 transform) {
//    return new Vector2d(this).mulLocal(transform);
//  } 

  public Vector2d affineTransformLocal(Matrix33 transform) {
    return set(
      x * transform.m00 + y * transform.m01 + 1 * transform.m02, 
      x * transform.m10 + y * transform.m11 + 1 * transform.m12); 
  }

  public Vector2d affineTransform(Matrix33 transform) {
    return new Vector2d(this).affineTransformLocal(transform);
  }

  public Vector2d affineTransformAsVectorLocal(Matrix33 transform) {
    return set(
      x * transform.m00 + y * transform.m01, 
      x * transform.m10 + y * transform.m11);
  }

  public Vector2d affineTransformAsVector(Matrix33 transform) {
    return new Vector2d(this).affineTransformLocal(transform);
  }

  public double dotProd(Vector2d that) {
    return x * that.x
         + y * that.y;
  }  

  public double area(Vector2d that) {
    return this.x*that.y - this.y*that.x;
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return this.dotProd((Vector2d) this);
  }

  public double distance(Vector2d that) {
    return Math.sqrt(this.distanceSquared(that));
  }

  public double distanceSquared(Vector2d that) {
    return this.sub(that).lengthSquared();
  }

  public Vector2d normalize() {
    return this.div(this.length());
  }

  public Vector2d normalizeLocal() {
    if (equals(ZERO)) {
      throw new IllegalArgumentException("Cannot normalize a vector of length 0.");
    }
    
    double norm = length();

    x /= norm;
    y /= norm;

    return this;
  } 

  public Vector2d proj(Vector2d that) {
    return that.mul(this.dotProd((Vector2d) that) / that.lengthSquared());
  }

  public Vector2d perp(Vector2d that) {
    return this.sub(this.proj(that));
  }

  public Vector2d midPoint(Vector2d that) {
    return this.add(that).divLocal(2);
  }

  public double angleBetween(Vector2d that) {
    return Math.acos(this.dotProd(that) / (this.length() * that.length()));
  }

  public Vector2d rotateLocal(Vector2d origin, double radians) {
    return affineTransformLocal(Matrix33.createRotateMatrix(origin, radians));
  }

  public Vector2d rotate(Vector2d origin, double radians) {
    return new Vector2d(this).rotateLocal(origin, radians); 
  }

  public Vector2d rotateLocal(double radians) {
    return affineTransformLocal(Matrix33.createRotateMatrix(radians));
  }

  public Vector2d rotate(double deg) {
    return new Vector2d(this).rotateLocal(Vector2d.ZERO, deg); 
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(x);
    result = 31 * result + MathUtils.hashDouble(y);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Vector2d)) {
      return false;
    }
    
    var that = (Vector2d) obj;
    
    return MathUtils.epsilonEquals(this.x, that.x)
        && MathUtils.epsilonEquals(this.y, that.y);
  }

  @Override
  public String toString() {
    return String.format("[%.2f, %.2f]", x, y);
  } 

  @Override
  public final ImmutableVector2d freeze() {
    return new ImmutableVector2d(this);
  }

  public static final class ImmutableVector2d extends Vector2d {   
    public ImmutableVector2d(Vector2d that) {
      super.set(that.x, that.y);
    } 

    @Override
    public Vector2d set(double x, double y) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d addLocal(double x, double y) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d addLocal(Vector2d that) {
      throw new ImmutableModificationException(this);
    }  

    @Override
    public Vector2d subLocal(double x, double y) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d subLocal(Vector2d that) {
      throw new ImmutableModificationException(this);
    } 
    
    @Override
    public Vector2d pointWiseMulLocal(double x, double y) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector2d pointWiseMulLocal(Vector2d that) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector2d pointWiseDivLocal(double x, double y) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector2d pointWiseDivLocal(Vector2d that) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d mulLocal(double scalar) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector2d divLocal(double scalar) {
      throw new ImmutableModificationException(this);
    }  

    @Override
    public Vector2d normalizeLocal() {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d rotateLocal(Vector2d origin, double deg) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d rotateLocal(double deg) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector2d affineTransformLocal(Matrix33 transform) {
      throw new ImmutableModificationException(this);
    } 
  }

  public static Vector2d fromSpherical(double r, double theta) {
    return new Vector2d(r * Math.cos(theta),
                        r * Math.sin(theta));
  }

  public static Vector2d[] fromArrays(double[] x, double[] y) {
    int size = x.length;
    Vector2d[] vecs = new Vector2d[size];
    for (int i = 0; i < size; i++) {
      vecs[i] = new Vector2d(x[i], y[i]);
    }
    return vecs;
  }

  public static boolean areColinear(Vector2d... v) {
    for (int i = 0; i < v.length-1; i++) {
      if (!MathUtils.epsilonZero(v[i].area(v[i+1]))) {
        return false;
      }
    }
    return false;
  }

  public static final Vector2d randomUnitVector() {
    return randomUnitVector(MathConsts.TWO_PI);
  }

  public static final Vector2d randomUnitVector(double thetaRange) {
    return fromSpherical(1, XORShift.getInstance().randomDouble(thetaRange));
  }
}