package rcs.feyn.utils.struct;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import rcs.feyn.utils.QuadConsumer;

public abstract class FeynCollection<T extends FeynGarbageCollectable> implements Iterable<T> { 

  public abstract void add(T t);

  public abstract void clear();

  public abstract int  size();
  
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public void forEach(Consumer<? super T> op) {
    forEachWithIndex((t, i) -> {
      op.accept(t);
    });    
  }

  public void forEachWithIndex(BiConsumer<? super T, Integer> op) { 
    int i = 0;
    for (var it = this.iterator(); it.hasNext(); i++) {
      T t = it.next();
      
      op.accept(t, i);
      
      if (t.isDestroyed()) {
        it.remove();
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
    for (var itA = iterator(); itA.hasNext(); i++) {
      T a = itA.next();
      int j = 0;
      for (var itB = iterator(); itB.hasNext(); j++) {
        T b = itB.next();
        
        // this pair has already been processed but in a different order, so skip
        if (j <= i) {
          continue;
        } 
         
        op.accept(a, i, b, j);
        
        if (a.isDestroyed()) {
          itA.remove();
        }
        if (b.isDestroyed()) {
          itB.remove();
        }
      }
    }      
  }
}
