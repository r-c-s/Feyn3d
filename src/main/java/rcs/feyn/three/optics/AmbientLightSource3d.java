package rcs.feyn.three.optics;

public class AmbientLightSource3d {
  
  private double intensity;
  
  public AmbientLightSource3d(double intensity) {
    this.intensity = intensity;
  }

  public double getIntensity() {
    return intensity;
  }
}