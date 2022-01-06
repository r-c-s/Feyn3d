package demo3d;

import java.io.Serial;

import demo3d.models.Grid;
import demo3d.models.PineTree;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.utils.XORShift;

public class PineForest extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L;
  
  private Grid ground = new Grid(
      10, 10, 5,
      Model3dUtils.getImageData(System.getProperty("user.dir") + "/textures/forest-ground.jpg"));

  private PineTree[] trees = new PineTree[10];
  
  @Override
  protected void initialize() {
    super.initialize(); 
    wzc.setAmount(0.2);
    setBackgroundColor(FeynColor.skyBlue);
    
    FeynRuntime.getRepository().add(ground);
    
    for (int i = 0; i < trees.length; i++) {
      Vector3d position = new Vector3d(
          XORShift.getInstance().randomInt(-4, 4),
          0,
          XORShift.getInstance().randomInt(-4, 4));
      PineTree tree = new PineTree(position, XORShift.getInstance().randomInt(5, 12));
      trees[i] = tree;
      FeynRuntime.getRepository().add(trees[i]);
    }

    camera.translate(0, 4, 10);

    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.8));
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(1)); 
    FeynRuntime.getDiffuseLightSources()[0].setPosition(new Vector3d(5, 10, 5)); 
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() {
    controlCamera();
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Pine Demo", true, false);
    var demo = new PineForest();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}