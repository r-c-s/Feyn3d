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
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class NormalizeDeformAndShatter extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L;
  
  private TextureRaster texture = Model3dUtils.getImageData(System.getProperty("user.dir") + "/textures/texture5.jpg");
  
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
    
    Model3dUtils.setOptions(
        obj, 
        Set.of(flatShaded, cullIfBackface), 
        Set.of(applyLightingColor, gouraudShaded));
    
    // normalize
    Model3dUtils.normalizeFacesToTriangles(obj);
    
    // deform
    Model3dUtils.deform(obj, 0.2);

    // shatter
    //objs = Model3dUtils.partition3d(obj);
    objs = Model3dUtils.partition2d(obj, 0.2);
    
    for (Model3d model : objs) {
      FeynRuntime.getRepository().add(model);
    }
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    FeynRuntime.getRepository().add(x);
    FeynRuntime.getRepository().add(y);
    FeynRuntime.getRepository().add(z);

    camera.translate(0, 0, 2);
    
    FeynRuntime.addDiffuseLightSource(new VariableIntensityLightSource3d(2)); 
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.4));
    
    wzc.setAmount(0.2);
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() {
    controlCamera();
    FeynRuntime.getDiffuseLightSources()[0].setPosition(camera.getPosition()); 
    shatterAnimation.run();
  }
  
  private class ShatterAnimation implements Runnable {
    private int i = 0;
    
    @Override
    public void run() {
      for (Model3d model : objs) {
        model.translate(model.getPosition().mul(0.005*TrigLookUp.sin(++i*0.0002)));
      }
    }
  }

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Shatter Demo", true, false);
    Demo3d demo = new NormalizeDeformAndShatter();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
