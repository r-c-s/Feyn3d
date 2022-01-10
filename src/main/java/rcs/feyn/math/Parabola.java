package rcs.feyn.math;

import rcs.feyn.math.LinearEquation.Coefficients3;
import rcs.feyn.math.LinearEquation.EquationWith3Unknows;
import rcs.feyn.math.LinearEquation.SystemOf3EquationsWith3Unknowns;
import rcs.feyn.math.Vector2d;

public class Parabola {

  private double a, b, c;

  public Parabola(Vector2d p1, Vector2d p2, Vector2d p3) {
    this(new SystemOf3EquationsWith3Unknowns(
           new EquationWith3Unknows(p1.x()*p1.x(), p1.x(), 1, p1.y()), 
           new EquationWith3Unknows(p2.x()*p2.x(), p2.x(), 1, p2.y()), 
           new EquationWith3Unknows(p3.x()*p3.x(), p3.x(), 1, p3.y())).getCoefficients());
  }

  public Parabola(Coefficients3 coef) {
    this(coef.getA(), coef.getB(), coef.getC());
  }

  public Parabola(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public double getA() {
    return a;
  }

  public double getB() {
    return b;
  }

  public double getC() {
    return c;
  }

  public double eval(double x) {
    return a*x*x + b*x + c;
  }

  public String toString() {
    return String.format("y = %s*x*x + %s*x + %s", a, b, c);
  }

  public static void main(String[] args) {
    Vector2d p1 = new Vector2d(1,  3);
    Vector2d p2 = new Vector2d(2, -6);
    Vector2d p3 = new Vector2d(4, -9);
    Parabola par = new Parabola(p1, p2, p3);
    System.out.println(par);
    for (int x = 0; x < 10; x++) {
      System.out.println(String.format("f(%s) = ", x) + par.eval(x));
    }
  }
}