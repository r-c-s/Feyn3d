package rcs.feyn.math;

public class Matrix33 { 
  
  double m00, m01, m02,
         m10, m11, m12,
         m20, m21, m22;
  
  public Matrix33() {
    toIdentity();
  }

  public Matrix33(
          double m00, double m01, double m02,
          double m10, double m11, double m12,
          double m20, double m21, double m22) {
    set(m00, m01, m02,
        m10, m11, m12,
        m20, m21, m22);
  }

  public Matrix33(Matrix33 m3x3) {
    set(m3x3.m00, m3x3.m01, m3x3.m02,
        m3x3.m10, m3x3.m11, m3x3.m12,
        m3x3.m20, m3x3.m21, m3x3.m22);
  }

  public void toIdentity() {
    set(1, 0, 0,
        0, 1, 0,
        0, 0, 1);
  }

  public void set(
          double m00, double m01, double m02,
          double m10, double m11, double m12,
          double m20, double m21, double m22) {
    this.m00 = m00; this.m01 = m01; this.m02 = m02;
    this.m10 = m10; this.m11 = m11; this.m12 = m12;
    this.m20 = m20; this.m21 = m21; this.m22 = m22;
  }

  public Matrix33 add(Matrix33 that) {
    return new Matrix33(this).addLocal(that);
  }

  public Matrix33 addLocal(Matrix33 that) {
    this.m00 += that.m00; this.m01 += that.m01; this.m02 += that.m02;  
    this.m10 += that.m10; this.m11 += that.m11; this.m12 += that.m12;  
    this.m20 += that.m20; this.m21 += that.m21; this.m22 += that.m22;  
    
    return this;
  }

  public Matrix33 sub(Matrix33 that) {
    return new Matrix33(this).subLocal(that);
  }

  public Matrix33 subLocal(Matrix33 that) {
    this.m00 -= that.m00; this.m01 -= that.m01; this.m02 -= that.m02; 
    this.m10 -= that.m10; this.m11 -= that.m11; this.m12 -= that.m12; 
    this.m20 -= that.m20; this.m21 -= that.m21; this.m22 -= that.m22; 
    
    return this;
  }

  public Matrix33 mul(Matrix33 that) {
    return new Matrix33(this).mulLocal(that);
  }

  public Matrix33 mulLocal(Matrix33 that) {
    set(
      this.m00*that.m00 + this.m01*that.m10 + this.m02*that.m20,
      this.m00*that.m01 + this.m01*that.m11 + this.m02*that.m21,
      this.m00*that.m02 + this.m01*that.m12 + this.m02*that.m22, 
  
      this.m10*that.m00 + this.m11*that.m10 + this.m12*that.m20,
      this.m10*that.m01 + this.m11*that.m11 + this.m12*that.m21,
      this.m10*that.m02 + this.m11*that.m12 + this.m12*that.m22, 
  
      this.m20*that.m00 + this.m21*that.m10 + this.m22*that.m20,
      this.m20*that.m01 + this.m21*that.m11 + this.m22*that.m21,
      this.m20*that.m02 + this.m21*that.m12 + this.m22*that.m22);
    
    return this;
  }  

  public Matrix33 mul(double scalar) {
    return new Matrix33(this).mulLocal(scalar);
  }

  public Matrix33 mulLocal(double scalar) {
    this.m00 *= scalar; this.m01 *= scalar; this.m02 *= scalar;
    this.m10 *= scalar; this.m11 *= scalar; this.m12 *= scalar;
    this.m20 *= scalar; this.m21 *= scalar; this.m22 *= scalar;
    
    return this;
  }

  public Vector3d mul(Vector3d vector) {
    return mul(vector.x(), vector.y(), vector.z());
  }

  public Vector3d mul(double x, double y, double z) {
    return new Vector3d(
      x * m00 + y * m01 + z * m02,
      x * m10 + y * m11 + z * m12,
      x * m20 + y * m21 + z * m22);
  }

  public Matrix33 div(Matrix33 that) {
    return new Matrix33(this).divLocal(that);
  }

  public Matrix33 divLocal(Matrix33 that) {
    return mulLocal(that.inverse());
  }

  public Matrix33 div(double scalar) {
    return new Matrix33(this).divLocal(scalar);
  }

  public Matrix33 divLocal(double scalar) {
    return mulLocal(1 / scalar);
  }

  public Matrix33 inverse() {
    double det = determinant(); 
    if (det == 0) {
      throw new IllegalArgumentException();
    } 
    double invDet = 1/det;
    return new Matrix33(
        m11*m22-m12*m21*invDet, m02*m21-m01*m22*invDet, m01*m12-m02*m11*invDet, 
        m12*m20-m10*m22*invDet, m00*m22-m02*m20*invDet, m02*m10-m00*m12*invDet, 
        m10*m21-m11*m20*invDet, m11*m01-m00*m21*invDet, m00*m11-m01*m10*invDet);
  }

  public double determinant() {
    return MathUtils.determinant(m00, m01, m02,
                                 m10, m11, m12,
                                 m20, m21, m22);
  }

  public Matrix33 extractRotation() {
    double sx = extractScaleX();
    double sy = extractScaleY();
    
    return new Matrix33(m00/sx, m01/sy,   0,
                        m10/sx, m11/sy,   0,
                           m20,    m21, m22);
  }

  public Vector2d extractTranslation() {
    return new Vector2d(m02, m12);
  }

  public double extractScaleX() {
    return new Vector2d(m00, m01).length();
  }

  public double extractScaleY() {
    return new Vector2d(m10, m11).length();
  } 

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(m00+0.0);
    result = 31 * result + MathUtils.hashDouble(m01+0.0);
    result = 31 * result + MathUtils.hashDouble(m02+0.0);
    result = 31 * result + MathUtils.hashDouble(m10+0.0);
    result = 31 * result + MathUtils.hashDouble(m11+0.0);
    result = 31 * result + MathUtils.hashDouble(m12+0.0);
    result = 31 * result + MathUtils.hashDouble(m20+0.0);
    result = 31 * result + MathUtils.hashDouble(m21+0.0);
    result = 31 * result + MathUtils.hashDouble(m22+0.0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Matrix33)) {
      return false;
    }
    
    var that = (Matrix33) obj;
    
    return
        this.m00 == that.m00 && this.m01 == that.m01 && this.m02 == that.m02 &&
        this.m10 == that.m10 && this.m11 == that.m11 && this.m12 == that.m12 &&
        this.m20 == that.m20 && this.m21 == that.m21 && this.m22 == that.m22;
  }

  @Override
  public String toString() {
    return String.format("%s:\n"
                       + "%.4f %.4f %.4f\n"
                       + "%.4f %.4f %.4f\n"
                       + "%.4f %.4f %.4f", 
          super.toString(), 
          m00, m01, m02,
          m10, m11, m12,
          m20, m21, m22);
  }
  
  public static Matrix33 createRotateMatrix(double radians) {
    return createRotateMatrix(Vector2d.ZERO, radians);
  }

  public static Matrix33 createRotateMatrix(Vector2d p, double radians) {
    double px = p.x();
    double py = p.y();

    double cos = TrigLookUp.cos(radians);
    double sin = TrigLookUp.sin(radians); 
    
    return new Matrix33(
      cos, -sin, px*(1-cos)+py*sin,
      sin,  cos, py*(1-cos)-px*sin,
        0,    0, 1
    );
  }

  public static Matrix33 createTranslateMatrix(Vector2d v) {
    return createTranslateMatrix(v.x(), v.y());
  }

  public static Matrix33 createTranslateMatrix(double dx, double dy) {
    return new Matrix33(
      1, 0, dx,
      0, 1, dy,
      0, 0,  1
    );
  }

  public static Matrix33 createScaleMatrix(double s) {
    return createScaleMatrix(s, s);
  }

  public static Matrix33 createScaleMatrix(double s, Vector2d p) {
    return createScaleMatrix(s, s, p);
  }

  public static Matrix33 createScaleMatrix(double sx, double sy) {
    return new Matrix33(
      sx,  0, 0,
       0, sy, 0,
       0,  0, 1);
  }

  public static Matrix33 createScaleMatrix(double sx, double sy, Vector2d p) {
    return new Matrix33(
      sx,  0, p.x()*(1-sx),
       0, sy, p.y()*(1-sy),
       0,  0, 1);
  } 
}