package rcs.feyn.three.render.patches;

import java.util.Comparator;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.color.AbstractColorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public abstract class Patch3d extends AbstractColorable { 
  
  public static final Comparator<Patch3d> DEPTH_COMPARATOR = (a, b) -> {
    Vector3d cameraPos = FeynApp3d.getView().getCamera().getPosition();
    
    double thisDepth = a.getCenter().distanceSquared(cameraPos);
    double thatDepth = b.getCenter().distanceSquared(cameraPos);
    
    return Double.compare(thisDepth, thatDepth);
  };

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

  protected abstract Vector3d getCenter();

  public abstract void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort);
}
