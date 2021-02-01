package demo3d;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.collision.CollisionDetection3d;
import rcs.feyn.three.collision.CollisionInfo3d;
import rcs.feyn.three.collision.models.CollidableModel3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dFace;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.models.Model3dUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.AnimationTimer;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.utils.struct.FeynLinkedList;

public class CollidingRocks extends Demo3d {
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private XORShift xorShift = XORShift.getInstance();
  
  private FeynCollection<CollidableModel3d> rocks = new FeynLinkedList<>();
  private FeynCollection<Model3d> shards = new FeynLinkedList<>();
  
  private AnimationTimer addRockTimer = new AnimationTimer(this::addNewRocks, 1000);
  
  public CollidingRocks() {
    super();
  }
  
  @Override
  public void initialize() {
    super.initialize();
    
    super.setBackgroundColor(FeynColor.black);
    
    FeynApp3d.getCamera().translate(0, 0, 10);
    
    FeynApp3d.getRepository().add(rocks);
    FeynApp3d.getRepository().add(shards);
    
    var lightSource = new ConstantLightSource3d();
    lightSource.setPosition(new Vector3d(0, 0, 10));
    FeynApp3d.setDiffuseLightSource(lightSource);
    FeynApp3d.setAmbientLight(new AmbientLightSource3d(0.05)); 
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() { 
    controlCamera();
    addRockTimer.run();
    animateRocks();
    animateShards();
    rocks.forEachPair(rockCollisionHandler);
  }
  
  private void addNewRocks() {
    CollidableModel3d rock1 = getNewRock();
    rock1.setPosition(-15, xorShift.randomDouble(-10, 10), xorShift.randomDouble(-5, 5));
    rock1.setVelocity(0.1, 0, 0);
    
    CollidableModel3d rock2 = getNewRock();
    rock2.setPosition(15, xorShift.randomDouble(-10, 10), xorShift.randomDouble(-5, 5));
    rock2.setVelocity(-0.1, 0, 0);

    rocks.add(rock1);
    rocks.add(rock2);
  }
  
  private CollidableModel3d getNewRock() {
  	var rock = (CollidableModel3d) Model3dFactory.dodecahedron(0.5)
  			.setOuterBoundingObject(new BoundingSphere3d(0.5))
  			.build();
  	
  	Model3dUtils.setOptions(rock, EnumSet.of(RenderOptions3d.Option.gouraudShaded), EnumSet.of(RenderOptions3d.Option.cullIfBackface));
  	
  	Model3dUtils.deform(rock, 0.1);
  	rock.setColor(FeynColor.rosyBrown);
  	return rock;
  }
  
  private void animateRocks() {
  	rocks.forEach(rock -> {
  		rock.spin(Vector3d.Z_AXIS, 1);
    	rock.move();
    	
    	boolean outOfBounds = Stream.of(rock.getPosX(), rock.getPosY(), rock.getPosZ())
		    	.map(Math::abs)
		    	.anyMatch(pos -> pos > 50);
    	
    	if (outOfBounds) {
    		rock.destroy();
    	}
    });
  }
  
  private void animateShards() {
  	shards.forEach(shard -> {
    	shard.move();
    	for (Model3dFace face : shard.getFaces()) {
  			var newColor = face.getColor().fadeTo(0.999);
  			if (newColor.getAlpha() < 5) {
  				shard.destroy();
  			} else {
  				face.setColor(newColor);
  			}
  		}
    });
  }
  
  private final BiConsumer<CollidableModel3d, CollidableModel3d> rockCollisionHandler = new BiConsumer<CollidableModel3d, CollidableModel3d>() {

    @Override
    public void accept(CollidableModel3d t, CollidableModel3d u) { 
      CollisionInfo3d ci1 = CollisionDetection3d.computeCollision(t, u);

      if (ci1 == null) {
        return;
      }
      
      t.destroy();
      u.destroy();

      addNewShards(t);
      addNewShards(u);
    }
    
    private void addNewShards(Model3d rock) {
    	var newShards = Model3dUtils.partition3d(rock);
    	for (var shard : newShards) {
        Model3dUtils.setOptions(
            shard, 
            EnumSet.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.bothSidesShaded), 
            null);
        double speed = rock.getVelocity().length();
        Vector3d velocity = Vector3d.getRandomUnitVector().mulLocal(speed);
  			shard.setVelocity(velocity);
    		shards.add(shard);
    	}
    }
  };

  public static void main(String[] args) {
    var frame = new FeynFrame(1000, 800, "Rain Demo", true, false);
    var demo = new CollidingRocks();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}