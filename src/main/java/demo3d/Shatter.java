package demo3d;

import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.entities.primitives.Line3d;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;

public class Shatter extends Demo3d {

	@Serial
  private static final long serialVersionUID = 1L;
  
  private Raster texture = Model3dUtils.getImageData(System.getProperty("user.dir") + "/textures/texture5.jpg");
  
  private Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private Model3d[] objs;
  
  private final Runnable shatterAnimation = new ShatterAnimation();
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.bisque);
    
    Model3d obj = Model3dFactory
        .dodecahedron(0.6)
        .setTextureData(texture)
        .build();

    objs = Model3dUtils.partition3d(obj, 1);
    for (Model3d model : objs) {
      FeynApp3d.getRepository().add(model);
      Model3dUtils.setOptions(
          model, 
          Set.of(RenderOptions3d.Option.flatShaded), 
          Set.of(RenderOptions3d.Option.applyLightingColor, RenderOptions3d.Option.gouraudShaded));
    }
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);

    camera.translate(0, 0, 2);
    
    FeynApp3d.addDiffuseLightSource(new VariableIntensityLightSource3d(2)); 
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
    FeynApp3d.getDiffuseLightSources()[0].setPosition(camera.getPosition()); 
    shatterAnimation.run();
  }
  
  private class ShatterAnimation implements Runnable {
    private int i = 0;
    
    @Override
    public void run() {
      for (Model3d model : objs) {
        model.translate(model.getPosition().mul(0.01*TrigLookUp.sin(++i*0.002)));
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
