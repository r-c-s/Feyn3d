package rcs.feyn.three.view;

import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.geo.Plane3d;
import rcs.feyn.math.MathConsts;

public class ViewFrustum3d {
  
  public static final double FOV = 45.0 * MathConsts.DEGREES_TO_RADIANS;
  
  protected final Plane3d T = Plane3d.yzPlane(); //top
  protected final Plane3d R = Plane3d.zyPlane(); //right
  protected final Plane3d B = Plane3d.zxPlane(); //bottom
  protected final Plane3d L = Plane3d.xzPlane(); //left
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

  public boolean triviallyNotVisible(Vector3d... vertices) {
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

  public boolean containsAtLeastOne(Vector3d... vertices) {
    for (Vector3d vertex : vertices) {
      if (contains(vertex)) {
        return true;
      }
    }
    return false;
  }

  public boolean containsAll(Vector3d... vertices) {
    for (Vector3d vertex : vertices) {
      if (!contains(vertex)) {
        return false;
      }
    }
    return true;
  }

  public boolean contains(Vector3d vertex) {
    for (Plane3d plane : planes) {
      if (plane.signedDistance(vertex) < 0) {
        return false;
      }
    }
    return true;
  }

  public Vector3d[] clipToNearPlane(Vector3d[] vertices) {
    return clipToPlane(N, vertices);
  }

  public Vector3d[][] clipToNearPlane(Vector3d[] vertices, Vector3d[] normals) {
    return clipToPlane(N, vertices, normals);
  }

  public Vector3d[] clipToFrustum(Vector3d[] vertices) {
    for (int i = 0; i < planes.length && vertices.length > 0; i++) {
      vertices = clipToPlane(planes[i], vertices);
    }
    return vertices;
  }

  public Vector3d[][] clipToFrustum(Vector3d[] vertices, Vector3d[] normals) {
    for (int i = 0; i < planes.length && vertices.length > 0; i++) {
      Vector3d[][] vn = clipToPlane(planes[i], vertices, normals);
      vertices = vn[0];
      normals  = vn[1];
    }
    return new Vector3d[][]{ vertices, normals };
  } 

  private static final Vector3d[] clipToPlane(Plane3d plane, Vector3d[] vertices) {
    int size = vertices.length;
    
    double[] signedDistances = new double[size];
    int numberVisible = 0;
    for (int i = 0; i < size; i++) {
      signedDistances[i] = plane.signedDistance(vertices[i]);
      numberVisible += signedDistances[i] > 0 ? 1 : 0;
    }
    
    if (numberVisible ==    0) {
      return new Vector3d[]{};
    }
    if (numberVisible == size) {
      return vertices;
    }
    
    int clippedSize = 2 + (size == 2 ? 0 : numberVisible);    
    Vector3d[] clipped = new Vector3d[clippedSize];
    for (int i = 0, j = 1, k = 0; i < size; i++, j++, j%=size) {
      double sda = signedDistances[i];
      double sdb = signedDistances[j];
      
      if (sda < 0 && sdb < 0) {
        continue;
      }
      
      Vector3d a = vertices[i];
      Vector3d b = vertices[j];
      
      if (sda > 0) {
        clipped[k++] = a;
        if (sdb < 0 && size > 2) { 
          clipped[k++] = fastIntersection(a, b, sda, sdb);
        }
      } else if (sdb > 0) {
        clipped[k++] = fastIntersection(a, b, sda, sdb);
      }
    }
    return clipped;
  } 

  private static final Vector3d[][] clipToPlane(Plane3d plane, Vector3d[] vertices, Vector3d[] normals) {    
    int size = vertices.length;
    
    double[] signedDistances = new double[size];
    int numberVisible = 0;
    for (int i = 0; i < size; i++) {
      signedDistances[i] = plane.signedDistance(vertices[i]);
      numberVisible += signedDistances[i] > 0 ? 1 : 0;
    }
    
    if (numberVisible == 0) {
      return new Vector3d[][]{ new Vector3d[]{}, new Vector3d[]{} };
    }
    if (numberVisible == size) {
      return new Vector3d[][]{ vertices, normals };
    }

    int clippedSize = 2 + (size == 2 ? 0 : numberVisible);  
    Vector3d[] clippedv = new Vector3d[clippedSize];
    Vector3d[] clippedn = new Vector3d[clippedSize];
    for (int i = 0, j = 1, k = 0; i < size; i++, j++, j%=size) {
      double sda = signedDistances[i];
      double sdb = signedDistances[j];
      
      if (sda < 0 && sdb < 0) {
        continue;
      }
      
      Vector3d va = vertices[i];
      Vector3d vb = vertices[j];

      Vector3d na = normals[i];
      Vector3d nb = normals[j];
      
      if (sda > 0) {
        clippedv[k  ] = va;
        clippedn[k++] = na;
        if (sdb < 0 && size > 2) { 
          clippedv[k  ] = fastIntersection(va, vb, sda, sdb);
          clippedn[k++] = interpolateNormal(na, nb, sda, sdb); 
        }
      } else if (sdb > 0) {
        clippedv[k  ] = fastIntersection(va, vb, sda, sdb);
        clippedn[k++] = interpolateNormal(na, nb, sda, sdb);
      }
    }
    return new Vector3d[][]{ clippedv, clippedn };
  }

  private static final Vector3d interpolateNormal(Vector3d a, Vector3d b, double sda, double sdb) {
    sda = Math.abs(sda);
    sdb = Math.abs(sdb); 
    return a.mul(sdb/(sda+sdb)).addLocal(b.mul(sda/(sda+sdb))).normalizeLocal();
  }

  private static final Vector3d fastIntersection(Vector3d a, Vector3d b, double sda, double sdb) {
    return a.add(b.sub(a).mul(sda/(sda-sdb)));
  }
}