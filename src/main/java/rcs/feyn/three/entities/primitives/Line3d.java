package rcs.feyn.three.entities.primitives;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.patches.Line3dPatch;
import rcs.feyn.three.render.patches.Patch3d;

public class Line3d extends Primitive3d {
  
  protected Vector3d a = new Vector3d();
  protected Vector3d b = new Vector3d();

  public Line3d(Vector3d a, Vector3d b) {
    this.a.set(a).addLocal(position);
    this.b.set(b).addLocal(position);
  }

  public Vector3d getA() {
    return new Vector3d(a);
  }

  public Vector3d getB() {
    return new Vector3d(b);
  }

  public Vector3d getMidPoint() {
    return a.midPoint(b);
  }

  public double getLength() {
    return a.distance(b);
  }

  public double getLengthSquared() {
    return a.distanceSquared(b);
  }

  public Vector3d getUnitVector() {
    return b.sub(a).normalizeLocal();
  }

  @Override
  public Vector3d getCenterOfMass() {
    return getMidPoint();
  }

  @Override
  public synchronized void translate(Vector3d v3d) {
    super.translate(v3d);
 
    a.addLocal(v3d);
    b.addLocal(v3d);
  }

  @Override
  public synchronized void transform(Matrix44 m4x4) {
    super.transform(m4x4);
    
    a.affineTransformLocal(m4x4);
    b.affineTransformLocal(m4x4);
  }

  @Override
  public synchronized Patch3d[] getRenderablePatches() {
    if (isHidden()) {
      return new Patch3d[] {};
    }
    
    return new Patch3d[] { 
        new Line3dPatch(new Vector3d(a), new Vector3d(b), color, options) 
    };
  }
}