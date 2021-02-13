package rcs.feyn.math;

public class Matrix44 { 

  double m00, m01, m02, m03,
         m10, m11, m12, m13,
         m20, m21, m22, m23,
         m30, m31, m32, m33;

  public Matrix44() { 
    set(1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1);
  }

  public Matrix44(
          double m00, double m01, double m02, double m03,
          double m10, double m11, double m12, double m13,
          double m20, double m21, double m22, double m23,
          double m30, double m31, double m32, double m33) {
    set(m00, m01, m02, m03,
        m10, m11, m12, m13,
        m20, m21, m22, m23,
        m30, m31, m32, m33);
  }

  public Matrix44(Matrix44 m4x4) {
    set(m4x4.m00, m4x4.m01, m4x4.m02, m4x4.m03,
        m4x4.m10, m4x4.m11, m4x4.m12, m4x4.m13,
        m4x4.m20, m4x4.m21, m4x4.m22, m4x4.m23,
        m4x4.m30, m4x4.m31, m4x4.m32, m4x4.m33);
  }

  public void set(
          double m00, double m01, double m02, double m03,
          double m10, double m11, double m12, double m13,
          double m20, double m21, double m22, double m23,
          double m30, double m31, double m32, double m33) {
    this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
    this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
    this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
    this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
  }

  public Matrix44 add(Matrix44 that) {
    return new Matrix44(this).addLocal(that);
  }

  public Matrix44 addLocal(Matrix44 that) {
    this.m00 += that.m00; this.m01 += that.m01; this.m02 += that.m02; this.m03 += that.m03;
    this.m10 += that.m10; this.m11 += that.m11; this.m12 += that.m12; this.m13 += that.m13;
    this.m20 += that.m20; this.m21 += that.m21; this.m22 += that.m22; this.m23 += that.m23;
    this.m30 += that.m30; this.m31 += that.m31; this.m32 += that.m32; this.m33 += that.m33;
    
    return this;
  }

  public Matrix44 sub(Matrix44 that) {
    return new Matrix44(this).subLocal(that);
  }

  public Matrix44 subLocal(Matrix44 that) {
    this.m00 -= that.m00; this.m01 -= that.m01; this.m02 -= that.m02; this.m03 -= that.m03;
    this.m10 -= that.m10; this.m11 -= that.m11; this.m12 -= that.m12; this.m13 -= that.m13;
    this.m20 -= that.m20; this.m21 -= that.m21; this.m22 -= that.m22; this.m23 -= that.m23;
    this.m30 -= that.m30; this.m31 -= that.m31; this.m32 -= that.m32; this.m33 -= that.m33;
    
    return this;
  }

  public Matrix44 mul(Matrix44 that) {
    return new Matrix44(this).mulLocal(that);
  }

  public Matrix44 mulLocal(Matrix44 that) {
    set(
      this.m00*that.m00 + this.m01*that.m10 + this.m02*that.m20 + this.m03*that.m30,
      this.m00*that.m01 + this.m01*that.m11 + this.m02*that.m21 + this.m03*that.m31,
      this.m00*that.m02 + this.m01*that.m12 + this.m02*that.m22 + this.m03*that.m32,
      this.m00*that.m03 + this.m01*that.m13 + this.m02*that.m23 + this.m03*that.m33, 

      this.m10*that.m00 + this.m11*that.m10 + this.m12*that.m20 + this.m13*that.m30,
      this.m10*that.m01 + this.m11*that.m11 + this.m12*that.m21 + this.m13*that.m31,
      this.m10*that.m02 + this.m11*that.m12 + this.m12*that.m22 + this.m13*that.m32,
      this.m10*that.m03 + this.m11*that.m13 + this.m12*that.m23 + this.m13*that.m33, 

      this.m20*that.m00 + this.m21*that.m10 + this.m22*that.m20 + this.m23*that.m30,
      this.m20*that.m01 + this.m21*that.m11 + this.m22*that.m21 + this.m23*that.m31,
      this.m20*that.m02 + this.m21*that.m12 + this.m22*that.m22 + this.m23*that.m32,
      this.m20*that.m03 + this.m21*that.m13 + this.m22*that.m23 + this.m23*that.m33, 

      this.m30*that.m00 + this.m31*that.m10 + this.m32*that.m20 + this.m33*that.m30,
      this.m30*that.m01 + this.m31*that.m11 + this.m32*that.m21 + this.m33*that.m31,
      this.m30*that.m02 + this.m31*that.m12 + this.m32*that.m22 + this.m33*that.m32,
      this.m30*that.m03 + this.m31*that.m13 + this.m32*that.m23 + this.m33*that.m33);
    
    return this;
  }

  public Matrix44 mul(double scalar) {
    return new Matrix44(this).mulLocal(scalar);
  }

  public Matrix44 mulLocal(double scalar) {
    this.m00 *= scalar; this.m01 *= scalar; this.m02 *= scalar; this.m03 *= scalar;
    this.m10 *= scalar; this.m11 *= scalar; this.m12 *= scalar; this.m13 *= scalar;
    this.m20 *= scalar; this.m21 *= scalar; this.m22 *= scalar; this.m23 *= scalar;
    this.m30 *= scalar; this.m31 *= scalar; this.m32 *= scalar; this.m33 *= scalar;
    
    return this;
  }

  public Vector4d mul(Vector4d vector) {
    return mul(vector.x(), vector.y(), vector.z(), vector.w());
  }

  public Vector4d mul(double x, double y, double z, double w) {
    return new Vector4d(
      m00 * x + m01 * y + m02 * z + m03 * w,
      m10 * x + m11 * y + m12 * z + m13 * w,
      m20 * x + m21 * y + m22 * z + m23 * w,
      m30 * x + m31 * y + m32 * z + m33 * w);
  }

  public Matrix44 div(Matrix44 that) {
    return new Matrix44(this).divLocal(that);
  }

  public Matrix44 divLocal(Matrix44 that) {
    return mulLocal(that.inverse());
  }

  public Matrix44 div(double scalar) {
    return new Matrix44(this).divLocal(scalar);
  }

  public Matrix44 divLocal(double scalar) {
    return mulLocal(1 / scalar);
  } 

  public Matrix44 transpose() {
    return new Matrix44(
      m00, m10, m20, m30,
      m01, m11, m21, m31,
      m02, m12, m22, m32,
      m03, m13, m23, m33);
  }

  public Matrix44 inverse() {
    double det = determinant();
    if (det == 0) {
      throw new AssertionError();
    }
    double invDet = 1/det;
    return new Matrix44(
      invDet * (m12*m23*m31 - m13*m22*m31 + m13*m21*m32 - m11*m23*m32 - m12*m21*m33 + m11*m22*m33),
      invDet * (m03*m22*m31 - m02*m23*m31 - m03*m21*m32 + m01*m23*m32 + m02*m21*m33 - m01*m22*m33),
      invDet * (m02*m13*m31 - m03*m12*m31 + m03*m11*m32 - m01*m13*m32 - m02*m11*m33 + m01*m12*m33),
      invDet * (m03*m12*m21 - m02*m13*m21 - m03*m11*m22 + m01*m13*m22 + m02*m11*m23 - m01*m12*m23),
      invDet * (m13*m22*m30 - m12*m23*m30 - m13*m20*m32 + m10*m23*m32 + m12*m20*m33 - m10*m22*m33),
      invDet * (m02*m23*m30 - m03*m22*m30 + m03*m20*m32 - m00*m23*m32 - m02*m20*m33 + m00*m22*m33),
      invDet * (m03*m12*m30 - m02*m13*m30 - m03*m10*m32 + m00*m13*m32 + m02*m10*m33 - m00*m12*m33),
      invDet * (m02*m13*m20 - m03*m12*m20 + m03*m10*m22 - m00*m13*m22 - m02*m10*m23 + m00*m12*m23),
      invDet * (m11*m23*m30 - m13*m21*m30 + m13*m20*m31 - m10*m23*m31 - m11*m20*m33 + m10*m21*m33),
      invDet * (m03*m21*m30 - m01*m23*m30 - m03*m20*m31 + m00*m23*m31 + m01*m20*m33 - m00*m21*m33),
      invDet * (m01*m13*m30 - m03*m11*m30 + m03*m10*m31 - m00*m13*m31 - m01*m10*m33 + m00*m11*m33),
      invDet * (m03*m11*m20 - m01*m13*m20 - m03*m10*m21 + m00*m13*m21 + m01*m10*m23 - m00*m11*m23),
      invDet * (m12*m21*m30 - m11*m22*m30 - m12*m20*m31 + m10*m22*m31 + m11*m20*m32 - m10*m21*m32),
      invDet * (m01*m22*m30 - m02*m21*m30 + m02*m20*m31 - m00*m22*m31 - m01*m20*m32 + m00*m21*m32),
      invDet * (m02*m11*m30 - m01*m12*m30 - m02*m10*m31 + m00*m12*m31 + m01*m10*m32 - m00*m11*m32),
      invDet * (m01*m12*m20 - m02*m11*m20 + m02*m10*m21 - m00*m12*m21 - m01*m10*m22 + m00*m11*m22));
  }   

  public double determinant() {
    return MathUtils.determinant(m00, m01, m02, m03,
                                 m10, m11, m12, m13,
                                 m20, m21, m22, m23,
                                 m30, m31, m32, m33);
  }

  public Matrix44 extractRotation() {
    double sx = extractScaleX();
    double sy = extractScaleY();
    double sz = extractScaleZ();
    
    return new Matrix44(m00/sx, m01/sy, m02/sz,   0,
                        m10/sx, m11/sy, m12/sz,   0,
                        m20/sx, m21/sy, m22/sz,   0,
                        m30,    m31,    m32,    m33);
  }

  public Vector3d extractTranslation() {
    return new Vector3d(m03, m13, m23);
  }

  public double extractScaleX() {
    return new Vector3d(m00, m01, m02).length();
  }

  public double extractScaleY() {
    return new Vector3d(m10, m11, m12).length();
  }

  public double extractScaleZ() {
    return new Vector3d(m20, m21, m22).length();
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(m00+0.0);
    result = 31 * result + MathUtils.hashDouble(m01+0.0);
    result = 31 * result + MathUtils.hashDouble(m02+0.0);
    result = 31 * result + MathUtils.hashDouble(m03+0.0);
    result = 31 * result + MathUtils.hashDouble(m10+0.0);
    result = 31 * result + MathUtils.hashDouble(m11+0.0);
    result = 31 * result + MathUtils.hashDouble(m12+0.0);
    result = 31 * result + MathUtils.hashDouble(m13+0.0);
    result = 31 * result + MathUtils.hashDouble(m20+0.0);
    result = 31 * result + MathUtils.hashDouble(m21+0.0);
    result = 31 * result + MathUtils.hashDouble(m22+0.0);
    result = 31 * result + MathUtils.hashDouble(m23+0.0);
    result = 31 * result + MathUtils.hashDouble(m30+0.0);
    result = 31 * result + MathUtils.hashDouble(m31+0.0);
    result = 31 * result + MathUtils.hashDouble(m32+0.0);
    result = 31 * result + MathUtils.hashDouble(m33+0.0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Matrix44)) {
      return false;
    }
    
    var that = (Matrix44) obj;

    return 
        this.m00 == that.m00 && this.m01 == that.m01 && this.m02 == that.m02 && this.m03 == that.m03 &&
        this.m10 == that.m10 && this.m11 == that.m11 && this.m12 == that.m12 && this.m13 == that.m13 &&
        this.m20 == that.m20 && this.m21 == that.m21 && this.m22 == that.m22 && this.m23 == that.m23 &&
        this.m30 == that.m30 && this.m31 == that.m31 && this.m32 == that.m32 && this.m33 == that.m33;
  }

  @Override
  public String toString() {
    return String.format("%s:\n"
                       + "%.4f %.4f %.4f %.4f\n"
                       + "%.4f %.4f %.4f %.4f\n"
                       + "%.4f %.4f %.4f %.4f\n"
                       + "%.4f %.4f %.4f %.4f", 
          super.toString(), 
          m00, m01, m02, m03,
          m10, m11, m12, m13,
          m20, m21, m22, m23,
          m30, m31, m32, m33);
  }
}
