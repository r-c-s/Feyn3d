package demo3d;

import java.util.EnumSet;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;
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

public class FallingRocks extends Demo3d {
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private XORShift xorShift = XORShift.getInstance();
  
  private Grid ground = new Grid(10, 10, 10);
  
  private FeynCollection<Model3d> rocks = new FeynLinkedList<>();
  private FeynCollection<Model3d> shards = new FeynLinkedList<>();
  
  private AnimationTimer addRockTimer = new AnimationTimer(this::addNewRock, 1000);
  
  public FallingRocks() {
    super();
  }
  
  @Override
  public void initialize() {
    super.initialize();
    
    super.setBackgroundColor(FeynColor.white);
    
    FeynApp3d.getCamera().translate(0, 4, 10);
    
    ground.setColor(FeynColor.darkOliveGreen);
    
    FeynApp3d.getRepository().add(ground);
    FeynApp3d.getRepository().add(rocks);
    FeynApp3d.getRepository().add(shards);
    
    var lightSource = new ConstantLightSource3d();
    lightSource.setPosition(new Vector3d(5, 5, 5));
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
  }
  
  private void addNewRock() {
  	var rock = Model3dFactory.dodecahedron(0.5)
  			.setPosition(new Vector3d(xorShift.randomDouble(-5, 5), 15, xorShift.randomDouble(-5, 5)))
  			.build();
  	
  	Model3dUtils.setOptions(
  			rock, 
  			EnumSet.of(RenderOptions3d.Option.gouraudShaded), 
  			EnumSet.of(RenderOptions3d.Option.cullIfBackface));
  	
  	Model3dUtils.deform(rock, 0.1);

  	rock.setColor(FeynColor.rosyBrown);
  	rock.setVelocity(0, -0.1, 0);
  	rocks.add(rock);
  }
  
  private void animateRocks() {
  	rocks.forEach(rock -> {
  		rock.spin(Vector3d.Z_AXIS, 0.01);
    	rock.move();
    	if (rock.getPosY() < 0) {
    		rock.destroy();
    		addNewShards(rock);
    	}
    });
  }
  
  private void animateShards() {
  	shards.forEach(shard -> {
    	if (shard.getPosition().y() < 0.05) {
    		shard.setPosY(0.04);
    		shard.setVelocity(0, 0, 0);
    	} else { 
    		shard.accelerate(0, -0.01, 0);
      	shard.move();
    	}
    	
    	if (shard.getVelocity().equals(Vector3d.ZERO)) {
    		for (Model3dFace face : shard.getFaces()) {
    			var newColor = face.getColor().fadeTo(0.999);
    			if (newColor.getAlpha() < 5) {
    				shard.destroy();
    			} else {
    				face.setColor(newColor);
    			}
    		}
    	}
    });
  }
  
  private void addNewShards(Model3d rock) {
  	var newShards = Model3dUtils.partition3d(rock);
  	for (var shard : newShards) {
      Model3dUtils.setOptions(
          shard, 
          EnumSet.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.bothSidesShaded), 
          null);
  		shard.setPosition(rock.getPosX(), 0.1, rock.getPosZ());
			shard.setVelocity(
					xorShift.randomDouble(-0.05, 0.05), 
					xorShift.randomDouble(0.1, 0.2), 
					xorShift.randomDouble(-0.05, 0.05));
  		shards.add(shard);
  	}
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Rain Demo", true, false);
    var demo = new FallingRocks();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
