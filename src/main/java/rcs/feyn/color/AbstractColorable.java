package rcs.feyn.color;

public class AbstractColorable implements Colorable, AlphaEnabled {
  
  protected FeynColor color;

  public AbstractColorable(FeynColor color) {
    setColor(color);
  }

  @Override
  public final FeynColor getColor() {
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
}
