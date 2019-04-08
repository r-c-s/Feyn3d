package rcs.feyn.two;

import rcs.feyn.math.linalg.Basis2d;
import rcs.feyn.math.linalg.Vector2d;

public class ReferenceFrame2d extends Basis2d {

  protected final Vector2d sd = i; 
  protected final Vector2d up = j;

  public ReferenceFrame2d() {
    super();
  }

  public ReferenceFrame2d(Vector2d x, Vector2d y) {
    set(x, y);
  }

  public ReferenceFrame2d(ReferenceFrame2d rf2d) {
    set(rf2d.i, rf2d.j);
  }

  public Vector2d getSideVector() {
    return new Vector2d(i);
  }

  public Vector2d getUpVector() {
    return new Vector2d(j);
  }
}