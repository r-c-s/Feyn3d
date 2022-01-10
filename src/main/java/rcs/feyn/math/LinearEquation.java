package rcs.feyn.math;

public class LinearEquation {

  private LinearEquation() {
    throw new AssertionError();
  } 

  public static class EquationWith3Unknows {
    private double a, b, c, d;

    public EquationWith3Unknows(double a, double b, double c, double d) {
      this.a = a; 
      this.b = b; 
      this.c = c; 
      this.d = d;
    }

    public boolean check(Coefficients3 coef) {
      return MathUtils.epsilonEquals(a*coef.a + b*coef.b + c*coef.c, d);
    }

    public String toString() {
      return String.format("%s*a + %s*b + %s*c = %s", a, b, c, d);
    }
  }

  public static class Coefficients3 {
    private double a, b, c;

    public Coefficients3(double a, double b, double c) {
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

    public String toString() {
      return String.format("[a=%s, b=%s, c=%s]", a, b, c);
    }
  }

  public static class SystemOf3EquationsWith3Unknowns {
    private EquationWith3Unknows eq1, eq2, eq3;
    private Coefficients3 coef;

    public SystemOf3EquationsWith3Unknowns(EquationWith3Unknows eq1, EquationWith3Unknows eq2, EquationWith3Unknows eq3) {
      this.eq1 = eq1; this.eq2 = eq2; this.eq3 = eq3;
    }

    public Coefficients3 getCoefficients() {
      if (coef != null) {
        return coef;
      }

      double det = MathUtils.determinant(
              eq1.a, eq1.b, eq1.c,
              eq2.a, eq2.b, eq2.c,
              eq3.a, eq3.b, eq3.c);

      double x = MathUtils.determinant(
              eq1.d, eq1.b, eq1.c,
              eq2.d, eq2.b, eq2.c,
              eq3.d, eq3.b, eq3.c) / det;

      double y = MathUtils.determinant(
              eq1.a, eq1.d, eq1.c,
              eq2.a, eq2.d, eq2.c,
              eq3.a, eq3.d, eq3.c) / det;

      double z = -(eq1.a*x + eq1.b*y - eq1.d) / eq1.c;

      coef = new Coefficients3(x, y, z);

      return check() ? coef : null;
    }

    public boolean check() {
      return eq1.check(coef) && eq2.check(coef) && eq3.check(coef);
    }

    public String toString() {
      return String.format("eq1 : %s\neq2 : %s\neq3 : %s\ncoef: %s", eq1, eq2, eq3, coef);
    }
  }

  public static void main(String[] args) {
    EquationWith3Unknows one = new EquationWith3Unknows(1,  3,  1,  4);
    EquationWith3Unknows two = new EquationWith3Unknows(2, -6, -3, 10);
    EquationWith3Unknows three = new EquationWith3Unknows(4, -9,  3,  4);
    SystemOf3EquationsWith3Unknowns sys = new SystemOf3EquationsWith3Unknowns(one, two, three);
    sys.getCoefficients();
    System.out.println(sys);
  }
}