package rcs.feyn.math;

import rcs.feyn.utils.Freezable;
import rcs.feyn.utils.ImmutableModificationException;

public class Rational extends Number implements Arithmetical<Rational>, Comparable<Rational>, Freezable<Rational> {
    
  private static final long serialVersionUID = 1L;

  public static final ImmutableRational ZERO = new Rational(0).freeze();
  public static final ImmutableRational ONE = new Rational(1).freeze();
    
  private long nom, den;

  public Rational() {
    this(0, 1);
  }

  public Rational(long nom) {
    this(nom, 1);
  }

  public Rational(int nom) {
    this((long) nom, 1);
  }

  public Rational(int nom, int den) {
    this((long) nom, (long) den);
  }

  public Rational(Rational that) {
    this(that.nom, that.den);
  }

  public Rational(long nom, long den) {
    if (den == 0) {
      throw new IllegalArgumentException("Denominator cannot be zero.");
    }
    
    this.nom = nom;
    this.den = den;
    this.simplify();
  }

  public long getNom() {
    return nom;
  }

  public long getDen() {
    return den;
  }

  public void setNom(long nom) {
    this.nom = nom;
    simplify();
  }

  public void setDen(long den) {
    if (den == 0) {
      throw new IllegalArgumentException("Denominator cannot be zero.");
    }
    
    this.den = den;
    simplify();
  }

  @Override
  public double doubleValue() {
    return (double) (nom / den);
  }

  @Override
  public float floatValue() {
    return (float) (nom / den);
  }

  @Override
  public int intValue() {
    return (int) (nom / den);
  }

  @Override
  public long longValue() {
    return (long) (nom / den);
  }

  @Override
  public Rational add(Rational that) {
    return new Rational(this).addLocal(that);
  }

  @Override
  public Rational sub(Rational that) {
    return new Rational(this).subLocal(that);
  }

  @Override
  public Rational mul(Rational that) {
    return new Rational(this).mulLocal(that);
  }

  @Override
  public Rational div(Rational that) {
    return new Rational(this).divLocal(that);
  }
  
  public Rational addLocal(Rational that) {
    long lcm = MathUtils.lcm(this.den, that.den);
    
    this.nom = this.nom * (lcm / this.den) + that.nom * (lcm / that.den);
    this.den = lcm;
    
    return this;
  }

  public Rational subLocal(Rational that) {
    long lcm = MathUtils.lcm(this.den, that.den);
    
    this.nom = this.nom * (lcm / this.den) - that.nom * (lcm / that.den);
    this.den = lcm;
    
    return this;
  }

  public Rational mulLocal(Rational that) {
    this.nom *= that.nom;
    this.den *= that.den;
    
    return this;
  }

  public Rational divLocal(Rational that) {
    this.nom *= that.den;
    this.den *= that.nom;
    
    return this;
  }

  public Rational reciprocal() {
    return new Rational(this.den, this.nom);
  }

  private Rational simplify() {
    long gcd = MathUtils.gcd(nom, den);
    nom /= gcd;
    den /= gcd;
    if (den < 0) {
      nom *= -1;
      den *= -1;
    }
    return this;
  }

  @Override
  public int compareTo(Rational that) {
    if (this.nom == that.nom && this.den == that.den)
        return  0;
    if (this.doubleValue() > that.doubleValue())      
        return  1;
    else                                              
        return -1;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result += 31 * result + Long.valueOf(nom).hashCode();
    result += 31 * result + Long.valueOf(den).hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Rational)) {
      return false;
    }
    
    Rational that = (Rational) obj;
    
    return this.nom == that.nom
        && this.den == that.den;
  }

  @Override
  public String toString() {
    if (this.nom == 0) return "0";
    if (this.den == 1) return this.nom + "";
    return String.format("%d/%d", this.nom, this.den);
  }

  public static Rational parseRational(String str) {
    String[] nomdem = str.split("/");

    if (nomdem.length == 2) {
      return new Rational(Long.parseLong(nomdem[0]), Long.parseLong(nomdem[1]));
    }
    if (nomdem.length == 1) {
      return new Rational(Long.parseLong(nomdem[0]));
    }
    else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public ImmutableRational freeze() {
    return new ImmutableRational(this);
  }

  public static final class ImmutableRational extends Rational { 
    private static final long serialVersionUID = 1L;

    public ImmutableRational(Rational that) {
      super(that);
    }
    
    public void setNom(long nom) {
      throw new ImmutableModificationException(this);
    }
    
    public void setDen(long den) {
      throw new ImmutableModificationException(this);
    }
    
    public Rational addLocal(Rational that) {
      throw new ImmutableModificationException(this);
    }
    
    public Rational subLocal(Rational that) {
      throw new ImmutableModificationException(this);
    }
    
    public Rational mulLocal(Rational that) {
      throw new ImmutableModificationException(this);
    }
    
    public Rational divLocal(Rational that) {
      throw new ImmutableModificationException(this);
    } 
  }
}
