package rcs.feyn.three.geo;

import rcs.feyn.math.MathConsts;
import rcs.feyn.math.linalg.Vector3d;

public class GeoUtils3d {
  
  public static final double COPLANAR_ERROR_THRESHOLD = 1 - MathConsts.EPSILON_0_001;

  private GeoUtils3d() {
    throw new AssertionError();
  }

  public static final void validatePolygon3d(Vector3d... v) {
    int size = v.length;
    
    if (size == 3) {
      return;
    }
    if (size <  3) {
      throw new IllegalArgumentException("Error: polygon3d must have 3 or more vertices.");
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
        throw new IllegalArgumentException("Error: polygon3d cannot be concave.");
      }
      if (dotProd < COPLANAR_ERROR_THRESHOLD) {
        throw new IllegalArgumentException("Error: polygon3d must be coplanar.");
      }
    }
  }

  public static final Vector3d[][] triangulate(Vector3d... v) {
    int size = v.length; 
    
    if (size < 3) {
      throw new IllegalArgumentException("Error: polygon3d must have 3 or more vertices.");
    } 
    
    int numTriangles = size - 2;
    
    Vector3d[][] triangles = new Vector3d[numTriangles][3];
    
    for (int i = 0; i < numTriangles; i++) {
      triangles[i] = new Vector3d[]{v[i+1], v[i+2], v[0]};
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
}
