package demo3d;

import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dTexturedFace;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.AnimationTimer;
import rcs.feyn.utils.XORShift;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.utils.struct.FeynLinkedList;

public class FallingRocks extends Demo3d {

	@Serial
  private static final long serialVersionUID = 1L; 

  private Raster rockTexture = Model3dUtils.getImageData(System.getProperty("user.dir") + "/textures/texture2.jpg");
  
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

    Model3dUtils.setOptions(
        ground, 
        Set.of(RenderOptions3d.Option.meshOnly), 
        Set.of());
    
    FeynApp3d.getRepository().add(ground);
    FeynApp3d.getRepository().add(rocks);
    FeynApp3d.getRepository().add(shards);
    
    var lightSource = new ConstantLightSource3d(1);
    lightSource.setPosition(new Vector3d(5, 5, 5));
    FeynApp3d.addDiffuseLightSource(lightSource);
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
  	    .setTextureData(rockTexture)
  			.setPosition(new Vector3d(xorShift.randomDouble(-5, 5), 15, xorShift.randomDouble(-5, 5)))
  			.build();
  	
  	Model3dUtils.setOptions(
  			rock, 
  			Set.of(RenderOptions3d.Option.flatShaded), 
  			Set.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.applyLightingColor));
  	
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
    		  Model3dTexturedFace tFace = (Model3dTexturedFace) face;
    			int newAlpha = MathUtils.roundToInt(tFace.getAlpha() - 1);
          tFace.setAlpha(newAlpha);
    			if (newAlpha < 5) {
    				shard.destroy();
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
          Set.of(RenderOptions3d.Option.gouraudShaded, RenderOptions3d.Option.bothSidesShaded), 
          Set.of());
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
