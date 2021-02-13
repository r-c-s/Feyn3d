package rcs.feyn.math;

import rcs.feyn.three.geo.Transformable3d;


public class Basis3d implements Transformable3d {
  
  protected final Vector3d i = new Vector3d(1, 0, 0);
  protected final Vector3d j = new Vector3d(0, 1, 0);
  protected final Vector3d k = new Vector3d(0, 0, 1);

  public Basis3d() { }

  public Basis3d(Vector3d i, Vector3d j) {
    set(i, j);
  }

  public Basis3d(Vector3d i, Vector3d j, Vector3d k) {
    set(i, j, k);
  }

  public Basis3d(Basis3d b3d) {
    set(b3d);
  }

  public Vector3d i() { 
    return new Vector3d(i); 
  }

  public Vector3d j() { 
    return new Vector3d(j); 
  }

  public Vector3d k() { 
    return new Vector3d(k); 
  }

  public void set(Basis3d b3d) {
    this.set(b3d.i, b3d.j, b3d.k);
  }

  public void set(Vector3d i, Vector3d j) {
    this.set(i, j, i.crossProd(j));
  }

  public void set(Vector3d i, Vector3d j, Vector3d k) {
    if (!validate(i, j, k)) {
      throw new IllegalArgumentException("Basis vectors must be unit vectors and orthogonal.");
    }
    
    this.i.set(i);
    this.j.set(j);
    this.k.set(k);
  }

  private boolean validate(Vector3d x, Vector3d y, Vector3d z) {
    return MathUtils.epsilonEquals(y.dotProd(x), 0) 
        && MathUtils.epsilonEquals(x.dotProd(z), 0) 
        && MathUtils.epsilonEquals(z.dotProd(y), 0) 
        && MathUtils.epsilonEquals(x.lengthSquared(), 1) 
        && MathUtils.epsilonEquals(y.lengthSquared(), 1) 
        && MathUtils.epsilonEquals(z.lengthSquared(), 1);
  }

  @Override
  public void transform(Matrix44 transform) { 
    i.affineTransformAsVectorLocal(transform);
    j.affineTransformAsVectorLocal(transform);
    k.set(i.crossProd(j));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + i.hashCode();
    result = 31 * result + j.hashCode();
    result = 31 * result + k.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Basis3d)) {
      return false;
    } 
    
    var that = (Basis3d) obj;

    return this.i.equals(that.i) &&
           this.j.equals(that.j) &&
           this.k.equals(that.k);
  }

  @Override
  public String toString() {
      return String.format("%s:[i=%s,j=%s,k=%s]", super.toString(), i, j, k);
  } 
}
