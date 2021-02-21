package rcs.feyn.three.gfx;

import java.util.Arrays;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;

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
 
  protected void putPixel(int index, double z, int pixelCandidate) {
    int currentPixel = raster.getPixel(index);
    int newPixel;
    
    boolean candidateIsInFront = zbuffer.checkAndSetDepth(index, z);
    
    if (candidateIsInFront) {
      newPixel = ColorUtils.alphaBlend(pixelCandidate, currentPixel);
    } else if (ColorUtils.isTransparent(currentPixel)) {
      newPixel = ColorUtils.alphaBlend(currentPixel, pixelCandidate);
    } else {
      return;
    }
    
    raster.setPixel(index, newPixel);
  }
}
