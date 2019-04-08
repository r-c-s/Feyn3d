package rcs.feyn.math;

public interface MutableArithmetical<T> extends Arithmetical<T> {
  
  public T addLocal(T t);
  public T subLocal(T t);
  public T mulLocal(T t);
  public T divLocal(T t);

}
