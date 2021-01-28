package demo3d.teapot;

import java.awt.event.KeyEvent;
import java.util.EnumSet;

import demo3d.Demo3d;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dBuilder;
import rcs.feyn.three.render.models.Model3dFace;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class Teapot extends Demo3d { 
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.01);
  }
  
  private String teapotObjFilePath = "C:\\Users\\User\\eclipse-workspace\\Feyn3d\\src\\demo3d\\teapot\\minicooper.obj";

  private Model3d teapot = new Model3dBuilder()
    .fromObjFile(teapotObjFilePath)
    .addColor(FeynColor.white)//.fade(0.5))
    .addTransform(Matrix44.createScaleMatrix(0.05))
    .build();

  public Teapot() { }

  @Override
  protected void initialize() {
    super.initialize();
    
    wzc.setAmount(0.1);
    
    teapot.pitch(-90);
    
    setBackgroundColor(FeynColor.superDarkGray); 
    
    Model3dUtils.setOptions(
        teapot, 
        EnumSet.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.bothSidesShaded), 
        null);
    
    FeynApp3d.getRepository().add(teapot);
    
    camera.translate(1, 2, 5.5);
    camera.rotate(Vector3d.X_AXIS, -20); 
    
    FeynApp3d.setDiffuseLightSource(new VariableIntensityLightSource3d(30));
    FeynApp3d.getDiffuseLightSource().setPosition(new Vector3d(3, 4, 5));
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.05)); 
  } 

  @Override
  public void runningLoop() {
    controlCamera();  
    handleInput();
//    teapot.yaw(1); 
  }

  @Override
  public void pausedLoop() {
    controlCamera();
    handleInput();
  }

  int inputDelay = 50;
  public void handleInput() {
    if (inputDelay > 0) {
      inputDelay--;
      return;
    } else {
      inputDelay = 0;
    }
     
    if (pressed[KeyEvent.VK_G]) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.getRenderOptions().toggle(RenderOptions3d.Option.gouraudShaded);
      }
    }
    if (pressed[KeyEvent.VK_A]) {
      inputDelay = 50;
      FeynColor color = teapot.getFaces()[0].getColor();
      if (color.isTransparent()) {
        teapot.setColor(color.opaque());
        for (Model3dFace face : teapot.getFaces()) {
          face.getRenderOptions().enable(RenderOptions3d.Option.cullIfBackface);
        }
      } else {
        teapot.setColor(color.fade(0.5));
        for (Model3dFace face : teapot.getFaces()) {
          face.getRenderOptions().disable(RenderOptions3d.Option.cullIfBackface);
        }
      }
    }
    if (pressed[KeyEvent.VK_M]) { 
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.getRenderOptions().toggle(RenderOptions3d.Option.meshOnly);
      }
    }
    if (pressed[KeyEvent.VK_S]) {
      inputDelay = 50;
      screenshot("./screenshots/"+System.currentTimeMillis()+".png");
    }
  }

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 600, "Teapot", true, false);
    Demo3d demo = new Teapot();
    frame.add("Center", demo);
    frame.setVisible(true); 
    demo.init();
  } 
}