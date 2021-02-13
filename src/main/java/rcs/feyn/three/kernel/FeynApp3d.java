package rcs.feyn.three.kernel;

import rcs.feyn.three.optics.AmbientLightSource3d;
import rcs.feyn.three.optics.DiffuseLightSource3d;

import java.util.ArrayList;
import java.util.List;

import rcs.feyn.math.TrigLookUp;

public class FeynApp3d {
  
  static { 
    new TrigLookUp(0.1);
  }
  
  private static final View view = new View();
  
  private static final ObjectRepository3d repository = new ObjectRepository3d();
  
  private static final RenderKernel3d renderKernel = new RenderKernel3d(repository);
  
  private static List<DiffuseLightSource3d> lightSources = new ArrayList<>();
  
  private static AmbientLightSource3d ambientLight = new AmbientLightSource3d(1);

  public static View getView() {
    return view;
  }
  
  public static RenderKernel3d getRenderKernel() {
    return renderKernel;
  }
  
  public static ObjectRepository3d getRepository() {
    return repository;
  }

  public static DiffuseLightSource3d[] getDiffuseLightSources() {
    return lightSources.toArray(DiffuseLightSource3d[]::new);
  }

  public static void addDiffuseLightSource(DiffuseLightSource3d ls) {
    lightSources.add(ls);
  }

  public static AmbientLightSource3d getAmbientLight() {
    return ambientLight;
  }

  public static void setAmbientLight(AmbientLightSource3d light) {
    ambientLight = light;
  }
}
