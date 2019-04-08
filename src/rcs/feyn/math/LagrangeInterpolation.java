package rcs.feyn.math;

import rcs.feyn.math.linalg.Vector2d;

public class LagrangeInterpolation implements Function {
  
  private Vector2d[] points;
  private double[] coefficients;
  
  public LagrangeInterpolation(Vector2d[] points) {
    this.points = points;
    this.coefficients = new double[points.length];
    
    for (int i = 0; i < points.length; i++) {
      double coef = points[i].y();
      for (int j = 0; j < points.length; j++) {
        if (i == j) {
          continue;
        }
        coef /= (points[i].x() - points[j].x());
      }
      coefficients[i] = coef;
    }
  }
  
  public double eval(double x) {
    double y = 0;
    for (int i = 0; i < points.length; i++) {
      double prod = 1;
      for (int j = 0; j < points.length; j++) {
        if (i == j) {
          continue;
        }
        prod *= (x - points[j].x());
      }
      y += coefficients[i] * prod;
    }
    return y;
  }
}
