package rcs.feyn.two;

import rcs.feyn.math.linalg.Vector2d;
import rcs.feyn.two.geo.Movable2d;
import rcs.feyn.two.geo.Transformable2d;

public interface IParticle2d extends Movable2d, Transformable2d {
  
  public double getMass();
  public void setMass(double m);
 
  public Vector2d getPosition();
  public void setPosition(Vector2d pos);
  
  public Vector2d getVelocity();
  public void setVelocity(Vector2d vel);
  
  public ReferenceFrame2d getReferenceFrame();
  public void setReferenceFrame(ReferenceFrame2d rf);
}
