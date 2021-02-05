package demo3d;

import java.io.Serial;
import java.util.EnumSet;

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
import rcs.feyn.three.collision.models.CollidableModel3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.entities.primitives.Line3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.XORShift;
import rcs.feyn.utils.struct.FeynArray;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;

public class Gumballs extends Demo3d {  

	@Serial
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private static final int NUM_BALLS = 100;

  private FeynCollection<CollidableModel3d> spheres = new FeynArray<>(NUM_BALLS); 
 
  private final CollidableModel3d cube = (CollidableModel3d) Model3dFactory
      .cube(2)
      .setOuterBoundingObject(new BoundingBox3d(2, 2, 2))
      .build();
  
  private final Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private final Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private final Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS); 

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
      
      sphere.setPosition(new Vector3d(
           xor.randomDouble(-0.5, 0.5), 
           xor.randomDouble(-0.5, 0.5), 
           xor.randomDouble(-0.5, 0.5)));
     
      sphere.setVelocity(new Vector3d(
           xor.randomDouble(-0.02, 0.02), 
           xor.randomDouble(-0.02, 0.02), 
           xor.randomDouble(-0.02, 0.02)));
      
      sphere.setColor(FeynColor.randomColor());
      sphere.setMass(radius);

      spheres.add(sphere);
    }

    cube.setColor(new FeynColor(255, 255, 255, 255));
    cube.getOuterBoundingObject().inverse();
    Model3dUtils.setOptions(
        cube,
        EnumSet.of(RenderOptions3d.Option.meshOnly), 
        EnumSet.of(RenderOptions3d.Option.cullIfBackface));
    
    FeynApp3d.getRepository().add(cube);
    FeynApp3d.getRepository().add(spheres);  
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);   
    
    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);

    camera.translate(0, 0, 2.5);
    FeynApp3d.setDiffuseLightSource(new ConstantLightSource3d());
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.5));
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
      sphere.move();
    }); 

    // check for collisions
    CollisionUtils3d.forEachCollision(
        spheres, 
        cube, 
        gumballWithCubeCollisionHandler,
        sphere -> sphere.accelerate(camera.getUpVector().mul(-0.0015)));
    CollisionUtils3d.forEachCollision(spheres, gumballWithGumballCollisionHandler);

    // figure out how much the ball needs to spin
    spheres.forEachWithIndex((sphere, i) -> {
      Vector3d d = sphere.getPosition().subLocal(delta[i]);
      Vector3d axis = d.crossProd(camera.getUpVector());
      double radians = (-d.length() * MathConsts.HALF_PI) 
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
