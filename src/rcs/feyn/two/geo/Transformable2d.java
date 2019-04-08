package rcs.feyn.two.geo;

import rcs.feyn.math.linalg.Matrix33;

public interface Transformable2d {
  
  public void transform(Matrix33 transform);
  
}
