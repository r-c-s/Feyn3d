package rcs.feyn.three.view;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class ViewUtils {

  public static boolean isBackFace(Vector3d viewer, Vector3d viewee, Vector3d normal) {
    return viewer.sub(viewee).dotProd(normal) < 0;
  }

  public static Matrix44 getViewSpaceMatrix(Vector3d position, Vector3d i, Vector3d j, Vector3d k) {
    return new Matrix44(
       i.x(),  i.y(),  i.z(), -i.dotProd(position), 
       j.x(),  j.y(),  j.z(), -j.dotProd(position), 
       k.x(),  k.y(),  k.z(), -k.dotProd(position), 
           0,      0,      0,                   1);
  }

  public static Matrix44 getPerspectiveProjectionMatrix(ViewFrustum3d frustum) {
    double n = frustum.getNearDistance();
    double f = frustum.getFarDistance();
    double r = frustum.getWidthAt(n) / 2.0;
    double t = frustum.getHeightAt(n) / 2.0;
    
    return new Matrix44(
      n/r,   0,            0,            0,
        0, n/t,            0,            0,
        0,   0, -(f+n)/(f-n), -(f*n)/(f-n),
        0,   0,           -1,            0);
  }

  public static Matrix44 getOrthographicProjectionMatrix(ViewFrustum3d frustum) {
    double n = frustum.getNearDistance();
    double f = frustum.getFarDistance();
    double w = frustum.getWidth();
    double h = frustum.getHeight();
    
    return new Matrix44(
      1/w,   0,       0,           0,
        0, 1/h,       0,           0,
        0,   0, 2/(f-n), (f+n)/(f-n),
        0,   0,       0,           1);
  }

  public static Matrix44 getViewPortMatrix(ViewFrustum3d frustum) {
    double w = frustum.getWidth();
    double h = frustum.getHeight();
    
    return new Matrix44(
      w/2,    0, 0, w/2,
        0, -h/2, 0, h/2,
        0,    0, 1,   0,
        0,    0, 0,   1);
  }
}
