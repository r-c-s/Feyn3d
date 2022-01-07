package demo3d;

import java.io.Serial;
import java.util.Set;

import rcs.feyn.color.FeynColor;
import rcs.feyn.gui.FeynFrame;
import rcs.feyn.three.entities.Rotation3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.ConstantLightSource3d;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Vector3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class Plato extends Demo3d { 

  @Serial
  private static final long serialVersionUID = 1L; 
  
  private TextureRaster texture = Model3dUtils
      .getImageData(System.getProperty("user.dir") + "/textures/marble.jpeg");
  
  private Model3d tetrahedron = Model3dFactory.tetrahedron(1)
      .setTextureData(texture)
      .build();
  
  private Model3d octahedron = Model3dFactory.octahedron(1)
      .setTextureData(texture)
      .build();
  
  private Model3d hexahedron = Model3dFactory.hexahedron(1)
      .setTextureData(texture)
      .build();
  
  private Model3d icosahedron  = Model3dFactory.icosahedron(1)
      .setTextureData(texture)
      .build();
  
  private Model3d dodecahedron = Model3dFactory.dodecahedron(1)
      .setTextureData(texture)
      .build();
  
  private Model3d[] solids = new Model3d[] {
    tetrahedron,
    octahedron,  
    hexahedron,  
    icosahedron,
    dodecahedron
  }; 
    
  public Plato() { }
  
  @Override
  protected void initialize() {
    super.initialize();
    
    setBackgroundColor(FeynColor.bisque3);
    
    wzc.setAmount(0.1);

    camera.translate(0, 2, 6);
    camera.rotate(Vector3d.X_AXIS, -25 * MathConsts.DEGREES_TO_RADIANS);
    
    FeynRuntime.addDiffuseLightSource(new ConstantLightSource3d(0.5));
    FeynRuntime.setAmbientLight(new AmbientLightSource3d(0.5));
    
    double deg = MathConsts.TWO_PI / (double) solids.length;
    
    for (int i = 0; i < solids.length; i++) {
      Model3d solid = solids[i];
      FeynRuntime.getRepository().add(solid); 
      solid.translate(Vector3d.fromSpherical(3, i*deg, 0));
      solid.setRotation(Rotation3d.spin(Vector3d.Y_AXIS, 1.5 * MathConsts.DEGREES_TO_RADIANS));
      Model3dUtils.setOptions(
          solid, 
          Set.of(flatShaded), 
          Set.of(gouraudShaded));
    }
  } 

  @Override
  public void runningLoop() {
    controlCamera();  

    FeynRuntime.getDiffuseLightSources()[0].setPosition(camera.getPosition()); 
    
    for (Model3d solid : solids) {  
      solid.animate();
    }
  }

  @Override
  public void pausedLoop() {
    controlCamera();
  } 

  public static void main(String[] args) {
    var frame = new FeynFrame(800, 600, "Feyn Demo", true, false);
    var demo = new Plato();
    frame.add("Center", demo);
    frame.setVisible(true); 
    frame.setLocationRelativeTo(null);
    demo.init();
  }
}
