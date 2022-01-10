package rcs.feyn.math;

import rcs.feyn.utils.Freezable;
import rcs.feyn.utils.ImmutableModificationException;
import rcs.feyn.utils.XORShift;

public class Vector3d implements Freezable<Vector3d> {  
  
  public static final ImmutableVector3d ZERO = new Vector3d( 0,  0,  0).freeze();
  public static final ImmutableVector3d X_AXIS = new Vector3d( 1,  0,  0).freeze();
  public static final ImmutableVector3d Y_AXIS = new Vector3d( 0,  1,  0).freeze();
  public static final ImmutableVector3d Z_AXIS = new Vector3d( 0,  0,  1).freeze();
  public static final ImmutableVector3d NEG_X_AXIS = new Vector3d(-1,  0,  0).freeze();
  public static final ImmutableVector3d NEG_Y_AXIS = new Vector3d( 0, -1,  0).freeze();
  public static final ImmutableVector3d NEG_Z_AXIS = new Vector3d( 0,  0, -1).freeze();

  private double x, y, z;

  public Vector3d() { }

  public Vector3d(double x, double y, double z) {
    set(x, y, z);
  }

  public Vector3d(Vector3d that) {
    set(that.x, that.y, that.z);
  }  

  public Vector2d toVector2d() {
    return new Vector2d(x, y);
  }

  public Vector4d toVector4d() {
    return new Vector4d(x, y, z, 0);
  }
  
  public Vector3d set(Vector3d that) {
    return set(that.x, that.y, that.z);
  }

  public Vector3d set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  } 

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public double z() {
    return z;
  } 

  public Vector3d x(double x) {
    this.x = x;
    return this;
  }

  public Vector3d y(double y) {
    this.y = y;
    return this;
  }

  public Vector3d z(double z) {
    this.z = z;
    return this;
  } 

  public Vector3d addLocal(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  public Vector3d addLocal(Vector3d that) {
    return addLocal(that.x, that.y, that.z);
  }
  
  public Vector3d add(Vector3d that) {
    return add(that.x, that.y, that.z);
  }

  public Vector3d add(double x, double y, double z) {
    return new Vector3d(this.x + x,
                        this.y + y,
                        this.z + z);
  }

  public Vector3d subLocal(double x, double y, double z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }

  public Vector3d subLocal(Vector3d that) {
    return subLocal(that.x, that.y, that.z);
  }

  public Vector3d sub(Vector3d that) {
    return sub(that.x, that.y, that.z);
  }

  public Vector3d sub(double x, double y, double z) {
    return new Vector3d(this.x - x,
                        this.y - y,
                        this.z - z);
  }

  public Vector3d pointWiseMulLocal(double x, double y, double z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }

  public Vector3d pointWiseMulLocal(Vector3d that) {
    return pointWiseMulLocal(that.x, that.y, that.z);
  }

  public Vector3d pointWiseMul(Vector3d that) {
    return pointWiseMul(that.x, that.y, that.z);
  }

  public Vector3d pointWiseMul(double x, double y, double z) {
    return new Vector3d(this.x * x,
                        this.y * y,
                        this.z * z);
  }

  public Vector3d pointWiseDivLocal(double x, double y, double z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }

  public Vector3d pointWiseDivLocal(Vector3d that) {
    return pointWiseDivLocal(that.x, that.y, that.z);
  }

  public Vector3d pointWiseDiv(Vector3d that) {
    return pointWiseDiv(that.x, that.y, that.z);
  }

  public Vector3d pointWiseDiv(double x, double y, double z) {
    return new Vector3d(this.x / x,
                        this.y / y,
                        this.z / z);
  }

  public Vector3d mulLocal(double scalar) {
    x *= scalar;
    y *= scalar;
    z *= scalar;
    return this;
  }

  public Vector3d mul(double scalar) {
    return new Vector3d(x * scalar,
                        y * scalar,
                        z * scalar);
  }

  public Vector3d divLocal(double scalar) {
    x /= scalar;
    y /= scalar;
    z /= scalar;
    return this;
  }

  public Vector3d div(double scalar) {
    return new Vector3d(x / scalar,
                        y / scalar,
                        z / scalar);
  } 

  public Vector3d mulLocal(Matrix33 transform) {
    return set(
      x * transform.m00 + y * transform.m10 + z * transform.m20,
      x * transform.m01 + y * transform.m11 + z * transform.m21,
      x * transform.m02 + y * transform.m12 + z * transform.m22);
  }

  public Vector3d mul(Matrix33 transform) {
    return new Vector3d(this).mulLocal(transform);
  } 

  public Vector3d affineTransformLocal(Matrix44 transform) { 
    return set(
      x * transform.m00 + y * transform.m01 + z * transform.m02 + transform.m03,
      x * transform.m10 + y * transform.m11 + z * transform.m12 + transform.m13,
      x * transform.m20 + y * transform.m21 + z * transform.m22 + transform.m23);
  }

  public Vector3d affineTransform(Matrix44 transform) {
    return new Vector3d(this).affineTransformLocal(transform);
  } 

  public Vector3d affineTransformAsVectorLocal(Matrix44 transform) {
    return set(
      x * transform.m00 + y * transform.m01 + z * transform.m02,
      x * transform.m10 + y * transform.m11 + z * transform.m12,
      x * transform.m20 + y * transform.m21 + z * transform.m22);
  }

  public Vector3d affineTransformAsVector(Matrix44 transform) {
    return new Vector3d(this).affineTransformAsVectorLocal(transform);
  } 

  public double dotProd(Vector3d that) {
    return x * that.x
         + y * that.y
         + z * that.z;
  } 

  public Vector3d crossProd(Vector3d that) {
    return new Vector3d(y * that.z - z * that.y,
                        z * that.x - x * that.z,
                        x * that.y - y * that.x);
  }
  
  public Matrix33 tensorProd(Vector3d that) {
    return new Matrix33(
      x * that.x, x * that.y, x * that.z,
      y * that.x, y * that.y, y * that.z,
      z * that.x, z * that.y, z * that.z);
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return x*x + y*y + z*z;
  }

  public double distance(Vector3d that) {
    return Math.sqrt(distanceSquared(that));
  }

  public double distanceSquared(Vector3d that) {
    return sub(that).lengthSquared();
  }

  public Vector3d normalizeLocal() {
    double normSquared = lengthSquared();
    
    if (MathUtils.epsilonEquals(normSquared, 1)) {
      return this;
    } 

    return divLocal(Math.sqrt(normSquared));
  }

  public Vector3d normalize() {
    return new Vector3d(this).normalizeLocal();
  }

  public Vector3d homogeneousNormalize() {
    return new Vector3d(this).homogeneousNormalizeLocal();
  }

  public Vector3d homogeneousNormalizeLocal() {
    return divLocal(z);
  } 

  public Vector3d proj(Vector3d that) {
    return that.mul(this.dotProd(that) / that.lengthSquared());
  }

  public Vector3d perp(Vector3d that) {
    return this.sub(this.proj(that));
  }

  public Vector3d midPoint(Vector3d that) {
    return this.add(that).divLocal(2);
  }

  public double angleBetween(Vector3d that) {
    return Math.acos(this.dotProd(that) / (this.length() * that.length()));
  }

  public double angleBetween(Vector3d that, Vector3d reference) {
    int sign = MathUtils.signum(this.dotProd(reference));
    return sign * Math.acos(this.dotProd(that) / (this.length() * that.length()));
  }

  public Vector3d rotateLocal(Vector3d origin, Vector3d axis, double radians) {
    return affineTransformLocal(Matrices.create3dRotateMatrix(origin, axis, radians));
  }

  public Vector3d rotate(Vector3d origin, Vector3d axis, double radians) {
    return new Vector3d(this).rotateLocal(origin, axis, radians); 
  }

  public Vector3d rotateLocal(Vector3d axis, double radians) {
    return affineTransformLocal(Matrices.create3dRotateMatrix(axis, radians));
  }

  public Vector3d rotate(Vector3d axis, double radians) {
    return new Vector3d(this).rotateLocal(axis, radians); 
  } 

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(x+0.0);
    result = 31 * result + MathUtils.hashDouble(y+0.0);
    result = 31 * result + MathUtils.hashDouble(z+0.0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Vector3d)) {
      return false;
    }
    
    var that = (Vector3d) obj;
    
    return this.x == that.x
        && this.y == that.y
        && this.z == that.z;
  }

  @Override
  public String toString() {
    return String.format("[%.3f, %.3f, %.3f]", x, y, z);
  }
  
  @Override
  public ImmutableVector3d freeze() {
    return new ImmutableVector3d(this);
  }

  public static final class ImmutableVector3d extends Vector3d {  
    public ImmutableVector3d(Vector3d that) {
      super.set(that.x, that.y, that.z);
    } 

    @Override
    public Vector3d set(double x, double y, double z) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d addLocal(double x, double y, double z) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d addLocal(Vector3d that) {
      throw new ImmutableModificationException(this);
    }  

    @Override
    public Vector3d subLocal(double x, double y, double z) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d subLocal(Vector3d that) {
      throw new ImmutableModificationException(this);
    } 
    
    @Override
    public Vector3d pointWiseMulLocal(double x, double y, double z) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector3d pointWiseMulLocal(Vector3d that) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector3d pointWiseDivLocal(double x, double y, double z) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector3d pointWiseDivLocal(Vector3d that) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d mulLocal(double scalar) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector3d divLocal(double scalar) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector3d mulLocal(Matrix33 transform) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector3d affineTransformLocal(Matrix44 transform) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d normalizeLocal() {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d rotateLocal(Vector3d origin, Vector3d axis, double radians) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector3d rotateLocal(Vector3d axis, double radians) {
      throw new ImmutableModificationException(this);
    }
  }

  /**
   * @param r : 0 -> infinity
   * @param theta : 0 -> pi
   * @param phi : 0 -> 2pi
   */
  public static Vector3d fromSpherical(double r, double theta, double phi) {
    return new Vector3d(r * Math.sin(theta) * Math.cos(phi),
                        r * Math.sin(theta) * Math.sin(phi),
                        r * Math.cos(theta));
  }

  public static boolean areColinear(Vector3d... v) {
    for (int i = 0; i < v.length-1; i++) {
      if (!MathUtils.epsilonZero(v[i].crossProd(v[i+1]).lengthSquared())) {
        return false;
      }
    }
    return false;
  }

  public static final Vector3d getRandomUnitVector() {
    return getRandomUnitVector(MathConsts.PI, MathConsts.TWO_PI);
  }

  public static final Vector3d getRandomUnitVector(double thetaRange, double phiRange) {
    return fromSpherical(1, 
        XORShift.getInstance().randomDouble(thetaRange), 
        XORShift.getInstance().randomDouble(phiRange));
  } 
}