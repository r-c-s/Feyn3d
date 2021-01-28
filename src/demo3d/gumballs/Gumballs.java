package demo3d.gumballs;

import java.util.EnumSet;
import java.util.function.BiConsumer;

import demo3d.Demo3d;
import rcs.feyn.color.FeynColor;
import rcs.feyn.event.DragRotateObject;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.struct.FeynArray;
import rcs.feyn.struct.FeynCollection;
import rcs.feyn.three.collision.BoundingBox3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.collision.Collidable3d;
import rcs.feyn.three.collision.CollisionDetection3d;
import rcs.feyn.three.collision.CollisionHandler3d;
import rcs.feyn.three.collision.CollisionInfo3d;
import rcs.feyn.three.collision.CollisionUtils3d;
import rcs.feyn.three.collision.InelasticCollision3d;
import rcs.feyn.three.collision.models.CollidableModel3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.primitives.Line3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;

public class Gumballs extends Demo3d {  

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
  
  private final DragRotateObject rotateCube = new DragRotateObject(cube, cube.getPosition());
  
  @Override
  protected void initialize() {
    super.initialize();
    
    setBackgroundColor(FeynColor.superDarkGray);
    addDeltaMouseListener(rotateCube);
    wzc.setAmount(0.2);
    
    XORShift xor = XORShift.getInstance();
    
    for (int i = 0; i < NUM_BALLS; i++) {
      double radius = xor.randomDouble(0.05, 0.2);
      
      CollidableModel3d sphere = (CollidableModel3d) Model3dFactory
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

      Model3dUtils.setOptions(
          sphere,
          EnumSet.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.cullIfBackface), 
          null);

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

    camera.translate(0, 0, 5);
    FeynApp3d.setDiffuseLightSource(new ConstantLightSource3d(1));
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.2));
  }

  @Override
  public void pausedLoop() {
    controlCamera();
  }
  
  private final double energyFactor = 0.8;
  
  private final CollisionHandler3d<Collidable3d, Collidable3d> ec = new InelasticCollision3d(energyFactor);
  
  private void bounce(CollidableModel3d sphere, CollisionInfo3d ci) {
    if (ci == null) {
      return;
    } 
    
    Vector3d normal = ci.getNormal();
    double overlap = ci.getOverlap(); 
   
    sphere.translate(normal.mul(overlap));
    
    double dot = normal.dotProd(sphere.getVelocity());
    
    if (dot < 0) {
      sphere.accelerate(normal.mul(dot).mul(-1.0-energyFactor)); 
    } 
  }
  
  Vector3d[] delta = new Vector3d[NUM_BALLS]; 

  @Override
  public void runningLoop() { 
    controlCamera(); 
    
    spheres.forEachWithIndex((sphere, i) -> {   
      delta[i] = sphere.getPosition();
      
      CollisionInfo3d ci = CollisionDetection3d.computeCollision(sphere, cube);
      
      if (ci != null) { 
        bounce(sphere, ci);
      } else {
        sphere.accelerate(camera.getUpVector().mul(-0.0015));
      }

      sphere.move();
    }); 
    
    spheres.forEachPair(ch);

    spheres.forEachWithIndex((sphere, i) -> { 
      Vector3d d = sphere.getPosition().subLocal(delta[i]);
      
      Vector3d axis = d.crossProd(camera.getUpVector());
      
      sphere.rotate( 
          axis, 
          (-d.norm() * 180) / (MathConsts.TWO_PI * ((BoundingSphere3d) sphere.getOuterBoundingObject()).getRadius()));
    });
  } 
  
  private final BiConsumer<CollidableModel3d, CollidableModel3d> ch = new BiConsumer<CollidableModel3d, CollidableModel3d>() {

    @Override
    public void accept(CollidableModel3d t, CollidableModel3d u) { 
      CollisionInfo3d ci1 = CollisionDetection3d.computeCollision(t, u);

      if (ci1 == null) {
        return;
      } 
      
      CollisionUtils3d.fixOverlap(t, u, ci1);
      
      CollisionInfo3d cia = CollisionDetection3d.computeCollision(t, cube);
      if (cia != null) {
        bounce(t, cia);
        u.translate(cia.getNormal().mul(cia.getOverlap()));
      }
      
      CollisionInfo3d cib = CollisionDetection3d.computeCollision(u, cube);
      if (cib != null) {
        bounce(u, cib);
        t.translate(cib.getNormal().mul(cib.getOverlap()));
      }
      
      ec.handleCollision(t, u, ci1); 
    }
  };

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Gumballs", true, false);
    Demo3d demo = new Gumballs();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  } 
}
