package rcs.feyn.three.view;

import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.geo.Plane3d;
import rcs.feyn.math.MathConsts;

public class ViewFrustum3d {
  
  public static final double FOV = 45.0 * MathConsts.DEGREES_TO_RADIANS;
  
  protected final Plane3d L = Plane3d.xzPlane(); //left
  protected final Plane3d R = Plane3d.zyPlane(); //right
  protected final Plane3d T = Plane3d.yzPlane(); //top
  protected final Plane3d B = Plane3d.zxPlane(); //bottom
  protected final Plane3d N = Plane3d.yxPlane(); //near
  protected final Plane3d F = Plane3d.xyPlane(); //far 

  protected final Plane3d[] planes = new Plane3d[]{ T, R, B, L, N, F };
  
  protected double width, height;
  protected double nearDist, farDist;

  public ViewFrustum3d(double width, double height, double nearDist, double farDist) {
    setWidth(width);
    setHeight(height);
    setNearDistance(nearDist);
    setFarDistance(farDist);

    L.setNormal(Vector3d.X_AXIS.rotate(Vector3d.Y_AXIS, FOV));
    R.setNormal(Vector3d.NEG_X_AXIS.rotate(Vector3d.NEG_Y_AXIS, FOV));
  }

  public double getWidthAt(double z) {
    return z * 2 * TrigLookUp.tan(FOV);
  }

  public double getHeightAt(double z) {
    return z * 2 * height / width;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;

    double radians = Math.atan(height / width);
    T.setNormal(Vector3d.NEG_Y_AXIS.rotate(Vector3d.X_AXIS, radians));
    B.setNormal(Vector3d.Y_AXIS.rotate(Vector3d.NEG_X_AXIS, radians));
  }

  public double getNearDistance() {
    return nearDist;
  }

  public void setNearDistance(double nearDist) {
    this.nearDist = Math.abs(nearDist);
    N.setOrigin(new Vector3d(0, 0, -this.nearDist));
  }

  public double getFarDistance() {
    return farDist;
  } 

  public void setFarDistance(double farDist) {
    this.farDist = Math.abs(farDist);
    F.setOrigin(new Vector3d(0, 0, -this.farDist));
  }

  public boolean triviallyNotVisible(Vector3d[] vertices) {
    for (Plane3d plane : planes) {
      boolean allAreNotVisible = true;
      for (Vector3d vertex : vertices) {
        allAreNotVisible &= plane.signedDistance(vertex) < 0;
      }
      if (allAreNotVisible) {
        return true;
      }
    }
    return false;
  }
  
  public Vector3d[] clipToNearPlane(Vector3d[] vertices) {  
    return GeoUtils3d.clipToPlane(N, vertices);  
  } 

  public Vector3d[][] clipToNearPlane(Vector3d[] vertices, Vector3d[] normals) {  
    return GeoUtils3d.clipToPlane(N, vertices, normals); 
  }

  public Vector3d[] clipToFrustum(Vector3d[] vertices) {
    for (int i = 0; i < planes.length && vertices.length > 0; i++) {
      vertices = GeoUtils3d.clipToPlane(planes[i], vertices);
    }
    return vertices;
  }

  public Vector3d[][] clipToFrustum(Vector3d[] vertices, Vector3d[] normals) {
    Vector3d[] clippedVertices = vertices;
    Vector3d[] clippedNormals = normals;
    for (int i = 0; i < planes.length && vertices.length > 0; i++) {
      Vector3d[][] vn = GeoUtils3d.clipToPlane(planes[i], clippedVertices, clippedNormals);
      clippedVertices = vn[0];
      clippedNormals = vn[1];
    }
    return new Vector3d[][]{ clippedVertices, clippedNormals };
  } 
}