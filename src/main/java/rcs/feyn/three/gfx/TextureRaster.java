package rcs.feyn.three.gfx;

import rcs.feyn.color.ColorUtils;

public class TextureRaster extends Raster {

  private final int averageColor;

  public TextureRaster(int[] data, int width, int height) {
    super(data, width, height);
    
    long sumr = 0, sumg = 0, sumb = 0;
    for (int rgba : data) {
      sumr += ColorUtils.getRedFromRGBA(rgba);
      sumg += ColorUtils.getGreenFromRGBA(rgba);
      sumb += ColorUtils.getBlueFromRGBA(rgba);
    }

    this.averageColor =  ColorUtils.getRGBA(
        (int) (sumr / data.length), 
        (int) (sumg / data.length),
        (int) (sumb / data.length),
        255);
  }
  
  public int getAverageColor() {
    return averageColor;
  }
}
