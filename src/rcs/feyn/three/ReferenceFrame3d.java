package rcs.feyn.three;

import rcs.feyn.math.linalg.Basis3d;
import rcs.feyn.math.linalg.Vector3d;

public class ReferenceFrame3d extends Basis3d {
  
  protected final Vector3d sd = i;
  protected final Vector3d up = j;
  protected final Vector3d fw = k;

  public ReferenceFrame3d() {
    super();
  }

  public ReferenceFrame3d(Vector3d side, Vector3d up, Vector3d forward) {
    set(side, up, forward);
  }

  public ReferenceFrame3d(Basis3d b3d) {
    super(b3d);
  }

  public Vector3d getSideVector() {
    return new Vector3d(i);
  }

  public Vector3d getUpVector() {
    return new Vector3d(j);
  }

  public Vector3d getForwardVector() {
    return new Vector3d(k);
  }
}
