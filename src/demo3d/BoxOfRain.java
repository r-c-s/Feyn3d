package demo3d;

import java.io.Serial;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.render.primitives.Line3d;
import rcs.feyn.three.render.primitives.Point3d;
import rcs.feyn.three.render.primitives.Polygon3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.utils.struct.FeynLinkedList;

public class BoxOfRain extends Demo3d {

	@Serial
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private XORShift xorShift = XORShift.getInstance();
  
  private Grid ground = new Grid(10, 10);
  
  private FeynCollection<Line3d> raindrops = new FeynLinkedList<>();
  private FeynCollection<Polygon3d> waves = new FeynLinkedList<>();
  private FeynCollection<Point3d> splashes = new FeynLinkedList<>();
  
  public BoxOfRain() {
    super();
  }
  
  @Override
  public void initialize() {
    super.initialize();
    
    super.setBackgroundColor(FeynColor.black);
    
    FeynApp3d.getCamera().translate(0, 4, 10);
    
    ground.setColor(FeynColor.blueViolet);
    
    FeynApp3d.getRepository().add(ground);
    FeynApp3d.getRepository().add(raindrops);
    FeynApp3d.getRepository().add(waves);
    FeynApp3d.getRepository().add(splashes);
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() { 
    controlCamera();

    addNewRaindrops();
    animateRaindrops();
    animateWaves();
    animateSplashes();
  } 
  
  private void addNewRaindrops() {
    for (int i = 0; i < 1; i++) {
    	var position = new Vector3d(xorShift.randomDouble(-5, 5), 10, xorShift.randomDouble(-5, 5));
    	var raindrop = new Line3d(position, position.add(0, 0.1, 0));
    	raindrop.setColor(FeynColor.white);
    	raindrop.setVelocity(new Vector3d(0, -0.1, 0));
    	raindrops.add(raindrop);
    }
  }
  
  private void animateRaindrops() {
  	raindrops.forEach(raindrop -> {
    	raindrop.move();
    	if (raindrop.getA().y() < 0) {
    		raindrop.destroy();
    		addNewWave(raindrop);
    		addNewSplash(raindrop);
    	}
    });
  }
  
  private void animateWaves() {
  	double maxRadius = 2;
  	waves.forEach(wave -> {
    	double radius = wave.getVertices()[0].distance(wave.getCenterOfMass());
    	wave.scale(1 + 0.005/(radius / maxRadius));
    	wave.setColor(wave.getColor().fadeTo(1 - (radius / maxRadius)));
    	if (radius > maxRadius) {
    		wave.destroy();
    	}
    });
  }
  
  private void addNewWave(Line3d raindrop) {
		var wave = Polygon3d.regularPolygon(0.1, 10);
		wave.setPosition(raindrop.getA().x(), 0.01, raindrop.getA().z());
		wave.setColor(FeynColor.white.fadeTo(0.9));
		wave.getRenderingOptions().enable(RenderOptions3d.Option.meshOnly);
		waves.add(wave);
  }
  
  private void addNewSplash(Line3d raindrop) {
  	for (int i = 0; i < 5; i++) {
  		var position = new Vector3d(raindrop.getA().x(), 0.01, raindrop.getA().z());
			var splash = new Point3d(position);
			splash.setVelocity(
					xorShift.randomDouble(-0.02, 0.02),
					0.1, 
					xorShift.randomDouble(-0.02, 0.02));
  		splash.setColor(FeynColor.white);
  		splashes.add(splash);
  	}
  }
  
  private void animateSplashes() {
  	splashes.forEach(splash -> {
  		splash.accelerate(0, -0.01, 0);
    	splash.move();
    	if (splash.getPosition().y() < 0) {
    		splash.destroy();
    	}
    });
  }

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 800, "Rain Demo", true, false);
    var demo = new BoxOfRain();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}