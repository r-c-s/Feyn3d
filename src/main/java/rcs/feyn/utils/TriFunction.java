package rcs.feyn.utils;

@FunctionalInterface
public interface TriFunction<W, X, Y, Z> {
  
  public Z apply(W w, X x, Y y);

}
