package demo3d.animations;

import rcs.feyn.three.kernel.FeynApp3d;

public class TrackLightSourceWithCamera implements Runnable {
  
  @Override
  public void run() {
    FeynApp3d.getDiffuseLightSource().setPosition(FeynApp3d.getCamera().getPosition()); 
  }
}
