package rcs.feyn.utils;

public abstract class RcsObject {
  
  /**
   * Forces overwriting of hashCode method
   */ 
  @Override
  public abstract int hashCode();
  
  /**
   * Forces overwriting of equals method
   */ 
  @Override
  public abstract boolean equals(Object obj);
  
  /**
   * Forces overwriting of toString method
   */ 
  @Override
  public abstract String toString();

  /**
   * Preserves default toString method
   */ 
  public String toHashString() {
    return super.toString();
  }
}

