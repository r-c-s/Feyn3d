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
    locks = new Object[1000];
    for (int i = 0; i < locks.length; i++) {
      locks[i] = new Object();
    }
  }
 
  @Override
  public void putPixel(int index, double z, int color) {
    int lockIndex = (int) ((index / (double) raster.size()) * locks.length);
    synchronized (locks[lockIndex]) {
      super.putPixel(index, z, color);
    }
  }
}