package rcs.feyn.three.render.primitives;

import rcs.feyn.color.Colorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.three.render.FeynSprite3d;

public abstract class Primitive3d extends FeynSprite3d implements Colorable {

  protected FeynColor color = FeynColor.black;

  @Override
  public FeynColor getColor() {
    return color;
  }

  @Override
  public void setColor(FeynColor color) {
    this.color = color;
  }
}
