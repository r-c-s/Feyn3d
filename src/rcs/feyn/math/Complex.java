package rcs.feyn.math;

import rcs.feyn.utils.Freezable;
import rcs.feyn.utils.ImmutableModificationException;

 
public class Complex implements Arithmetical<Complex>, Freezable<Complex> {
  
  public static final ImmutableComplex I    = new Complex(0, 1).freeze();
  public static final ImmutableComplex ZERO = new Complex(0, 0).freeze();
  public static final ImmutableComplex ONE  = new Complex(1, 0).freeze();
    
  private double real;
  private double imag; 

  public Complex(double real, double imag) {
    this.real = real;
    this.imag = imag;
  }

  public Complex(Complex that) {
    this(that.real, that.imag);
  }

  public double getReal() {
    return this.real;
  }

  public double getImaginary() {
    return this.imag;
  }

  public void setReal(double real) {
    this.real = real;
  }

  public void setImaginary(double imag) {
    this.imag = imag;
  }

  public Complex complexConjugate() {
    return new Complex(this.real, 
                      -this.imag);
  }

  @Override
  public Complex add(Complex that) {
    return new Complex(this).addLocal(that);
  }

  @Override
  public Complex sub(Complex that) {
    return new Complex(this).subLocal(that);
  }

  @Override
  public Complex mul(Complex that) { //foil
    return new Complex(this).mulLocal(that);
  }

  @Override
  public Complex div(Complex that) {
    return new Complex(this).divLocal(that);
  }

  public Complex addLocal(Complex that) {
    this.real += that.real;
    this.imag += that.imag;
    
    return this;
  }

  public Complex subLocal(Complex that) {
    this.real -= that.real; 
    this.imag -= that.imag;
    
    return this;
  }

  public Complex mulLocal(Complex that) { //foil
    this.real = (this.real * that.real) - (this.imag * that.imag);
    this.imag = (this.real * that.imag) + (this.imag * that.real);
    
    return this;
  }

  public Complex divLocal(Complex that) {
    Complex cj  = that.complexConjugate();
    Complex nom = this.mul(cj);
    Complex den = that.mul(cj);
    Complex res = nom.mul(new Complex(1/den.getReal(), 0));
    
    this.real = res.real;
    this.imag = res.imag;
    
    return this;
  } 

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + MathUtils.hashDouble(real);
    result = 31 * result + MathUtils.hashDouble(imag);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Complex)) {
      return false;
    }
    
    Complex that = (Complex) obj;
    
    return this.real == that.real 
        && this.imag == that.imag;
  }

  @Override
  public String toString() {
    return String.format("%s%s", 
        this.real != 0 ?  this.real : "",
        this.imag != 0 ? (this.imag > 0 ? "+" : "") + this.imag + "i" : "");
  }

  public static Complex[] rootsOfUnity(int n) {
    assert(MathUtils.isPowerOfTwo(n));
    
    Complex[] roots = new Complex[n]; 
    double rad = MathConsts.TWO_PI/n;
    
    for (int i = 0; i < n; i++) {
      roots[i] = new Complex(Math.cos(i*rad), Math.sin(i*rad));
    }
    
    return roots;
  }

  @Override
  public ImmutableComplex freeze() {
    return new ImmutableComplex(this);
  }

  public static final class ImmutableComplex extends Complex { 
    public ImmutableComplex(Complex that) {
      super(that);
    }
    
    public void setReal(double real) throws ImmutableModificationException {
      throw new ImmutableModificationException(this);
    }
    
    public void setImaginary(double imag) throws ImmutableModificationException {
      throw new ImmutableModificationException(this);
    }
    
    public Complex addLocal(Complex that) throws ImmutableModificationException {
      throw new ImmutableModificationException(this);
    }
    
    public Complex subLocal(Complex that) throws ImmutableModificationException {
      throw new ImmutableModificationException(this);
    }
    
    public Complex mulLocal(Complex that) throws ImmutableModificationException { 
      throw new ImmutableModificationException(this);
    }
    
    public Complex divLocal(Complex that) throws ImmutableModificationException {
      throw new ImmutableModificationException(this);
    }  
  }
}
