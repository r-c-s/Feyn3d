package rcs.feyn.three.gfx;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.two.gfx.Graphics2d;

public class Graphics3d extends Graphics2d {

  protected DepthBuffer zbuffer;

  public Graphics3d(Raster raster) {
    super(raster);
  } 

  public void setRaster(Raster raster) {
    super.setRaster(raster);
    zbuffer = new NdcZDepthBuffer64(raster.size());
  }

  public DepthBuffer getZBuffer() {
    return zbuffer;
  }

  @Override
  public void fill(FeynColor background) {
    super.fill(background); 
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
}
