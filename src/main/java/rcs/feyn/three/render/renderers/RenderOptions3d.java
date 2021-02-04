package rcs.feyn.three.render.renderers;

import java.util.EnumSet;
import java.util.Set;

public class RenderOptions3d {
  
  public enum Option {
    meshOnly,
    lighted,
    cullIfBackface,
    bothSidesShaded,
    flatShaded,
    gouraudShaded,
    meshShaded,
    applyLightingColor
  }

  private Set<Option> options;
  
  public static RenderOptions3d defaults() {
    return new RenderOptions3d(EnumSet.<Option>of(
        Option.cullIfBackface,
        Option.flatShaded, 
        Option.meshShaded, 
        Option.lighted,
        Option.applyLightingColor));
  }
  
  public RenderOptions3d(RenderOptions3d options) {
    this(options.options);
  }
  
  public RenderOptions3d(Set<Option> options) {
    this.options = options;
  }
  
  public boolean isEnabled(Option option) {
    return options.contains(option);
  }
  
  public void enable(Option option) {
    options.add(option);
  }
  
  public void disable(Option option) {
    options.remove(option);
  }
  
  public void toggle(Option option) {
    if (options.contains(option)) {
      options.remove(option);
    } else {
      options.add(option);
    }
  }
}
