package demo3d;

import java.io.Serial;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.collision.BoundingSphere3d;
import rcs.feyn.three.collision.CollisionHandler3d;
import rcs.feyn.three.collision.CollisionInfo3d;
import rcs.feyn.three.collision.CollisionUtils3d;
import rcs.feyn.three.entities.Rotation3d;
import rcs.feyn.three.entities.models.CollidableModel3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.utils.AnimationTimer;
import rcs.feyn.utils.XORShift;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.utils.struct.FeynLinkedList;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class CollidingRocks extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L; 
  
  private XORShift xorShift = XORShift.getInstance();
  
  private FeynCollection<CollidableModel3d> rocks = new FeynLinkedList<>();
  private FeynCollection<Model3d> shards = new FeynLinkedList<>();
  
  private AnimationTimer addRockTimer = new AnimationTimer(this::addNewRocks, 1000);
  
  private RockCollisionHandler rockCollisionHandler = new RockCollisionHandler();
  
  public CollidingRocks() {
    super();
  }
  
  @Override
  public void initialize() {
    super.initialize();
    
    super.setBackgroundColor(FeynColor.black);
    
    camera.translate(0, 0, 10);
    
    FeynRuntime.getRepository().add(rocks);
    FeynRuntime.getRepository().add(shards);
    
    var lightSource = new ConstantLightSource3d(1);
    lightSource.setPosition(0, 0, 10);
    FeynRuntime.addDiffuseLightSource(lightSource);
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.05)); 
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
    CollisionUtils3d.forEachCollision(rocks, rockCollisionHandler);
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
    
    Model3dUtils.setOptions(
        rock, 
        EnumSet.of(gouraudShaded), 
        EnumSet.of(cullIfBackface));
    
    Model3dUtils.deform(rock, 0.1);
    rock.setColor(FeynColor.rosyBrown);
    return rock;
  }
  
  private void animateRocks() {
    rocks.forEach(rock -> {
      rock.spin(Vector3d.Z_AXIS, 0.05);
      rock.animate();
      
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
      shard.animate();
      
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
  
  private class RockCollisionHandler implements CollisionHandler3d<CollidableModel3d, CollidableModel3d> {

    @Override
    public void handleCollision(CollidableModel3d rock, CollidableModel3d projectile, CollisionInfo3d ci) {
      rock.destroy();
      projectile.destroy();

      addNewShards(rock);
    }

    private void addNewShards(Model3d rock) {
      var newShards = Model3dUtils.partition3d(rock);
      for (var shard : newShards) {
        Model3dUtils.setOptions(
            shard, 
            Set.of(flatShaded), 
            Set.of());
        
        double speed = rock.getVelocity().length() * (1 + XORShift.getInstance().randomDouble(-1, 1));
        Vector3d velocity = Vector3d.getRandomUnitVector().mulLocal(speed);
        shard.setVelocity(velocity);
        
        Rotation3d rotation = Rotation3d.spin(Vector3d.getRandomUnitVector(), 0.1);
        shard.setRotation(rotation);
        
        shards.add(shard);
      }
    }
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(1000, 800, "Rain Demo", true, false);
    var demo = new CollidingRocks();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
