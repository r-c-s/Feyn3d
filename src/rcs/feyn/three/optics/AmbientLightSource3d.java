package rcs.feyn.three.optics;

import rcs.feyn.color.FeynColor;

public class AmbientLightSource3d implements LightSource3d {
  
  private FeynColor color;
  private double intensity;
  
  public AmbientLightSource3d(double intensity) {
    this(FeynColor.white, intensity);
  }

  public AmbientLightSource3d(FeynColor color, double intensity) {
    this.color = color;
    this.intensity = intensity;
  }

  @Override
  public FeynColor getColor() {
    return color;
  }

  @Override
  public void setColor(FeynColor color) {
    this.color = color;
  }

  @Override
  public double getIntensity() {
    return intensity;
  }
}