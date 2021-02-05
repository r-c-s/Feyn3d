package rcs.feyn.three.entities.models;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.geo.Movable3d;
import rcs.feyn.three.geo.Transformable3d;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.physics.PhysicsUtils;

public class Model3dVertices implements Movable3d, Transformable3d {
  
  protected Vector3d[] vertices;
  protected double[] masses;
  
  public Model3dVertices(Vector3d[] vertices) {
    this(vertices, null);
  }

  public Model3dVertices(Vector3d[] vertices, double[] masses) {
    this.vertices = vertices;
    this.masses = masses;
  }

  public double size() {
    return vertices.length;
  }
  
  protected Vector3d[] getVertices() {
    return vertices;
  }
  
  public synchronized void setVertices(Vector3d[] vertices, int[] indices) {
    for (int i = 0; i < indices.length; i++) {
      this.vertices[indices[i]] = new Vector3d(vertices[i]);
    }
  }

  public double[] getMasses() {
    return masses;
  }

  @Override
  public synchronized void translate(Vector3d delta) {
    for (Vector3d vertex : vertices) {
      vertex.addLocal(delta);
    }
  }

  @Override
  public synchronized void transform(Matrix44 transform) {
    for (Vector3d vertex : vertices) {
      vertex.affineTransformLocal(transform);
    }
  }

  public synchronized Vector3d centerOfMass() {
    if (masses == null) {
      return center();
    } else { 
      return PhysicsUtils.centerOfMass(vertices, masses);
    }
  }

  public synchronized Vector3d center() {
    return GeoUtils3d.getCenter(vertices);
  }
}
