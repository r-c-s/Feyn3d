package rcs.feyn.three.entities;

import java.util.function.Function;

import rcs.feyn.math.Matrices;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Rotation3d {
  
  private Function<Particle3d, Vector3d> center;
  private Function<Particle3d, Vector3d> axis;
  private Function<Particle3d, Double> radians;

  public static Rotation3d spin(Vector3d axis, double radians) {
    return new Rotation3d(
        Particle3d::getPosition,
        particle -> axis,
        particle -> radians);
  }
  
  public Rotation3d(
      Function<Particle3d, Vector3d> center,
      Function<Particle3d, Vector3d> axis,
      Function<Particle3d, Double> radians) {
    this.center = center;
    this.axis = axis;
    this.radians = radians;
  }

  public Matrix44 getMatrix(Particle3d particle) {
    return Matrices.create3dRotateMatrix(
        center.apply(particle), 
        axis.apply(particle), 
        radians.apply(particle));
  }
}
