package rcs.feyn.three.render.patches;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.color.AbstractColorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public abstract class Patch3d extends AbstractColorable { 

  protected RenderOptions3d options;

  public Patch3d(FeynColor color, RenderOptions3d options) {
    super(color); 
    this.options = options;
  }

  public final RenderOptions3d getRenderOptions() {
    return options;
  }
  
  public boolean isTransparent() {
    return color.isTransparent();
  }

  public abstract Vector3d getCenter();

  public abstract void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort);
}
