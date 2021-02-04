package rcs.feyn.math.linalg;

import rcs.feyn.math.MathUtils;
import rcs.feyn.utils.Freezable;
import rcs.feyn.utils.ImmutableModificationException;

public class Vector4d implements Freezable<Vector4d> {   

  private double x, y, z, w;

  public Vector4d() { }

  public Vector4d(double x, double y, double z, double w) {
    set(x, y, z, w);
  }

  public Vector4d(Vector4d that) {
    set(that.x, that.y, that.z, that.w);
  }  

  public Vector2d toVector2d() {
    return new Vector2d(x, y);
  }

  public Vector3d toVector3d() {
    return new Vector3d(x, y, z);
  }

  public Vector4d set(Vector4d that) {
    return set(that.x, that.y, that.z, that.w);
  }

  public Vector4d set(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
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

  public double w() {
    return w;
  } 

  public Vector4d x(double x) {
    this.x = x;
    return this;
  }

  public Vector4d y(double y) {
    this.y = y;
    return this;
  }

  public Vector4d z(double z) {
    this.z = z;
    return this;
  } 

  public Vector4d w(double w) {
    this.w = w;
    return this;
  } 

  public Vector4d addLocal(double x, double y, double z, double w) {
    this.x += x;
    this.y += y;
    this.z += z;
    this.w += w;
    return this;
  }

  public Vector4d addLocal(Vector4d that) {
    return addLocal(that.x, that.y, that.z, that.z);
  }

  public Vector4d add(Vector4d that) {
    return add(that.x, that.y, that.z, that.w);
  }

  public Vector4d add(double x, double y, double z, double w) {
    return new Vector4d(this.x + x,
                        this.y + y,
                        this.z + z,
                        this.w + w);
  }

  public Vector4d subLocal(double x, double y, double z, double w) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    this.w -= w;
    return this;
  }

  public Vector4d subLocal(Vector4d that) {
    return subLocal(that.x, that.y, that.z, that.w);
  }

  public Vector4d sub(Vector4d that) {
    return sub(that.x, that.y, that.z, that.w);
  }

  public Vector4d sub(double x, double y, double z, double w) {
    return new Vector4d(this.x - x,
                        this.y - y,
                        this.z - z,
                        this.w - w);
  }

  public Vector4d pointWiseMulLocal(double x, double y, double z, double w) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    this.w *= w;
    return this;
  }

  public Vector4d pointWiseMulLocal(Vector4d that) {
    return pointWiseMulLocal(that.x, that.y, that.z, that.w);
  }

  public Vector4d pointWiseMul(Vector4d that) {
    return pointWiseMul(that.x, that.y, that.z, that.w);
  }

  public Vector4d pointWiseMul(double x, double y, double z, double w) {
    return new Vector4d(this.x * x,
                        this.y * y,
                        this.z * z,
                        this.w * w);
  }

  public Vector4d pointWiseDivLocal(double x, double y, double z, double w) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    this.w /= w;
    return this;
  }

  public Vector4d pointWiseDivLocal(Vector4d that) {
    return pointWiseDivLocal(that.x, that.y, that.z, that.w);
  }

  public Vector4d pointWiseDiv(Vector4d that) {
    return pointWiseDiv(that.x, that.y, that.z, that.w);
  }

  public Vector4d pointWiseDiv(double x, double y, double z, double w) {
    return new Vector4d(this.x / x,
                        this.y / y,
                        this.z / z,
                        this.w / w);
  }

  public Vector4d mulLocal(double scalar) {
    x *= scalar;
    y *= scalar;
    z *= scalar;
    w *= scalar;
    return this;
  }

  public Vector4d mul(double scalar) {
    return new Vector4d(x * scalar,
                        y * scalar,
                        z * scalar,
                        w * scalar);
  }

  public Vector4d divLocal(double scalar) {
    x /= scalar;
    y /= scalar;
    z /= scalar;
    w /= scalar;
    return this;
  }

  public Vector4d div(double scalar) {
    return new Vector4d(x / scalar,
                        y / scalar,
                        z / scalar,
                        w / scalar);
  } 

  public Vector4d mulLocal(Matrix44 transform) {
    return set(
      x * transform.m00 + y * transform.m10 + z * transform.m20 + w + transform.m30,
      x * transform.m01 + y * transform.m11 + z * transform.m21 + w + transform.m31,
      x * transform.m02 + y * transform.m12 + z * transform.m22 + w + transform.m32,
      x * transform.m03 + y * transform.m13 + z * transform.m23 + w + transform.m33);
  }

  public Vector4d mul(Matrix44 transform) {
    return new Vector4d(this).mulLocal(transform);
  }

  public double dotProd(Vector4d that) {
    return x * that.x
         + y * that.y
         + z * that.z
         + w * that.w;
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return x*x + y*y + z*z + w*w;
  }

  public double distance(Vector4d that) {
    return Math.sqrt(distanceSquared(that));
  }

  public double distanceSquared(Vector4d that) {
    return sub(that).lengthSquared();
  }

  public Vector4d normalizeLocal() {
    double normSquared = lengthSquared();
    
    if (MathUtils.epsilonEquals(normSquared, 1)) {
      return this;
    } 

    return divLocal(Math.sqrt(normSquared));
  }

  public Vector4d normalize() {
    return new Vector4d(this).normalizeLocal();
  }

  public Vector4d homogeneousNormalize() {
    return new Vector4d(this).homogeneousNormalizeLocal();
  }

  public Vector4d homogeneousNormalizeLocal() {
    return divLocal(w);
  } 

  public Vector4d proj(Vector4d that) {
    return that.mul(this.dotProd(that) / that.lengthSquared());
  }

  public Vector4d perp(Vector4d that) {
    return this.sub(this.proj(that));
  }

  public Vector4d midPoint(Vector4d that) {
    return this.add(that).divLocal(2);
  }

  public double angleBetween(Vector4d that) {
    return Math.acos(this.dotProd(that) / (this.length() * that.length()));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(x+0.0);
    result = 31 * result + MathUtils.hashDouble(y+0.0);
    result = 31 * result + MathUtils.hashDouble(z+0.0);
    result = 31 * result + MathUtils.hashDouble(w+0.0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Vector4d)) {
      return false;
    }
    
    var that = (Vector4d) obj;

    return this.x == that.x
        && this.y == that.y
        && this.z == that.z
        && this.w == that.w;
  }

  @Override
  public String toString() {
    return String.format("[%.3f, %.3f, %.3f, %.3f]", x, y, z, w);
  }
  
  @Override
  public ImmutableVector3d freeze() {
    return new ImmutableVector3d(this);
  }

  public static final class ImmutableVector3d extends Vector4d {  
    public ImmutableVector3d(Vector4d that) {
      super.set(that.x, that.y, that.z, that.w);
    } 

    @Override
    public Vector4d set(double x, double y, double z, double w) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector4d addLocal(double x, double y, double z, double w) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector4d addLocal(Vector4d that) {
      throw new ImmutableModificationException(this);
    }  

    @Override
    public Vector4d subLocal(double x, double y, double z, double w) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector4d subLocal(Vector4d that) {
      throw new ImmutableModificationException(this);
    } 
    
    @Override
    public Vector4d pointWiseMulLocal(double x, double y, double z, double w) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector4d pointWiseMulLocal(Vector4d that) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector4d pointWiseDivLocal(double x, double y, double z, double w) {
      throw new ImmutableModificationException(this);
    }
    
    @Override
    public Vector4d pointWiseDivLocal(Vector4d that) {
      throw new ImmutableModificationException(this);
    }

    @Override
    public Vector4d mulLocal(double scalar) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector4d divLocal(double scalar) {
      throw new ImmutableModificationException(this);
    } 

    @Override
    public Vector4d mulLocal(Matrix44 transform) {
      throw new ImmutableModificationException(this);
    }  

    @Override
    public Vector4d normalizeLocal() {
      throw new ImmutableModificationException(this);
    }
  }
}