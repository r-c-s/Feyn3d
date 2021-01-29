package rcs.feyn.three.kernel;

import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.DiffuseLightSource3d;
import rcs.feyn.three.view.Camera3d;
import rcs.feyn.three.view.ViewFrustum3d;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class FeynApp3d {
 
  private static final ViewFrustum3d viewFrustum =
      new ViewFrustum3d(0, 0,  0.01, 1000);
  
  private static final ObjectRepository3d repository =
      new ObjectRepository3d();
  
  private static final RenderKernel3d renderKernel =
      new RenderKernel3d(Runtime.getRuntime().availableProcessors(), repository);
  
  private static final Camera3d camera = new Camera3d();
  
  private static DiffuseLightSource3d lightSource = null;
  private static AmbientLightSource3d ambientLight = new AmbientLightSource3d(1);

  public static ViewFrustum3d getViewFrustum() {
    return viewFrustum;
  }
  
  public static RenderKernel3d getRenderKernel() {
    return renderKernel;
  }
  
  public static ObjectRepository3d getRepository() {
    return repository;
  }

  public static Camera3d getCamera() {
    return camera;
  }

  public static Matrix44 getViewSpaceMatrix() {
    Vector3d p, i, j, k;
    synchronized (camera) {
      p = camera.getPosition();
      i = camera.getSideVector();
      j = camera.getUpVector();
      k = camera.getForwardVector();
    }
    return ViewUtils.getViewSpaceMatrix(p, i, j, k);
  } 

  public static Matrix44 getPerspectiveProjectionMatrix() {
    return ViewUtils.getPerspectiveProjectionMatrix(viewFrustum);
  }

  public static Matrix44 getOrthographicProjectionMatrix() {
    return ViewUtils.getOrthographicProjectionMatrix(viewFrustum);
  }

  public static Matrix44 getViewPortMatrix() {
    return ViewUtils.getViewPortMatrix(viewFrustum);
  }

  public static DiffuseLightSource3d getDiffuseLightSource() {
    return lightSource;
  }

  public static void setDiffuseLightSource(DiffuseLightSource3d ls) {
    lightSource = ls;
  }

  public static AmbientLightSource3d getAmbientLight() {
    return ambientLight;
  }

  public static void setAmbientLight(AmbientLightSource3d light) {
    ambientLight = light;
  }
}
