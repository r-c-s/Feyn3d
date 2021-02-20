package rcs.feyn.utils;


public class ImmutableModificationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ImmutableModificationException(Freezable<?> frozen) {
    super("Cannot mutate immutable object: " + frozen.getClass().getName());
  }
}
