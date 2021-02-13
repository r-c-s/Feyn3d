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

  protected Vector3d[] getNormals() {
    Vector3d[] copy = new Vector3d[normals.length];
    for (int i = 0; i < normals.length; i++) {
      copy[i] = new Vector3d(this.normals[i]);
    }
    return copy;
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
}
