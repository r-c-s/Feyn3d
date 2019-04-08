package rcs.feyn.three.gfx;

import rcs.feyn.gfx.Raster;

public class Graphics3dSynchronized extends Graphics3d {

  protected boolean updating = false;
  
  protected Object[] locks;

  public Graphics3dSynchronized(Raster raster) {
    super(raster);
  } 

  @Override
  public synchronized void setRaster(Raster raster) {
    super.setRaster(raster);
    
    updating = true;
    locks = new Object[raster.size()];
    for (int i = 0; i < locks.length; i++) {
      locks[i] = new Object();
    }
    updating = false;
    
    synchronized (this) {
      notify();
    }
  } 
 
  @Override
  public void putPixel(int index, int color) { 
    waitIfUpdating();
    
    synchronized (locks[index]) {
      super.putPixel(index, color);
    }
  }
 
  @Override
  public void putPixel(int index, double z, int color) { 
    waitIfUpdating();
    
    synchronized (locks[index]) {
      super.putPixel(index, z, color);
    }
  }
 
  public void waitIfUpdating() {
    if (updating) {
      try {
        wait();
      } 
      catch (InterruptedException e) { }
    }
  }
}