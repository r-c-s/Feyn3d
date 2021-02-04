package rcs.feyn.three.geo;

import rcs.feyn.math.linalg.Matrix44;

public interface Transformable3d {
  
  public void transform(Matrix44 transform);
  
}
