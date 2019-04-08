package rcs.feyn.utils;

import java.util.Iterator;

public class PeekingIterator<T> implements Iterator<T> { 

  private Iterator<T> it;
  private T curr;

  public PeekingIterator(Iterator<T> it) {
   this.it = it;
  }

  @Override
  public boolean hasNext() {
    return it.hasNext() || curr != null;
  } 

  @Override
  public T next() {
    if (!hasNext()) {
      return null;
    }
    if (curr != null) {
      T temp = curr; 
      curr = null;
      return temp;
    }
    curr = null;  
    return it.next();
  }

  public T peek() { 
    if (!hasNext()) {
      return null;
    }
    if (curr == null) {
      curr = it.next();
    }
    return curr;
  }
}
