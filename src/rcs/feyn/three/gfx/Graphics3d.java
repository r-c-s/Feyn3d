package rcs.feyn.three.gfx;

import java.util.Arrays;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;

public class Graphics3d {

  protected DepthBuffer zbuffer;
  protected Raster raster;

  public Graphics3d(Raster raster) {
    setRaster(raster);
  } 
  
  public Raster getRaster() {
    return raster;
  }

  public void setRaster(Raster raster) {
    this.raster = raster;
    this.zbuffer = new NdcZDepthBuffer32(raster.size());
  }

  public DepthBuffer getZBuffer() {
    return zbuffer;
  }

  public void fill(FeynColor background) {
    Arrays.fill(raster.getData(), background.getRGBA());
    zbuffer.clear();
  }

  public final void putPixel(int x, int y, double z, int color) {    
    int w = raster.getWidth();
    int h = raster.getHeight();
    
    if (x < 0 || x >= w ||
        y < 0 || y >= h) {
      return;
    }
    putPixel(w*y + x, z, color);
  }
 
  protected void putPixel(int index, double z, int color) {
    int pixel = raster.getPixel(index);
    if (zbuffer.checkAndSetDepth(index, z)) {
      pixel = ColorUtils.alphaBlend(color, pixel);
    } else if (ColorUtils.getAlphaFromRGBA(pixel) != 255) {
      pixel = ColorUtils.alphaBlend(pixel, color);
    } 
    raster.setPixel(index, pixel);
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
