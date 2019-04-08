package rcs.feyn.two.kernel;

import rcs.feyn.three.kernel.RenderKernel3d;
import rcs.feyn.two.view.Camera2d;

public class FeynApp2d {

  public static final int DEFAULT_RENDER_KERNEL_NUM_THREADS = 1;
  
  private static final RenderKernel3d RENDER_KERNEL = 
          new RenderKernel3d(Runtime.getRuntime().availableProcessors(), null);
  
  private static Camera2d camera = new Camera2d();
  private static double ambientLight = 1.00;
  
  public static RenderKernel3d getRenderKernel() {
    return RENDER_KERNEL;
  }

  public static Camera2d getCamera() {
    return camera;
  }

  public static void setCamera(Camera2d cam) {
    camera = cam;
  }
  
  public static double getAmbientLight() {
    return ambientLight;
  }

  public static void setAmbientLight(double light) {
    ambientLight = light;
  }
}
