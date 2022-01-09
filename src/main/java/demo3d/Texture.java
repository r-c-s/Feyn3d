package demo3d;

import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dTexturedFace;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.entities.primitives.Line3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.RenderOptions3d;

public class Texture extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L;

  String[] textureFiles = {
      System.getProperty("user.dir") + "/textures/texture.jpg",
      System.getProperty("user.dir") + "/textures/texture2.jpg",
      System.getProperty("user.dir") + "/textures/texture3.jpg",
      System.getProperty("user.dir") + "/textures/texture4.jpg",
      System.getProperty("user.dir") + "/textures/texture5.jpg",
      System.getProperty("user.dir") + "/textures/texture6.jpg",
      System.getProperty("user.dir") + "/textures/texture7.jpg",
      System.getProperty("user.dir") + "/textures/texture8.jpg"
  };
  
  private final Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private final Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private final Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private final Model3d obj = Model3dFactory
      .dodecahedron(0.6)
      .setTextureData(nextTexture())
      .build();
  
  private final Runnable objAnimation = new Animation();
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.darkGray);
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    Model3dUtils.setOptions(
        obj, 
        Set.of(RenderOptions3d.Option.flatShaded), 
        Set.of(RenderOptions3d.Option.gouraudShaded));

    FeynRuntime.getRepository().add(obj);
    FeynRuntime.getRepository().add(x);
    FeynRuntime.getRepository().add(y);
    FeynRuntime.getRepository().add(z);

    camera.translate(0, 0, 1.5);
    
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(1)); 
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.2));
    FeynRuntime.getDiffuseLightSources()[0].setPosition(camera.getPosition()); 
    
    wzc.setAmount(0.1);
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() {
    controlCamera();
    handleInput();
    objAnimation.run();
  }  

  int inputDelay = 20;
  public void handleInput() {
    if (inputDelay > 0) {
      inputDelay--;
      return;
    } else {
      inputDelay = 0;
    }
     
    if (keyHasBeenPressed(KeyEvent.VK_C)) {
      inputDelay = 20;
      TextureRaster nextTexture = nextTexture();
      for (var face : obj.getFaces()) {
        ((Model3dTexturedFace) face).setTextureData(nextTexture);
      }
    }
  }
  
  int i = 0;
  public TextureRaster nextTexture() {
    return Model3dUtils.getImageData(textureFiles[i++ % textureFiles.length]);
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