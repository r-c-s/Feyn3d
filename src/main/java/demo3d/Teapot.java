package demo3d;

import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dBuilder;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dTexturedFace;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Matrices;
import rcs.feyn.math.Vector3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class Teapot extends Demo3d { 

  @Serial
  private static final long serialVersionUID = 1;
  
  private String teapotObjFilePath = System.getProperty("user.dir") + "/objfiles/teapot.obj";

  private Model3d teapot = new Model3dBuilder()
      .fromObjFile(teapotObjFilePath)
      .setTextureData(Model3dUtils.getImageData(
          System.getProperty("user.dir") + "/textures/tile.jpg"),
          255, 
          1)
      .addTransform(Matrices.create3dScaleMatrix(0.05))
      .build();
  
  public Teapot() { }

  @Override
  protected void initialize() {
    super.initialize();
    
    wzc.setAmount(0.1);
    
    setBackgroundColor(FeynColor.white); 
    
    Model3dUtils.setOptions(
        teapot, 
        Set.of(),
        Set.of(cullIfBackface, applyLightingColor, flatShaded, gouraudShaded, textured, bothSidesShaded));
    
    FeynRuntime.getRepository().add(teapot);
    
    camera.setPosition(teapot.getCenter());
    camera.translate(0, 2, 7);
    camera.rotate(Vector3d.X_AXIS, -20 * MathConsts.DEGREES_TO_RADIANS); 

    var lightSourceRed = new VariableIntensityLightSource3d(10, new FeynColor(255, 0, 0));
    var lightSourceGreen = new VariableIntensityLightSource3d(10, new FeynColor(0, 255, 0));
    lightSourceRed.setPosition(3, 4, 5);
    lightSourceGreen.setPosition(-3, 4, 5);
    FeynRuntime.addDiffuseLightSource(lightSourceRed);
    FeynRuntime.addDiffuseLightSource(lightSourceGreen);
    
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.3)); 
  } 

  @Override
  public void runningLoop() {
    controlCamera();  
    handleInput();
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
      
    if (keyHasBeenPressed(KeyEvent.VK_F)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.setColor(new FeynColor((((Model3dTexturedFace) face).getTextureData().getAverageColor())));
        face.getRenderOptions().toggle(flatShaded);
        face.getRenderOptions().disable(gouraudShaded);
        face.getRenderOptions().disable(meshOnly);
      }
    }
   
    if (keyHasBeenPressed(KeyEvent.VK_G)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.setColor(new FeynColor((((Model3dTexturedFace) face).getTextureData().getAverageColor())));
        face.getRenderOptions().toggle(gouraudShaded);
        face.getRenderOptions().disable(flatShaded);
        face.getRenderOptions().disable(meshOnly);
      }
    }
  
    if (keyHasBeenPressed(KeyEvent.VK_T)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.setColor(new FeynColor((((Model3dTexturedFace) face).getTextureData().getAverageColor())));
        face.getRenderOptions().toggle(textured);
      }
    }
    
    if (keyHasBeenPressed(KeyEvent.VK_M)) { 
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.setColor(FeynColor.black);
        face.getRenderOptions().toggle(meshOnly);
      }
    }
    
    if (keyHasBeenPressed(KeyEvent.VK_C)) {
      inputDelay = 50;
      for (Model3dFace face : teapot.getFaces()) {
        face.setColor(new FeynColor((((Model3dTexturedFace) face).getTextureData().getAverageColor())));
        face.getRenderOptions().toggle(applyLightingColor);
      }
    }
    
    if (keyHasBeenPressed(KeyEvent.VK_S)) {
      inputDelay = 50;
      screenshot("./screenshots/"+System.currentTimeMillis()+".png");
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