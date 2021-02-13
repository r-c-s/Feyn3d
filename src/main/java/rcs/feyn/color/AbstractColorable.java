package rcs.feyn.color;

public class AbstractColorable implements Colorable {
  
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
}
