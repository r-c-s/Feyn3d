package rcs.feyn.three.gfx;

public class Graphics3dSynchronized extends Graphics3d {

  protected boolean updating = false;
  
  protected Object[] locks;

  public Graphics3dSynchronized(Raster raster) {
    super(raster);
  } 

  @Override
  public void setRaster(Raster raster) {
    super.setRaster(raster);
    locks = new Object[raster.size()];
    for (int i = 0; i < locks.length; i++) {
      locks[i] = new Object();
    }
  } 
 
  @Override
  public void putPixel(int index, int color) {
    synchronized (locks[index]) {
      super.putPixel(index, color);
    }
  }
 
  @Override
  public void putPixel(int index, double z, int color) {
    synchronized (locks[index]) {
      super.putPixel(index, z, color);
    }
  }
}