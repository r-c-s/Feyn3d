package rcs.feyn.utils;


public interface TriFunction<W, X, Y, Z> {
  
  public Z apply(W w, X x, Y y);

}
