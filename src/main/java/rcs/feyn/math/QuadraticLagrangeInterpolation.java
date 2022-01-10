package rcs.feyn.math;

import rcs.feyn.math.Vector2d;

public class QuadraticLagrangeInterpolation {

  private Vector2d a, b, c;  

  private double k1, k2, k3;

  public QuadraticLagrangeInterpolation(Vector2d a, Vector2d b, Vector2d c) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.k1 = (a.x() - b.x()) * (a.x() - c.x());
    this.k2 = (b.x() - a.x()) * (b.x() - c.x());
    this.k3 = (c.x() - a.x()) * (c.x() - b.x());
  }

  public double eval(double x) {
    return 
      a.y() * (((x-b.x()) * (x-c.x())) / k1) + 
      b.y() * (((x-a.x()) * (x-c.x())) / k2) + 
      c.y() * (((x-a.x()) * (x-b.x())) / k3);
  }
}