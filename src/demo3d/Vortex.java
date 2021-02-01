package demo3d;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.anim.Curve3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.render.primitives.Line3d;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;

public class Vortex extends Demo3d { 
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private Curve3d curve = new Curve3d(
      (r, t) -> { 
        r = 2*(r-0.5);
        double radius = Math.sqrt(1 - r*r);
        return new Vector3d(radius*TrigLookUp.cos(r*t*360 + t),
                            radius*TrigLookUp.sin(r*t*360 + t),
                            r); 
      },
      (r, t, z) -> {         
        return new FeynColor((int)(10+TrigLookUp.cos(r*t*360 + t)*245), 
                             (int)(10+TrigLookUp.cos(r*t*180 + t)*245), 
                             (int)(10+TrigLookUp.cos(r*t*90 + t)*245), 
                             255);
      },
      0.001, 
      0.05);

  public Vortex() { }
  
  @Override
  public void initialize() {
    super.initialize();
    
    wzc.setAmount(0.2);
    
    var x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
    var y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
    var z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);
    
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);

    FeynApp3d.getRepository().add(curve);
    
    camera.translate(0, 0, 2);
  }
  
  @Override
  public void pausedLoop() { 
    controlCamera(); 
  } 
  
  @Override
  public void runningLoop() { 
    controlCamera(); 
    curve.tick();
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Vortex", true, false); 
    var demo = new Vortex(); 
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
