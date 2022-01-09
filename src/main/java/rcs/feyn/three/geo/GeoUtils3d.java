package rcs.feyn.three.geo;

import rcs.feyn.math.MathConsts;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector2d;
import rcs.feyn.math.Vector3d;

public class GeoUtils3d {
  
  public static final double COPLANAR_ERROR_THRESHOLD = 1 - MathConsts.EPSILON_0_001;
  public static final double DEFAULT_CIRCLE_SIDES_ERROR_THRESHOLD = MathConsts.EPSILON_0_001;

  private GeoUtils3d() {
    throw new AssertionError();
  }

  public static final void validatePolygon3d(Vector3d... v) {
    int size = v.length;
    
    if (size == 3) {
      return;
    }
    if (size <  3) {
      throw new IllegalArgumentException("Polygon3d must have 3 or more vertices.");
    }
    
    Vector3d first = v[0]
            .sub(v[2])
            .crossProd(v[1].sub(v[2]))
            .normalizeLocal();
    
    for (int i = 1; i < size; i++) {
      Vector3d current = v[i]
              .sub(v[(i+2)%size])
              .crossProd(v[(i+1)%size].sub(v[(i+2)%size]))
              .normalizeLocal();
      
      double dotProd = current.dotProd(first);
      if (dotProd < 0) {
        throw new IllegalArgumentException("Polygon3d cannot be concave.");
      }
      if (dotProd < COPLANAR_ERROR_THRESHOLD) {
        throw new IllegalArgumentException("Polygon3d must be coplanar.");
      }
    }
  }

  public static final Vector2d[][] triangulate(Vector2d... v) {
    int size = v.length; 
    
    if (size < 3) {
      throw new IllegalArgumentException("Polygon3d must have 3 or more vertices.");
    }
    
    int numTriangles = size - 2;
    
    Vector2d[][] triangles = new Vector2d[numTriangles][3];
    
    for (int i = 0; i < numTriangles; i++) {
      triangles[i] = new Vector2d[]{v[i+1], v[i+2], v[0]};
    }
    
    return triangles;
  }

  public static final Vector3d[][] triangulate(Vector3d... v) {
    int size = v.length; 
    
    if (size < 3) {
      throw new IllegalArgumentException("Polygon3d must have 3 or more vertices.");
    }
    
    int numTriangles = size - 2;
    
    Vector3d[][] triangles = new Vector3d[numTriangles][3];
    
    for (int i = 0; i < numTriangles; i++) {
      triangles[i] = new Vector3d[]{v[i+1], v[i+2], v[0]};
    }
    
    return triangles;
  }

  public static final double[][] triangulate(double... d) {
    int size = d.length; 
    
    if (size < 3) {
      throw new IllegalArgumentException("Polygon3d must have 3 or more vertices.");
    }
    
    int numTriangles = size - 2;
    
    double[][] triangles = new double[numTriangles][3];
    
    for (int i = 0; i < numTriangles; i++) {
      triangles[i] = new double[]{d[i+1], d[i+2], d[0]};
    }
    
    return triangles;
  }

  public static final int[][] triangulate(int... d) {
    int size = d.length; 
    
    if (size < 3) {
      throw new IllegalArgumentException("Polygon3d must have 3 or more vertices.");
    }
    
    int numTriangles = size - 2;
    
    int[][] triangles = new int[numTriangles][3];
    
    for (int i = 0; i < numTriangles; i++) {
      triangles[i] = new int[]{d[i+1], d[i+2], d[0]};
    }
    
    return triangles;
  }

  public static final boolean areCoplanar(Vector3d... v) {
    int size = v.length;
    if (size <= 3) {
      return true;
    }
    
    Vector3d first = v[0]
            .sub(v[2])
            .crossProd(v[1].sub(v[2]))
            .normalizeLocal();
    
    for (int i = 1; i < size; i++) {
      Vector3d current = v[i]
              .sub(v[(i+2)%size])
              .crossProd(v[(i+1)%size].sub(v[(i+2)%size]))
              .normalize();
      
      if (Math.abs(current.dotProd(first)) < COPLANAR_ERROR_THRESHOLD) {
        return false;
      }
    }
    return true;
  }

  public static final boolean isConvex(Vector3d... v) {
    int size = v.length;
    if (size == 3) {
      return true;
    }
    if (size < 3) {
      throw new IllegalArgumentException();
    }
    Vector3d first = v[0]
            .sub(v[2])
            .crossProd(v[1].sub(v[2]));
    
    for (int i = 1; i < size; i++) {
      Vector3d current = v[i]
              .sub(v[(i+2)%size])
              .crossProd(v[(i+1)%size].sub(v[(i+2)%size]));
      if (current.dotProd(first) < 0) {
        return false;
      }
    }
    return true;
  } 

  public static Vector3d getNormal(Vector3d... v) {
    return v[1]
            .sub(v[0])
            .crossProd(v[2].sub(v[0]))
            .normalizeLocal();
  } 
  
  public static Vector3d getCenter(Vector3d... v) {
    int N = v.length; 
    Vector3d sum = new Vector3d();
    for (int i = 0; i < N; i++) {
      sum.addLocal(v[i]);
    }
    return sum.divLocal(N);
  }
  
  public static final int getNumberOfSidesOfCircle(double radius) {
    return getNumberOfSidesOfCircle(radius, DEFAULT_CIRCLE_SIDES_ERROR_THRESHOLD);
  }

  public static final int getNumberOfSidesOfCircle(double radius, double error) {
    return (int) Math.ceil(MathConsts.TWO_PI / Math.acos(2 * MathUtils.squared(1 - error / radius) - 1));
  }
  
  public static final Vector3d[] clipToPlane(Plane3d plane, Vector3d[] vertices) {
    int size = vertices.length;
    
    double[] signedDistances = new double[size];
    int numberVisible = 0;
    for (int i = 0; i < size; i++) {
      signedDistances[i] = plane.signedDistance(vertices[i]);
      numberVisible += signedDistances[i] > 0 ? 1 : 0;
    }
    
    if (numberVisible == 0) {
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

  public static final Vector3d[][] clipToPlane(Plane3d plane, Vector3d[] vertices, Vector3d[] normals) {    
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
