package rcs.feyn.three;

import rcs.feyn.event.DragRotateObject;
import rcs.feyn.event.WheelZoomCamera;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.gui.AnimationCanvas3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.view.Camera3d;
import rcs.feyn.math.linalg.Vector3d;

public class Demo3d extends AnimationCanvas3d { 

  private static final long serialVersionUID = 1L;
  
  protected Camera3d camera = FeynApp3d.getCamera();
  protected DragRotateObject drc = new DragRotateObject(camera, Vector3d.ZERO);
  protected WheelZoomCamera  wzc = new WheelZoomCamera(camera, 10);

  public Demo3d() { }

  @Override
  protected void initialize() {
    requestFocus(); 
    addKeyListener(this);
    wzc.setAmount(1);
    addDeltaMouseListener(drc);
    addMouseWheelListener(wzc);
  }

  protected final void controlCamera() {
    drc.update();
    wzc.update();
  }

  @Override
  public void runningLoop() { }

  @Override
  public void pausedLoop() { } 

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(1920, 1080, "Feyn Demo", true, true);
    Demo3d demo = new Demo3d();
    frame.add("Center", demo);
    demo.init();
    frame.setVisible(true); 
  }
}
