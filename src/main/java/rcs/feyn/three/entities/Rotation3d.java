package rcs.feyn.three.entities;

import rcs.feyn.math.Matrices;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Rotation3d {
  
  private Vector3d center;
  private Vector3d axis;
  private double radians;
  
  public Rotation3d(Vector3d center, Vector3d axis, double radians) {
    this.center = center;
    this.axis = axis;
    this.radians = radians;
  }

  public Matrix44 getMatrix() {
    return Matrices.create3dRotateMatrix(center, axis, radians);
  }
  
  public void setCenter(Vector3d center) {
    this.center = new Vector3d(center);
  }
}
