package demo3d;

import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.collision.BoundingBox3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.collision.Collidable3d;
import rcs.feyn.three.collision.CollisionDetection3d;
import rcs.feyn.three.collision.CollisionHandler3d;
import rcs.feyn.three.collision.CollisionInfo3d;
import rcs.feyn.three.collision.CollisionUtils3d;
import rcs.feyn.three.collision.InelasticCollision3d;
import rcs.feyn.three.entities.models.CollidableModel3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.utils.XORShift;
import rcs.feyn.utils.struct.FeynArray;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Vector3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class Gumballs extends Demo3d {  

  @Serial
  private static final long serialVersionUID = 1L; 
  
  private static final int NUM_BALLS = 100;

  private FeynCollection<CollidableModel3d> spheres = new FeynArray<>(NUM_BALLS); 
 
  private final CollidableModel3d cube = (CollidableModel3d) Model3dFactory
      .cube(2)
      .setOuterBoundingObject(new BoundingBox3d(2, 2, 2))
      .build();

  private final double energyFactor = 0.5;
  
  private final GumballWithCubeCollisionHandler gumballWithCubeCollisionHandler = 
      new GumballWithCubeCollisionHandler(energyFactor);
  
  private final GumballWithGumballCollisionHandler gumballWithGumballCollisionHandler = 
      new GumballWithGumballCollisionHandler(energyFactor, gumballWithCubeCollisionHandler);
  
  @Override
  protected void initialize() {
    super.initialize();
    
    setBackgroundColor(FeynColor.superDarkGray);
    wzc.setAmount(0.2);
    
    XORShift xor = XORShift.getInstance();
    
    for (int i = 0; i < NUM_BALLS; i++) {
      double radius = xor.randomDouble(0.05, 0.2);
      
      var sphere = (CollidableModel3d) Model3dFactory
          .icosphere(radius, 1)
          .setOuterBoundingObject(new BoundingSphere3d(radius))
          .build();
      
      sphere.setPosition(
           xor.randomDouble(-0.5, 0.5), 
           xor.randomDouble(-0.5, 0.5), 
           xor.randomDouble(-0.51, 0.5));
      
      sphere.setColorToAllFaces(FeynColor.randomColor());
      sphere.setMass(radius);

      Model3dUtils.setOptions(
          sphere, 
          Set.of(flatShaded, applyLightingColor), 
          Set.of(gouraudShaded));

      spheres.add(sphere);
    }

    cube.setColorToAllFaces(new FeynColor(255, 255, 255, 255));
    cube.getOuterBoundingObject().inverse();
    Model3dUtils.setOptions(
        cube,
        Set.of(meshOnly), 
        Set.of(cullIfBackface, flatShaded, gouraudShaded));
    
    FeynRuntime.getRepository().add(cube);
    FeynRuntime.getRepository().add(spheres);

    camera.translate(0, 0, 2.5);
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(0.5));
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.8));
  }

  @Override
  public void pausedLoop() {
    controlCamera();
  }
  
  private final Vector3d[] delta = new Vector3d[NUM_BALLS]; 

  @Override
  public void runningLoop() { 
    controlCamera();

    // saves old position in order to figure out how much the ball needs to spin later
    spheres.forEachWithIndex((sphere, i) -> {   
      delta[i] = sphere.getPosition();
      sphere.animate();
    }); 

    // check for collisions
    CollisionUtils3d.forEachCollision(
        spheres, 
        cube, 
        gumballWithCubeCollisionHandler,
        sphere -> sphere.accelerate(camera.getUpVector().mul(-0.0015)));
    
    CollisionUtils3d.forEachCollision(spheres, gumballWithGumballCollisionHandler);

    // figure out how much the ball needs to spin, given deltas
    spheres.forEachWithIndex((sphere, i) -> {
      Vector3d d = sphere.getPosition().subLocal(delta[i]);
      Vector3d axis = d.crossProd(camera.getUpVector());
      double radians = (-d.length() * MathConsts.PI) 
          / (MathConsts.TWO_PI * ((BoundingSphere3d) sphere.getOuterBoundingObject()).getRadius());
      sphere.rotate(axis, radians);
    });
  }  
  
  private class GumballWithCubeCollisionHandler implements CollisionHandler3d<CollidableModel3d, CollidableModel3d> {
    
    private final double energyFactor;

    public GumballWithCubeCollisionHandler(double energyFactor) {
      this.energyFactor = energyFactor;
    }

    @Override
    public void handleCollision(CollidableModel3d sphere, CollidableModel3d cube, CollisionInfo3d ci) {
      Vector3d normal = ci.getNormal();
      double overlap = ci.getOverlap(); 
     
      sphere.translate(normal.mul(overlap));
      
      double dotProd = normal.dotProd(sphere.getVelocity());
      
      if (dotProd < 0) {
        sphere.accelerate(normal.mul(dotProd).mul(-1.0-energyFactor)); 
      }
    }
  }
  
  private class GumballWithGumballCollisionHandler implements CollisionHandler3d<CollidableModel3d, CollidableModel3d> {
    
    private final CollisionHandler3d<Collidable3d, Collidable3d> ec;

    private final GumballWithCubeCollisionHandler gumballWithCubeCollisionHandler;

    public GumballWithGumballCollisionHandler(
        double energyFactor, 
        GumballWithCubeCollisionHandler gumballWithCubeCollisionHandler) {
      this.ec = new InelasticCollision3d(energyFactor);
      this.gumballWithCubeCollisionHandler = gumballWithCubeCollisionHandler;
    }

    @Override
    public void handleCollision(CollidableModel3d sphereA, CollidableModel3d sphereB, CollisionInfo3d ci) {
      CollisionUtils3d.fixOverlap(sphereA, sphereB, ci);   
      
      CollisionInfo3d cia = CollisionDetection3d.computeCollision(sphereA, cube);
      if (cia != null) {
        gumballWithCubeCollisionHandler.handleCollision(sphereA, cube, cia);
        sphereB.translate(cia.getNormal().mul(cia.getOverlap()));
      }
      
      CollisionInfo3d cib = CollisionDetection3d.computeCollision(sphereB, cube);
      if (cib != null) {
        gumballWithCubeCollisionHandler.handleCollision(sphereB, cube, cib);
        sphereA.translate(cib.getNormal().mul(cib.getOverlap()));
      }
      
      ec.handleCollision(sphereA, sphereB, ci); 
    }
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Gumballs", true, false);
    var demo = new Gumballs();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  } 
}
