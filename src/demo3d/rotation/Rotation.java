package demo3d.rotation;

import java.awt.event.KeyEvent;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.Demo3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.three.render.models.Model3d;
import rcs.feyn.three.render.models.Model3dFace;
import rcs.feyn.three.render.models.Model3dFactory;
import rcs.feyn.three.render.primitives.Line3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import demo3d.Grid;

public class Rotation extends Demo3d {
  
  private static final long serialVersionUID = 1L; 
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private Model3d obj = Model3dFactory.dodecahedron(0.25).addColor(FeynColor.white).build();
  
  private Line3d up = new Line3d(Vector3d.ZERO, obj.getUpVector());
  private Line3d sd = new Line3d(Vector3d.ZERO, obj.getSideVector());
  private Line3d fw = new Line3d(Vector3d.ZERO, obj.getForwardVector());
  
  private Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private Grid xyPlane = new Grid(2, 2, 2);
  private Grid xzPlane = new Grid(2, 2, 2); 
  private Grid zyPlane = new Grid(2, 2, 2); 

  private final Matrix44 xTransform = Matrix44.createRotateMatrix(Vector3d.ZERO, Vector3d.X_AXIS, 1);
  private final Matrix44 yTransform = Matrix44.createRotateMatrix(Vector3d.ZERO, Vector3d.Y_AXIS, 1);
  private final Matrix44 zTransform = Matrix44.createRotateMatrix(Vector3d.ZERO, Vector3d.Z_AXIS, 1);
  
  private final double ANG_VEL = 2;
   
  private ConstantLightSource3d lightSource; 
  
  public Rotation() {
    super();
  }
  
  @Override
  public void initialize() {
    super.initialize();
    
    super.setBackgroundColor(FeynColor.bisque4);
    
    obj.translate(0.5, 0.5, 0.5);
    up.translate(obj.getPosition()); 
    sd.translate(obj.getPosition()); 
    fw.translate(obj.getPosition()); 
    
    xyPlane.rotate(Vector3d.X_AXIS, 90);
    zyPlane.rotate(Vector3d.Z_AXIS, 90);
    
    xyPlane.setColor(new FeynColor(255, 255, 0, 50));  
    xzPlane.setColor(new FeynColor(255, 0, 255, 50));  
    zyPlane.setColor(new FeynColor(0, 255, 255, 50)); 

    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    obj.setColor(FeynColor.white);
    for (Model3dFace face : obj.getFaces()) {
      face.getRenderOptions().enable(RenderOptions3d.Option.cullIfBackface);
      face.getRenderOptions().enable(RenderOptions3d.Option.gouraudShaded);
    }
    
    up.setColor(FeynColor.green);
    sd.setColor(FeynColor.red);
    fw.setColor(FeynColor.blue);

    FeynApp3d.getRepository().add(obj);
    FeynApp3d.getRepository().add(xyPlane);
    FeynApp3d.getRepository().add(xzPlane);
    FeynApp3d.getRepository().add(zyPlane);
    FeynApp3d.getRepository().add(x);
    FeynApp3d.getRepository().add(y);
    FeynApp3d.getRepository().add(z);
    FeynApp3d.getRepository().add(up);
    FeynApp3d.getRepository().add(sd);
    FeynApp3d.getRepository().add(fw);

    camera.translate(0, 0, 2.5);
    
    lightSource = new ConstantLightSource3d(1);  
    FeynApp3d.setDiffuseLightSource(lightSource); 
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
    
    lightSource.setPosition(camera.getPosition());
    
    Matrix44 transform = new Matrix44(); 
    Vector3d position  = obj.getPosition();
     
    if (pressed[KeyEvent.VK_X]) {
      if (pressed[KeyEvent.VK_SHIFT]) {
        transform.mulLocal(Matrix44.createRotateMatrix(position, obj.getSideVector(), ANG_VEL)); 
      } else {
        transform.mulLocal(xTransform); 
      }
    }

    if (pressed[KeyEvent.VK_Y]) {
      if (pressed[KeyEvent.VK_SHIFT]) {
        transform.mulLocal(Matrix44.createRotateMatrix(position, obj.getUpVector(), ANG_VEL)); 
      } else {
        transform.mulLocal(yTransform); 
      }
    }

    if (pressed[KeyEvent.VK_Z]) {
      if (pressed[KeyEvent.VK_SHIFT]) {
        transform.mulLocal(Matrix44.createRotateMatrix(position, obj.getForwardVector(), ANG_VEL)); 
      } else {
        transform.mulLocal(zTransform); 
      }
    }  
        
    obj.transform(transform);
    up.transform(transform);
    sd.transform(transform);
    fw.transform(transform);
  } 

  public static void main(String[] args) {
    FeynFrame frame = new FeynFrame(800, 800, "Rotation Demo", true, false);
    Demo3d demo = new Rotation();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
