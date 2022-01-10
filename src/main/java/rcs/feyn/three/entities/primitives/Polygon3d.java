package rcs.feyn.three.entities.primitives;

import rcs.feyn.three.entities.Particle3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.color.Colorable;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.utils.ArrayUtils;
import rcs.feyn.math.MathConsts;

public class Polygon3d extends Primitive3d implements Colorable {
  
  protected Vector3d[] vertices;

  public Polygon3d(Vector3d[] vertices) {
    GeoUtils3d.validatePolygon3d(vertices);
    this.vertices = vertices;
  }

  public Vector3d[] getVertices() {
    return vertices;
  }

  public synchronized Vector3d[] copyVertices() {
    Vector3d[] vertices = new Vector3d[this.vertices.length];
    for (int i = 0; i < vertices.length; i++) {
      vertices[i] = new Vector3d(this.vertices[i]);
    }
    return vertices;
  }

  @Override
  public Vector3d getCenterOfMass() {
    return GeoUtils3d.getCenter(vertices);
  }

  public Polygon3d reverseOrientation() { 
    ArrayUtils.reverse(vertices);
    return this;
  }

  public void setOrientation(Particle3d that) {
    double radians;
    Vector3d axis;
    
    Vector3d thatUp = that.getUpVector();
    Vector3d thatSd = that.getSideVector();
    
    radians = this.up.angleBetween(thatUp);
    if (!MathUtils.epsilonZero(radians)) {
      axis = this.up.crossProd(thatUp);
      rotate(axis, radians);
    }
    
    radians = this.sd.angleBetween(thatSd);
    if (MathUtils.epsilonEquals(radians, 180)) {
      axis = this.up;
    } else {
      axis = this.sd.crossProd(thatSd);
    }
    
    rotate(axis, radians);
  }

  @Override
  public synchronized void translate(Vector3d v3d) {
    super.translate(v3d);
    synchronized (this) {
      for (Vector3d vertex : vertices) {
        vertex.addLocal(v3d);
      } 
    }
  } 

  @Override
  public synchronized void transform(Matrix44 m4x4) {
    super.transform(m4x4);
    synchronized (this) {
      for (Vector3d vertex : vertices) {
        vertex.affineTransformLocal(m4x4);
      }
    }
  }

  @Override
  public Patch3d[] getRenderablePatches() {
    if (isHidden()) {
      return new Patch3d[] {};
    }
    
    return new Patch3d[] {
      new Polygon3dPatch(
          vertices, 
          color,
          options)
    };
  }  

  public static Polygon3d regularPolygon(double radius, int sides) {
    Vector3d[] vertices = new Vector3d[sides];

    double deg = MathConsts.TWO_PI / (double) sides;
    
    for (int i = 0; i < sides; i++) {
      vertices[i] = Vector3d.fromSpherical(radius, i*deg, 0);
    } 
    
    return new Polygon3d(vertices);
  }
}
