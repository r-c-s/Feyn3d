package rcs.feyn.two.gui;

import rcs.feyn.gfx.Raster;
import rcs.feyn.gui.AbstractAnimationCanvas;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.two.gfx.Graphics2d;
import rcs.feyn.two.kernel.FeynApp2d;

public abstract class AnimationCanvas2d extends AbstractAnimationCanvas {
  
  private static final long serialVersionUID = 1L;
  
  static {
      new FeynApp2d();
  }
  
  public AnimationCanvas2d() {
      super(new Graphics2d(new Raster(0, 0)));
  }

  @Override
  protected final void render(Graphics2d graphics) {
      FeynApp2d.getRenderKernel().renderAll((Graphics3d) graphics);
  }

  @Override
  protected void setWidth(int width) {
      //FeynApp2d.getViewFrustum().setWidth(width);
  }

  @Override
  protected void setHeight(int height) {
      //FeynApp2d.getViewFrustum().setHeight(height);
  }
}