package rcs.feyn.three.geo;

import rcs.feyn.math.Matrix44;

public interface Transformable3d {
  
  void transform(Matrix44 transform);
  
}
