package demo3d;

import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dBuilder;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Matrices;
import rcs.feyn.math.linalg.Vector3d;

public class Teapot extends Demo3d { 

	@Serial
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.01);
  }
  
  private String teapotObjFilePath = System.getProperty("user.dir") + "/objfiles/teapot.obj";

  private Model3d teapot = new Model3dBuilder()
  		.fromObjFile(teapotObjFilePath)
  		.setColor(FeynColor.white)
  		.addTransform(Matrices.create3dScaleMatrix(0.05))
  		.build();
  
  private boolean trackLightsourceWithCamera = false;

  public Teapot() { }

  @Override
  protected void initialize() {
    super.initialize();
    
    wzc.setAmount(0.1);
    
    setBackgroundColor(FeynColor.superDarkGray); 
    
    Model3dUtils.setOptions(
        teapot, 
        Set.of(RenderOptions3d.Option.gouraudShaded),
        Set.of(RenderOptions3d.Option.cullIfBackface));
    
    FeynApp3d.getRepository().add(teapot);
    
    camera.translate(0.5, 2, 5.5);
    camera.rotate(Vector3d.X_AXIS, -20 * MathConsts.DEGREES_TO_RADIANS); 

    var lightSourceRed = new VariableIntensityLightSource3d(10, new FeynColor(255, 0, 0));
    var lightSourceGreen = new VariableIntensityLightSource3d(10, new FeynColor(0, 255, 0));
    lightSourceRed.setPosition(new Vector3d( 5, 4, 5));
    lightSourceGreen.setPosition(new Vector3d(-5, 4, 5));
    FeynApp3d.addDiffuseLightSource(lightSourceRed);
    //FeynApp3d.addDiffuseLightSource(lightSourceGreen);
    
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.2)); 
  } 

  @Override
  public void runningLoop() {
    controlCamera();  
    handleInput();
    if (trackLightsourceWithCamera) {
      // add new white lightsource
    }
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
     
    if (keyHasBeenPressed(KeyEvent.VK_G)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.getRenderOptions().toggle(RenderOptions3d.Option.gouraudShaded);
      }
    }
    if (keyHasBeenPressed(KeyEvent.VK_A)) {
      inputDelay = 50;
      FeynColor color = teapot.getFaces()[0].getColor();
      if (color.isTransparent()) {
        teapot.setColor(color.opaque());
        for (Model3dFace face : teapot.getFaces()) {
          face.getRenderOptions().enable(RenderOptions3d.Option.cullIfBackface);
        }
      } else {
        teapot.setColor(color.fadeTo(0.5));
        for (Model3dFace face : teapot.getFaces()) {
          face.getRenderOptions().disable(RenderOptions3d.Option.cullIfBackface);
        }
      }
    }
    if (keyHasBeenPressed(KeyEvent.VK_M)) { 
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.getRenderOptions().toggle(RenderOptions3d.Option.meshOnly);
      }
    }
    if (keyHasBeenPressed(KeyEvent.VK_S)) {
      inputDelay = 50;
      screenshot("./screenshots/"+System.currentTimeMillis()+".png");
    }
    if (keyHasBeenPressed(KeyEvent.VK_L)) {
      inputDelay = 50;
      trackLightsourceWithCamera = !trackLightsourceWithCamera;
    }
    if (keyHasBeenPressed(KeyEvent.VK_C)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.getRenderOptions().toggle(RenderOptions3d.Option.applyLightingColor);
      }
    }
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 600, "Teapot", true, false);
    var demo = new Teapot();
    frame.add("Center", demo);
    frame.setVisible(true); 
    demo.init();
  } 
}