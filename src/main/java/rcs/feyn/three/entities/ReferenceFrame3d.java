package rcs.feyn.three.entities;

import rcs.feyn.math.Basis3d;
import rcs.feyn.math.Vector3d;

public class ReferenceFrame3d extends Basis3d {
  
  // reassign simply to rename
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
    return new Vector3d(sd);
  }

  public Vector3d getUpVector() {
    return new Vector3d(up);
  }

  public Vector3d getForwardVector() {
    return new Vector3d(fw);
  }
}
