package demo3d;

import java.awt.event.KeyEvent;
import java.io.Serial;

import demo3d.models.Grid;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.primitives.Line3d;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Matrices;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class SixDegreesOfFreedom extends Demo3d {

  @Serial
  private static final long serialVersionUID = 1L; 
  
  private Model3d obj = Model3dFactory.dodecahedron(0.25).setColor(FeynColor.white).build();
  
  private Line3d up = new Line3d(Vector3d.ZERO, obj.getUpVector());
  private Line3d sd = new Line3d(Vector3d.ZERO, obj.getSideVector());
  private Line3d fw = new Line3d(Vector3d.ZERO, obj.getForwardVector());
  
  private Line3d x = new Line3d(Vector3d.NEG_X_AXIS, Vector3d.X_AXIS);
  private Line3d y = new Line3d(Vector3d.NEG_Y_AXIS, Vector3d.Y_AXIS);
  private Line3d z = new Line3d(Vector3d.NEG_Z_AXIS, Vector3d.Z_AXIS);

  private Grid xyPlane = new Grid(2, 2, 2);
  private Grid xzPlane = new Grid(2, 2, 2); 
  private Grid zyPlane = new Grid(2, 2, 2); 

  private final double angularVelocity = 2 * MathConsts.DEGREES_TO_RADIANS;

  private final Matrix44 xTransform = Matrices.create3dRotateMatrix(Vector3d.ZERO, Vector3d.X_AXIS, angularVelocity);
  private final Matrix44 yTransform = Matrices.create3dRotateMatrix(Vector3d.ZERO, Vector3d.Y_AXIS, angularVelocity);
  private final Matrix44 zTransform = Matrices.create3dRotateMatrix(Vector3d.ZERO, Vector3d.Z_AXIS, angularVelocity);
   
  private ConstantLightSource3d lightSource; 
  
  public SixDegreesOfFreedom() {
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
    
    xyPlane.rotate(Vector3d.X_AXIS, 90 * MathConsts.DEGREES_TO_RADIANS);
    zyPlane.rotate(Vector3d.Z_AXIS, 90 * MathConsts.DEGREES_TO_RADIANS);
    
    xyPlane.setColorToAllFaces(new FeynColor(255, 255, 0, 50));  
    xzPlane.setColorToAllFaces(new FeynColor(255, 0, 255, 50));  
    zyPlane.setColorToAllFaces(new FeynColor(0, 255, 255, 50)); 

    x.setColor(FeynColor.red);
    y.setColor(FeynColor.green);
    z.setColor(FeynColor.blue);
    
    obj.setColorToAllFaces(FeynColor.white);
    for (Model3dFace face : obj.getFaces()) {
      face.getRenderOptions().enable(cullIfBackface);
      face.getRenderOptions().enable(gouraudShaded);
    }
    
    up.setColor(FeynColor.green);
    sd.setColor(FeynColor.red);
    fw.setColor(FeynColor.blue);

    FeynRuntime.getRepository().add(obj);
    FeynRuntime.getRepository().add(xyPlane);
    FeynRuntime.getRepository().add(xzPlane);
    FeynRuntime.getRepository().add(zyPlane);
    FeynRuntime.getRepository().add(x);
    FeynRuntime.getRepository().add(y);
    FeynRuntime.getRepository().add(z);
    FeynRuntime.getRepository().add(up);
    FeynRuntime.getRepository().add(sd);
    FeynRuntime.getRepository().add(fw);

    camera.translate(0, 0, 2.5);
    
    lightSource = new ConstantLightSource3d(1);  
    FeynRuntime.addDiffuseLightSource(lightSource); 
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.2));
    
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
     
    if (keyHasBeenPressed(KeyEvent.VK_X)) {
      if (keyHasBeenPressed(KeyEvent.VK_SHIFT)) {
        transform.mulLocal(Matrices.create3dRotateMatrix(position, obj.getSideVector(), angularVelocity)); 
      } else {
        transform.mulLocal(xTransform); 
      }
    }

    if (keyHasBeenPressed(KeyEvent.VK_Y)) {
      if (keyHasBeenPressed(KeyEvent.VK_SHIFT)) {
        transform.mulLocal(Matrices.create3dRotateMatrix(position, obj.getUpVector(), angularVelocity)); 
      } else {
        transform.mulLocal(yTransform); 
      }
    }

    if (keyHasBeenPressed(KeyEvent.VK_Z)) {
      if (keyHasBeenPressed(KeyEvent.VK_SHIFT)) {
        transform.mulLocal(Matrices.create3dRotateMatrix(position, obj.getForwardVector(), angularVelocity)); 
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
    var frame = new FeynFrame(800, 800, "Rotation Demo", true, false);
    var demo = new SixDegreesOfFreedom();
    frame.add("Center", demo);
    frame.setVisible(true);
    demo.init();
  }
}
