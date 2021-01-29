package rcs.feyn.utils.struct;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class NaturalOrder<T> implements Comparator<T> {
  
  @Override
  public int compare (T ta, T tb) {
    if (!(ta instanceof Comparable<?>)) {
      throw new UnsupportedOperationException();
    }
    return ((Comparable<T>) ta).compareTo(tb);
  }
}
