package rcs.feyn.struct;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import rcs.feyn.utils.QuadConsumer;

public abstract class FeynCollection<T extends FeyngGarbageCollectable> implements Iterable<T> { 

  public abstract void add(T t);

  public abstract void clear();

  public abstract int  size();

  @Override
  public void forEach(Consumer<? super T> op) {
    forEachWithIndex((t, i) -> {
      op.accept(t);
    });    
  }

  public void forEachWithIndex(BiConsumer<? super T, Integer> op) { 
    int i = 0;
    for (Iterator<? extends T> it = this.iterator(); it.hasNext(); i++) {
      T t = it.next();
      
      op.accept(t, i);
      
      if (t.isDestroyed()) { 
        it.remove();
        break;
      }
    }
  }

  public void forEachPair(BiConsumer<? super T, ? super T> op) {
    forEachPairWithIndex((t, i, u, j) -> {
      op.accept(t, u);
    });    
  }

  public void forEachPairWithIndex(QuadConsumer<? super T, Integer, ? super T, Integer> op) {
    int i = 0;
    for (Iterator<? extends T> itA = iterator(); itA.hasNext(); i++) {
      T a = itA.next();
      int j = 0;
      for (Iterator<? extends T> itB = iterator(); itB.hasNext(); j++) {
        T b = itB.next();
        
        if (j <= i) {
          continue;
        } 
         
        op.accept(a, i, b, j);
        
        if (a.isDestroyed()) {
          itA.remove();
          break;
        }
        if (b.isDestroyed()) {
          itB.remove();
        }
      }
    }      
  }
}
