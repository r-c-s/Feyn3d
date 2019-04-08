package rcs.feyn.three;

import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.geo.Movable3d;
import rcs.feyn.three.geo.Transformable3d;

public interface IParticle3d extends Movable3d, Transformable3d {
  
  public double getMass();
  public void setMass(double m);
 
  public Vector3d getPosition();
  public void setPosition(Vector3d pos);
  
  public Vector3d getVelocity();
  public void setVelocity(Vector3d vel);
  
  public ReferenceFrame3d getReferenceFrame();
  public void setReferenceFrame(ReferenceFrame3d rf);
}
