package rcs.feyn.utils;

public interface HexaConsumer<T, U, V, W, X, Y> {
  
  public void accept(T t, U u, V v, W w, X x, Y y);

}
