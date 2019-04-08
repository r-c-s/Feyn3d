package rcs.feyn.two.view;

import rcs.feyn.two.Particle2d;
import rcs.feyn.math.linalg.Matrix33;
import rcs.feyn.math.linalg.Vector2d;

public class Camera2d extends Particle2d {

  private final Matrix33 viewMatrix = new Matrix33();

  public Camera2d() {
    this(Vector2d.ZERO);
  }

  public Camera2d(Vector2d position) {
    super(position);     
  
    update();
  }  

  public synchronized void update() {
    viewMatrix.set(
        -sd.x(), -sd.y(),  sd.dotProd(position), 
         up.x(),  up.y(), -up.dotProd(position), 
         0, 0, 1);
  }

  public Matrix33 getViewMatrix() {
    return new Matrix33(viewMatrix);
  } 

  public synchronized Vector2d toViewSpace(Vector2d vector) {
    return vector.affineTransform(viewMatrix);
  }
}
