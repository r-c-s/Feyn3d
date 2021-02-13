package rcs.feyn.math;

public class Matrices {
  
  public Matrices() {
    throw new AssertionError();
  }
  
  public static Matrix44 create3dRotateMatrix(Vector3d v, double radians) {
    return create3dRotateMatrix(Vector3d.ZERO, v, radians);
  }

  public static Matrix44 create3dRotateMatrix(Vector3d p, Vector3d v, double radians) {    
    Vector3d u = v.normalize();
    
    double ux = u.x();
    double uy = u.y();
    double uz = u.z();
    
    double px = p.x();
    double py = p.y();
    double pz = p.z();

    double cos = TrigLookUp.cos(radians);
    double sin = TrigLookUp.sin(radians);
    
    double K = 1-cos;
    
    double a = cos+ux*ux*K;    double b = ux*uy*K-uz*sin; double c = ux*uz*K+uy*sin; double d = px*(1-a)-py*b-pz*c;
    double e = uy*ux*K+uz*sin; double f = cos+uy*uy*K;    double g = uy*uz*K-ux*sin; double h = py*(1-f)-pz*g-px*e;
    double i = uz*ux*K-uy*sin; double j = uz*uy*K+ux*sin; double k = cos+uz*uz*K;    double l = pz*(1-k)-px*i-py*j;

    return new Matrix44(
      a, b, c, d,
      e, f, g, h,
      i, j, k, l,
      0, 0, 0, 1);
  }

  public static Matrix44 create3dXRotateMatrix(double deg) {
    return create3dXRotateMatrix(Vector3d.ZERO, deg);
  }

  public static Matrix44 create3dXRotateMatrix(Vector3d p, double radians) {
    double cos = TrigLookUp.cos(radians);
    double sin = TrigLookUp.sin(radians);

    return new Matrix44(
      1,   0,    0,                       0,
      0, cos, -sin, p.y()*(1-cos)+p.z()*sin,
      0, sin,  cos, p.z()*(1-cos)-p.y()*sin,
      0,   0,    0,                       1);
  }

  public static Matrix44 create3dYRotateMatrix(double deg) {
    return create3dYRotateMatrix(Vector3d.ZERO, deg);
  }

  public static Matrix44 create3dYRotateMatrix(Vector3d p, double radians) {
    double cos = TrigLookUp.cos(radians);
    double sin = TrigLookUp.sin(radians);

    return new Matrix44(
       cos, 0, sin, p.x()*(1-cos)-p.z()*sin,
         0, 1,   0,                       0,
      -sin, 0, cos, p.z()*(1-cos)+p.x()*sin,
         0, 0,   0,                       1); 
  }
  
  public static Matrix44 create3dZRotateMatrix(double radians) {
    return create3dZRotateMatrix(Vector3d.ZERO, radians);
  }

  public static Matrix44 create3dZRotateMatrix(Vector3d p, double radians) {
    double cos = TrigLookUp.cos(radians);
    double sin = TrigLookUp.sin(radians);

    return new Matrix44(
      cos, -sin, 0, p.x()*(1-cos)+p.y()*sin,
      sin,  cos, 0, p.y()*(1-cos)-p.x()*sin,
        0,    0, 1,                       0,
        0,    0, 0,                       1);  
  }

  public static Matrix44 create3dRotateMatrix(double radiansx, double radiansy, double radiansz) {
    double cosx = TrigLookUp.cos(radiansx);
    double sinx = TrigLookUp.sin(radiansx);
    double cosy = TrigLookUp.cos(radiansy);
    double siny = TrigLookUp.sin(radiansy);
    double cosz = TrigLookUp.cos(radiansz);
    double sinz = TrigLookUp.sin(radiansz);

    return new Matrix44(
      cosy*cosz, sinx*siny*cosz-cosx*sinz, sinx*sinz+cosx*siny*cosz, 0,
      cosy*sinz, cosx*cosz+sinx*siny*sinz, cosx*siny*sinz-sinx*cosz, 0,
          -siny,                sinx*cosy,                cosx*cosy, 0,
              0,                        0,                        0, 1);
  } 

  public static Matrix44 create3dTranslateMatrix(Vector3d v) {
    return create3dTranslateMatrix(v.x(), v.y(), v.z());
  }

  public static Matrix44 create3dTranslateMatrix(double dx, double dy, double dz) {
    return new Matrix44(
      1, 0, 0, dx,
      0, 1, 0, dy,
      0, 0, 1, dz,
      0, 0, 0,  1);
  }

  public static Matrix44 create3dScaleMatrix(double s) {
    return create3dScaleMatrix(s, s, s);
  }

  public static Matrix44 create3dScaleMatrix(double s, Vector3d p) {
    return create3dScaleMatrix(s, s, s, p);
  }

  public static Matrix44 create3dScaleMatrix(double sx, double sy, double sz) {
    return new Matrix44(
      sx,  0,  0, 0,
       0, sy,  0, 0,
       0,  0, sz, 0,
       0,  0,  0, 1);
  }

  public static Matrix44 create3dScaleMatrix(double sx, double sy, double sz, Vector3d p) {
    return new Matrix44(
      sx,  0,  0, p.x()*(1-sx),
       0, sy,  0, p.y()*(1-sy),
       0,  0, sz, p.z()*(1-sz),
       0,  0,  0,           1);
  }
}
