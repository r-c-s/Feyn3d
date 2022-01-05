package rcs.feyn.three.entities.primitives;

import rcs.feyn.color.AlphaEnabled;
import rcs.feyn.color.Colorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.three.entities.Sprite3d;
import rcs.feyn.three.render.RenderOptions3d;

public abstract class Primitive3d extends Sprite3d implements Colorable, AlphaEnabled {

  protected FeynColor color = FeynColor.black;
  protected RenderOptions3d options = RenderOptions3d.defaults();

  @Override
  public FeynColor getColor() {
    return color;
  }

  @Override
  public void setColor(FeynColor color) {
    this.color = color;
  }

  @Override
  public int getAlpha() {
    return color.getAlpha();
  }

  @Override
  public void setAlpha(int alpha) {
    color = new FeynColor(color.getRed(), color.getGreen(), color.getBlue(), alpha);
  }
  
  public final RenderOptions3d getRenderOptions() {
    return options;
  }
}
