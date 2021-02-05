package rcs.feyn.utils.struct;

public interface FeynGarbageCollectable {
  
  public void destroy();
  
  public boolean isDestroyed();
}
