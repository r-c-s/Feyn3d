package rcs.feyn.three.gui;

import rcs.feyn.gui.AbstractAnimationCanvas;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Graphics3dSynchronized;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.kernel.RenderKernel;

public abstract class AnimationCanvas3d extends AbstractAnimationCanvas {
  
  private static final long serialVersionUID = 1L;
  
  private final RenderKernel renderKernel = FeynRuntime.getRenderKernel(); 

  static {
    new FeynRuntime();
  }

  public AnimationCanvas3d() {
    super(new Graphics3dSynchronized(new Raster(0, 0)));
  }

  @Override
  protected final void render(Graphics3d graphics) {
    renderKernel.renderAll((Graphics3d) graphics); 
  }

  @Override
  protected void setWidth(int width) {
      FeynRuntime.getView().getViewFrustum().setWidth(width);
  }

  @Override
  protected void setHeight(int height) {
      FeynRuntime.getView().getViewFrustum().setHeight(height);
  }
}
