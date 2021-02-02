package demo3d;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.primitives.Line3d;

public class Texture extends Demo3d {

  private static final long serialVersionUID = 1L;
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private final Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private final Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private final Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);
  
  private String textureFile = System.getProperty("user.dir") + "/textures/texture.jpg";
  private Raster textureData = Model3dUtils.getImageData(textureFile);

  private final Model3d obj = Model3dFactory
      .icosphere(0.6, 1)
      .setTextureData(textureData)
      .build();
  
  private final Runnable objAnimation = new Animation();
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.darkGray);
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);

    FeynApp3d.getRepository().add(obj);
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);

    camera.translate(0, 0, 1.5);
    
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
    objAnimation.run();
  }  
  
  private class Animation implements Runnable {
    @Override
    public void run() {
      obj.rotate(Vector3d.Y_AXIS, 0.01);
    }
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Blob Demo", true, false);
    var demo = new Texture();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}