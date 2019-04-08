package rcs.feyn.math.linalg;

import rcs.feyn.math.MathUtils;
import rcs.feyn.two.geo.Transformable2d;


public class Basis2d implements Transformable2d {
  
  protected final Vector2d i = new Vector2d(Vector2d.X_AXIS);
  protected final Vector2d j = new Vector2d(Vector2d.Y_AXIS);

  public Basis2d() { }

  public Basis2d(Vector2d i, Vector2d j) {
    set(i, j);
  }

  public Basis2d(Basis2d b2d) {
    set(b2d);
  }

  public Vector2d i() { 
    return new Vector2d(i); 
  }
  
  public Vector2d j() { 
    return new Vector2d(j); 
  } 

  public void set(Basis2d b2d) {
    this.set(b2d.i, b2d.j);
  }

  public void set(Vector2d i, Vector2d j) {
    if (!validate(i, j)) {
      throw new IllegalArgumentException("Basis vectors must be unit vectors and orthogonal.");
    }
    
    this.i.set(i);
    this.j.set(j);
  }

  private boolean validate(Vector2d x, Vector2d y) {
    return MathUtils.epsilonEquals(x.dotProd(y), 0) 
        && MathUtils.epsilonEquals(x.normSquared(), 1) 
        && MathUtils.epsilonEquals(y.normSquared(), 1);
  }

  @Override
  public void transform(Matrix33 transform) {
    i.affineTransformAsVectorLocal(transform);
    j.affineTransformAsVectorLocal(transform);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + i.hashCode();
    result = 31 * result + j.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Basis2d)) {
      return false;
    }
    
    Basis2d that = (Basis2d) obj;
    
    return this.i.equals(that.i) &&
           this.j.equals(that.j);
  }

  @Override
  public String toString() {
      return String.format("%s:[i=%s,j=%s]", super.toString(), i, j);
  }
}
