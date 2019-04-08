package rcs.feyn.three.render.primitives;

import rcs.feyn.three.Particle3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.color.Colorable;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.utils.ArrayUtils;

public class Polygon3d extends Primitive3d implements Colorable {

  protected RenderOptions3d options = RenderOptions3d.defaults();
  
  protected Vector3d[] vertices;

  public Polygon3d(Vector3d[] vertices) {
    super();
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
    double degree;
    Vector3d axis;
    
    Vector3d thatUp = that.getUpVector();
    Vector3d thatSd = that.getSideVector();
    
    degree = this.up.angleBetween(thatUp) * MathConsts.RADIANS_TO_DEGREES;
    if (!MathUtils.epsilonZero(degree)) {
      axis = this.up.crossProd(thatUp);
      rotate(axis, degree);
    }
    
    degree = this.sd.angleBetween(thatSd) * MathConsts.RADIANS_TO_DEGREES;
    if (MathUtils.epsilonEquals(degree, 180)) {
      axis = this.up;
    } else {
      axis = this.sd.crossProd(thatSd);
    }
    
    rotate(axis, degree);
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

  public RenderOptions3d getRenderingOptions() {
    return options;
  }

  @Override
  public Patch3d[] getRenderablePatches() {
    return new Patch3d[] {
      new Polygon3dPatch(
          copyVertices(), 
          color,
          options)
    };
  }  

  public static Polygon3d regularPolygon(int radius, int sides) {
    Vector3d[] vertices = new Vector3d[sides];

    double deg = MathConsts.TWO_PI / (double) sides;
    
    for (int i = 0; i < sides; i++) {
      vertices[i] = Vector3d.fromSpherical(radius, i*deg, 0);
    } 
    
    return new Polygon3d(vertices);
  }
}
