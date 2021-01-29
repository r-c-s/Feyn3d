package demo3d.rain;

import java.util.Iterator;

import demo3d.Demo3d;
import demo3d.Grid;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.XORShift;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.render.primitives.Line3d;
import rcs.feyn.three.render.primitives.Polygon3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.utils.struct.DoublyLinkedList;
import rcs.feyn.utils.struct.FeynArray;
import rcs.feyn.utils.struct.FeynCollection;
import rcs.feyn.utils.struct.FeynLinkedList;

public class BoxOfRain extends Demo3d {
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private XORShift xorShift = XORShift.getInstance();
  
  private Grid ground = new Grid(10, 10, 10);
  
  private FeynCollection<Line3d> raindrops = new FeynLinkedList<>();
  private FeynCollection<Polygon3d> waves = new FeynLinkedList<>();
  
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
  }

  @Override
  public void pausedLoop() { 
    controlCamera();
  }
  
  @Override
  public void runningLoop() { 
    controlCamera();

    for (int i = 0; i < 10; i++) {
    	Vector3d position = new Vector3d(xorShift.randomDouble(-5, 5), 10, xorShift.randomDouble(-5, 5));
    	Line3d raindrop = new Line3d(position, position.add(0, 0.1, 0));
    	raindrop.setColor(FeynColor.white);
    	raindrop.setVelocity(new Vector3d(0, -0.1, 0));
    	raindrops.add(raindrop);
    }
    
    raindrops.forEach(raindrop -> {
    	raindrop.move();
    	if (raindrop.getA().y() < 0) {
    		raindrop.destroy();
    		Polygon3d wave = Polygon3d.regularPolygon(0.1, 10);
    		wave.setPosition(raindrop.getA().x(), 0.01, raindrop.getA().z());
    		wave.setColor(FeynColor.white);
    		wave.getRenderingOptions().enable(RenderOptions3d.Option.meshOnly);
    		waves.add(wave);
    	}
    });
    
    waves.forEach(wave -> {
    	double radius = wave.getVertices()[0].distance(wave.getCenterOfMass());
    	wave.scale(1 + 0.01/(radius / 0.5));
    	wave.setColor(wave.getColor().fade(1.5 - (radius / 0.5)));
    	if (radius > 0.5) {
    		wave.destroy();
    	}
    });
  } 

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Rain Demo", true, false);
    Demo3d demo = new BoxOfRain();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
