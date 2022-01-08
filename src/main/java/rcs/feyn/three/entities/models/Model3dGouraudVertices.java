package rcs.feyn.three.entities.models;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Model3dGouraudVertices extends Model3dVertices {
  
  protected Vector3d[] normals;

  public Model3dGouraudVertices(Vector3d[] vertices, Vector3d[] normals) {
    this(vertices, normals, null);
  }

  public Model3dGouraudVertices(Vector3d[] vertices, Vector3d[] normals, double[] masses) {
    super(vertices, masses);
    this.normals = normals;
  }

  final Vector3d[] getNormals() {
    return normals;
  }

  @Override
  public void transform(Matrix44 transform) {
    super.transform(transform);
    
    Matrix44 rotation = transform.extractRotation();

    synchronized (this) {
      for (Vector3d normal : normals) {
        normal.affineTransformLocal(rotation);
      }
    }
  }
  
  @Override
  public synchronized Model3dVertices clone() {
    return new Model3dGouraudVertices(copyVertices(vertices), copyVertices(normals), copyMasses());
  }
}
