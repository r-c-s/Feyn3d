package rcs.feyn.three.gui;

import rcs.feyn.gfx.Raster;
import rcs.feyn.gui.AbstractAnimationCanvas;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Graphics3dSynchronized;
import rcs.feyn.three.kernel.FeynApp3d;

public abstract class AnimationCanvas3d extends AbstractAnimationCanvas {
  
  private static final long serialVersionUID = 1L;

  static {
    new FeynApp3d();
  }

  public AnimationCanvas3d() {
    super(new Graphics3dSynchronized(new Raster(0, 0)));
  }

  @Override
  protected final void render(Graphics3d graphics) {
    FeynApp3d.getRenderKernel().renderAll((Graphics3d) graphics); 
  }

  @Override
  protected void setWidth(int width) {
      FeynApp3d.getViewFrustum().setWidth(width);
  }

  @Override
  protected void setHeight(int height) {
      FeynApp3d.getViewFrustum().setHeight(height);
  }
}
