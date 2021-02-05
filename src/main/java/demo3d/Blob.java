package demo3d;

import java.io.Serial;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.entities.primitives.Line3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;

public class Blob extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L;
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private final Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private final Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private final Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private final Model3d obj = Model3dFactory
      .icosphere(0.6, 2)
      .setColor(FeynColor.darkSeaGreen)
      .build();
  
  private final Runnable blobAnimation = new BlobAnimation();
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.bisque);
    
    Model3dUtils.setOptions(
        obj, 
        Set.of(RenderOptions3d.Option.gouraudShaded), 
        Set.of(RenderOptions3d.Option.cullIfBackface));
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);

    FeynApp3d.getRepository().add(obj);
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
    FeynApp3d.getDiffuseLightSource().setPosition(camera.getPosition()); 
    blobAnimation.run();
  }  
  
  private class BlobAnimation implements Runnable {
    
    private int i = 0;

    @Override
    public void run() {
      obj.rotate(Vector3d.Y_AXIS, 0.01);
      
      Function<Double, Double> function = ++i % 2 == 0 
      		? TrigLookUp::sin
      		: TrigLookUp::cos;
      
      double factor = 0.005 * function.apply(i * MathConsts.DEGREES_TO_RADIANS);
      		
      Model3dUtils.deform(obj, factor);
    }
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Blob Demo", true, false);
    var demo = new Blob();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}