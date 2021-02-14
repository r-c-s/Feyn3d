package rcs.feyn.utils;

@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {
  
  public void accept(T t, U u, V v, W w);

}
