package rcs.feyn.utils;

public interface QuadConsumer<T, U, V, W> {
  
  public void accept(T t, U u, V v, W w);

}
