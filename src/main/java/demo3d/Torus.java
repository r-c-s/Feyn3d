package demo3d;

import java.io.Serial;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;

public class Torus extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L; 
  
  private Model3d torus = Model3dFactory
      .torus(1, 2, 15, 15)
      .build();

  @Override
  protected void initialize() {
    super.initialize();
    
    setBackgroundColor(FeynColor.black);
    
    FeynRuntime.getRepository().add(torus);
    
    camera.setPosition(torus.getPosition());
    camera.translate(0, 0, 4);
    
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.2));
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(1));
    FeynRuntime.getDiffuseLightSources()[0].setPosition(0, 0, 4);
  }
  
  @Override
  public void runningLoop() {
    controlCamera();  
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(600, 600, "Torus Demo", true, false);
    var demo = new Torus();
    frame.add("Center", demo);
    frame.setVisible(true); 
    frame.setLocationRelativeTo(null);
    demo.init();
  }
}
