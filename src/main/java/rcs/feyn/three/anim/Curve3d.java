package rcs.feyn.three.anim;

import java.util.function.BiFunction;

import rcs.feyn.three.entities.Sprite3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.patches.Line3dPatch;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.utils.TriFunction;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Curve3d extends Sprite3d { 
  
  protected Matrix44 transform = new Matrix44();
  
  protected BiFunction<Double, Double, Vector3d> positionFunction;
  protected TriFunction<Double, Double, Double, FeynColor> colorFunction;
  
  protected double dr;
  protected double t, dt;

  public Curve3d(
          BiFunction<Double, Double, Vector3d> positionFunction,
          TriFunction<Double, Double, Double, FeynColor> colorFunction, 
          double dr, 
          double dt) {
    super();
    
    this.positionFunction = positionFunction;
    this.colorFunction = colorFunction;
    this.dr = dr;
    this.dt = dt;
  }

  public synchronized void setPositionFunction(BiFunction<Double, Double, Vector3d> func) {
    this.positionFunction = func;
  }   

  public synchronized void setColorFunction(TriFunction<Double, Double, Double, FeynColor> func) {
    this.colorFunction = func;
  }

  @Override
  public Vector3d getCenterOfMass() {
    return position;
  }

  @Override
  public synchronized void transform(Matrix44 m4x4) {
    super.transform(m4x4);
    transform.mulLocal(m4x4);
  } 

  public synchronized void tick() {
    t+=dt;
  }

  @Override
  public synchronized Patch3d[] getRenderablePatches() {
    Patch3d[] patches = new Line3dPatch[(int) (1 / dr)];
    
    double r = 0;
    for (int i = 0; i < patches.length; r+=dr, i++) {
      Vector3d a = positionFunction.apply(   r, t).addLocal(position).affineTransformLocal(transform);
      Vector3d b = positionFunction.apply(dr+r, t).addLocal(position).affineTransformLocal(transform);
      
      FeynColor color = colorFunction.apply(r, t, 0.0);
      
      patches[i] = new Line3dPatch(a, b, color, RenderOptions3d.defaults());
    }
    
    return patches;
  }
}