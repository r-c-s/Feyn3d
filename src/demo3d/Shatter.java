package demo3d;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.anim.TrackLightSourceWithCamera;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.primitives.Line3d;

public class Shatter extends Demo3d {

  private static final long serialVersionUID = 1L;
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private Model3d[] objs;
  
  private final Runnable shatterAnimation = new ShatterAnimation();
  private final Runnable trackLightsourceWithCamera = new TrackLightSourceWithCamera();
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.bisque);
    
    Model3d obj = Model3dFactory
        .dodecahedron(0.6)
        .addColor(new FeynColor(123, 234, 13, 255))
        .build();

    objs = Model3dUtils.partition3d(obj, 1);
    for (Model3d model : objs) {
      FeynApp3d.getRepository().add(model);
    }
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);

    camera.translate(0, 0, 2);
    
    FeynApp3d.setDiffuseLightSource(new VariableIntensityLightSource3d(2)); 
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.2));
    
    wzc.setAmount(0.2);
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() {
    controlCamera();
    trackLightsourceWithCamera.run();
    shatterAnimation.run();
  }
  
  private class ShatterAnimation implements Runnable {
    private int i = 0;
    
    @Override
    public void run() {
      for (Model3d model : objs) {
        model.translate(model.getPosition().mul(0.01*TrigLookUp.sin(++i*0.1)));
      }
    }
  }

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Shatter Demo", true, false);
    Demo3d demo = new Shatter();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
