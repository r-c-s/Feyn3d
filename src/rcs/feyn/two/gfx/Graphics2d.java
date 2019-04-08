package rcs.feyn.two.gfx;

import java.util.Arrays;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;

public class Graphics2d { 
  
  protected Raster raster;

  public Graphics2d(Raster raster) {
    setRaster(raster);
  }

  public Raster getRaster() {
    return raster;
  }

  public void setRaster(Raster raster) {
    this.raster = raster;
  }

  public void fill(FeynColor background) {
    Arrays.fill(raster.getData(), background.getRGBA());
  }

  public final void putPixel(int x, int y, int color) {
    int w = raster.getWidth();
    int h = raster.getHeight();
    
    if (x < 0 || x >= w ||
        y < 0 || y >= h) {
      return;
    }
    putPixel(w*y + x, color);
  }

  protected void putPixel(int index, int color) {
    int pixel = raster.getPixel(index);
    if (ColorUtils.getAlphaFromRGBA(pixel) != 255) {
      pixel = ColorUtils.alphaBlend(pixel, color);
    } else {
      pixel = ColorUtils.alphaBlend(color, pixel);
    }
    raster.setPixel(index, pixel);
  }
}
