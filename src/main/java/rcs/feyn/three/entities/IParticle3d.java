package rcs.feyn.three.entities;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.Movable3d;
import rcs.feyn.three.geo.Transformable3d;

public interface IParticle3d extends Movable3d, Transformable3d {
  
  public double getMass();
  public void setMass(double m);
 
  public Vector3d getPosition();
  public void setPosition(Vector3d pos);
  public void setPosition(double x, double y, double z);
  
  public Vector3d getVelocity();
  public void setVelocity(Vector3d vel);
  public void setVelocity(double x, double y, double z);
  
  public ReferenceFrame3d getReferenceFrame();
  public void setReferenceFrame(ReferenceFrame3d rf);
}
