package rcs.feyn.three.optics;

public class AmbientLightSource3d implements LightSource3d {
  
  private double intensity;
  
  public AmbientLightSource3d(double intensity) {
    this.intensity = intensity;
  }

  @Override
  public double getIntensity() {
    return intensity;
  }
}