package rcs.feyn.two;

import java.awt.Frame;

import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.gui.AnimationCanvas3d;

public class Demo2d extends AnimationCanvas3d {
 
  private static final long serialVersionUID = 1L;

  @Override
  public void runningLoop() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void pausedLoop() {
    // TODO Auto-generated method stub
    
  }
 
  public static void main(String[] args) {
    Frame frame = new FeynFrame(1920, 1080, "Feyn Demo", true, true);
    Demo2d demo = new Demo2d();
    frame.add("Center", demo);
    demo.init();
    frame.setVisible(true); 
  }
}
