package rcs.feyn.utils;


public class ImmutableModificationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ImmutableModificationException() { 
    super(); 
  }

  public ImmutableModificationException(String message) { 
    super(message); 
  }

  public ImmutableModificationException(String message, Throwable cause) { 
    super(message, cause); 
  }

  public ImmutableModificationException(Throwable cause) { 
    super(cause); 
  }

  public ImmutableModificationException(Freezable<?> frozen) {
    this("Cannot mutate immutable object: " + frozen.getClass().getName());
  }
}
