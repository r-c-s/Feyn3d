package demo3d;

import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class Pine extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L;
  
  private Grid ground = new Grid(10, 10, 10);

  // todo: encapsulate cone and branches into a class
  private final Model3d cone = Model3dFactory
      .pyramid(0.1, 10, 5)
      .setColor(FeynColor.sandyBrown)
      .build();
  
  private final Model3d[] branches = new Model3d[500];
  
  @Override
  protected void initialize() {
    super.initialize(); 
    wzc.setAmount(0.2);
    setBackgroundColor(FeynColor.black);
    
    ground.setColor(FeynColor.rosyBrown);

    Model3dUtils.setOptions(
        ground, 
        Set.of(meshOnly), 
        Set.of());
    
    FeynRuntime.getRepository().add(ground);
    
    Model3dUtils.setOptions(
        cone, 
        Set.of(gouraudShaded), 
        Set.of());

    double coneCenterOffset = 10 - MathConsts.SQURT_2 * 10 / 2;
    cone.translate(0, coneCenterOffset, 0);

    FeynRuntime.getRepository().add(cone);
    
    for (int i = 0; i < branches.length; i++) {
      double y = Math.max(1, i / (double) branches.length * 9.5);
      Vector3d position = new Vector3d(0, y, 0);
      double height = 1 - (i / (double) branches.length);
      Model3d branch = Model3dFactory
          .pyramid(0.05, height, 3)
          .setPosition(position)
          .setColor(FeynColor.darkOliveGreen)
          .build();
      
      double branchCenterOffset = height - MathConsts.SQURT_2 * height / 2;
      branch.rotate(
          position.y(y - branchCenterOffset), 
          Vector3d.getRandomUnitVector().y(0), 
          -MathConsts.HALF_PI);
      
      branches[i] = branch;
      FeynRuntime.getRepository().add(branches[i]);
    }

    camera.setPosition(cone.getCenter());
    camera.translate(0, 3, 10);

    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.5));
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(1)); 
    FeynRuntime.getDiffuseLightSources()[0].setPosition(camera.getPosition()); 
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
    var demo = new Pine();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}