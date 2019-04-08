package rcs.feyn.math.linalg;

import rcs.feyn.math.MathUtils;

public class Vector {

  private double[] vector;
  private int N;

  public Vector(Vector that) {
    this(that.vector);
  } 

  public Vector(double... vector) {
    this.vector = new double[vector.length];
    this.N      = vector.length; 
    set(vector);
  } 

  public double get(int i) {
    return vector[i];
  }

  public void set(double... vector) {
    if (vector.length != N) {
      throw new IllegalArgumentException("Cannot change dimension of vector. Create a new one.");
    }
    
    for (int i = 0; i < N; i++) {
      this.vector[i] = vector[i];
    }
  }

  public Vector add(Vector that) {
    return new Vector(this).addLocal(that);
  }

  public Vector addLocal(Vector that) {
    if (this.N != that.N) {
      throw new IllegalArgumentException("Cannot add vectors of different dimensions."); 
    }

    for (int i = 0; i < N; i++) {
      this.vector[i] += that.vector[i];
    }

    return this;
  }

  public Vector sub(Vector that) {
    return new Vector(this).subLocal(that);
  }

  public Vector subLocal(Vector that) {
    if (this.N != that.N) {
      throw new IllegalArgumentException("Cannot subtract vectors of different dimensions.");
    }

    for (int i = 0; i < N; i++) {
      this.vector[i] -= that.vector[i]; 
    }

    return this;
  }

  public Vector mul(double scalar) {
    return new Vector(this).mulLocal(scalar);
  }

  public Vector mulLocal(double scalar) {
    for (int i = 0; i < N; i++) {
      this.vector[i] *= scalar; 
    }

    return this;
  }

  public Vector div(double denominator) {
    return mul(1 / denominator);
  }

  public Vector divLocal(double denominator) {
    return mulLocal(1 / denominator);
  }

  public double dotProd(Vector that) {
    if (this.N != that.N) {
      throw new IllegalArgumentException("Cannot compute dot product of vectors of different dimensions."); 
    }

    double dot = 0;

    for (int i = 0; i < N; i++) {
      dot += this.vector[i] * that.vector[i]; 
    }

    return dot;
  }

  public Vector normalize() {
    return new Vector(this).normalizeLocal();
  }

  public Vector normalizeLocal() {
    double norm = norm();

    if (norm == 0) {
      throw new IllegalArgumentException("Cannot normalize a vector of length 0."); 
    } 
    
    for (int i = 0; i < N; i++) {
      vector[i] /= norm;
    } 

    return this;
  }

  public double norm() {
    return Math.sqrt(normSquared());
  }

  public double normSquared() {
    return dotProd(this);
  }

  public double distance(Vector that) {
    return Math.sqrt(distanceSquared(that));
  }

  public double distanceSquared(Vector that) {
    return this.sub(that).normSquared();
  }

  @Override
  public int hashCode() {
    int result = 17;
    for (double v : vector) {
      result = 31 * result + MathUtils.hashDouble(v);
    }
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Vector)) {
      return false;
    }
    
    Vector that = (Vector) obj;
    
    if (this.N != that.N) {
      return false;
    } 
    for (int i = 0; i < N; i++) {
      if (!MathUtils.epsilonEquals(this.vector[i], that.vector[i])) {
        return false;
      }
    }
    
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (double v : vector) {
      sb.append(String.format("%.3f, ", v));
    }
    sb.delete(sb.length()-2, sb.length());
    sb.append("]"); 
    return sb.toString();
  } 
}