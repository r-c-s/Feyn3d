package rcs.feyn.physics;

import rcs.feyn.math.Vector3d;

public final class PhysicsUtils {

  private PhysicsUtils() {
    throw new AssertionError();
  }
  
  public static Vector3d centerOfMass(Vector3d[] points, double[] mass) {
    int N = points.length;
    if (N != mass.length) {
      throw new IllegalArgumentException("Error: Vector3d array and double array must be the same size.");
    }
    Vector3d sum = new Vector3d();
    double M = 0; 
    for (int i = 0; i < N; i++) {
      sum.addLocal(points[i].mul(mass[i]));
      M += mass[i];
    }
    return sum.divLocal(M);
  }
}
