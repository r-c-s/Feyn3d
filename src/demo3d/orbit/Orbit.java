package demo3d.orbit;

import java.util.EnumSet;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.physics.Gravity3d;
import rcs.feyn.three.Demo3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.VariableIntensityLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;

public class Orbit extends Demo3d {

  private static final long serialVersionUID = 1L;
  
  static { 
    new TrigLookUp(0.1);
  }

  private static final double sunRadius = 0.8;
  private static final double planetRadius = 0.4;

  private Model3d sun = Model3dFactory
      .icosphere(sunRadius, 1)
      .addColor(FeynColor.yellow)
      .build();
  
  private Gravity3d sunGravity;
  
  private Model3d[] planets = new Model3d[10];
  private Gravity3d[] planetGravities = new Gravity3d[planets.length];
  
  @Override
  protected void initialize() {
    super.initialize(); 
    
    setBackgroundColor(FeynColor.bisque);
    
    sun.setPosition(new Vector3d(0, 0, 0));
    sunGravity = new Gravity3d(sun.getPosition(), sunRadius, sunRadius * 0.01);
    Model3dUtils.setOptions(
        sun, 
        EnumSet.of(RenderOptions3d.Option.cullIfBackface, RenderOptions3d.Option.gouraudShaded), 
        EnumSet.of(RenderOptions3d.Option.lighted));
    
    FeynApp3d.getRepository().add(sun);
    
    for (int i = 0; i < planets.length; i++) {
      Model3d planet = Model3dFactory
        .icosphere(planetRadius, 0.5 + 0.5 * XORShift.getInstance().randomDouble())
        .addColor(FeynColor.blue)
        .build();
      planet.setPosition(new Vector3d(1, 0, 0).mul(i * 5 * planetRadius + 5 * sunRadius));
      planet.setVelocity(new Vector3d(0, 1, 0).mul(0.04 + 0.06 * XORShift.getInstance().randomDouble(-1, 1)));
      Model3dUtils.setOptions(
          planet, 
          EnumSet.of(RenderOptions3d.Option.cullIfBackface, RenderOptions3d.Option.gouraudShaded),
          null);
      
      FeynApp3d.getRepository().add(planet);
      
      Gravity3d gravity = new Gravity3d(planet.getPosition(), planetRadius, planetRadius * 0.001);
      
      planets[i] = planet;
      planetGravities[i] = gravity;
    }

    camera.translate(0, 0, 10);
    
    FeynApp3d.setDiffuseLightSource(new VariableIntensityLightSource3d(50));
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
    
    for (int i = 0; i < planets.length; i++) {
      Model3d planet = planets[i];
      planet.accelerate(sunGravity.getAccelerationAt(planet.getPosition()));
      planetGravities[i].setPosition(planet.getPosition());
      
      for (int j = 0; j < planetGravities.length; j++) {
        sun.accelerate(planetGravities[j].getAccelerationAt(sun.getPosition()));
        if (j == i) continue;
        planet.accelerate(planetGravities[j].getAccelerationAt(planet.getPosition()));
      }
      
      planet.move();
    }
    sun.move();
    FeynApp3d.getDiffuseLightSource().setPosition(sun.getPosition());
  }

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Center of mass Demo", true, false);
    Demo3d demo = new Orbit();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
