package rcs.feyn.three.kernel;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.view.Camera3d;
import rcs.feyn.three.view.ViewFrustum3d;
import rcs.feyn.three.view.ViewUtils;

public class View {
  
  private final Camera3d camera = new Camera3d();
  
  private final ViewFrustum3d viewFrustum = new ViewFrustum3d(0, 0,  0.01, 1000);
  
  public Camera3d getCamera() {
    return camera;
  }
  
  public ViewFrustum3d getViewFrustum() {
    return viewFrustum;
  }

  public Matrix44 getViewSpaceMatrix() {
    Vector3d p, i, j, k;
    synchronized (camera) {
      p = camera.getPosition();
      i = camera.getSideVector();
      j = camera.getUpVector();
      k = camera.getForwardVector();
    }
    return ViewUtils.getViewSpaceMatrix(p, i, j, k);
  } 

  public Matrix44 getPerspectiveProjectionMatrix() {
    return ViewUtils.getPerspectiveProjectionMatrix(viewFrustum);
  }

  public Matrix44 getOrthographicProjectionMatrix() {
    return ViewUtils.getOrthographicProjectionMatrix(viewFrustum);
  }

  public Matrix44 getViewPortMatrix() {
    return ViewUtils.getViewPortMatrix(viewFrustum);
  }
}
