package rcs.feyn.three.render.models;

import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

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

  public synchronized Vector3d[] getNormals(int[] indices) {
    Vector3d[] normals = new Vector3d[indices.length];
    for (int i = 0; i < indices.length; i++) {
      normals[i] = new Vector3d(this.normals[indices[i]]);
    }
    return normals;
  }

  @Override
  public Polygon3dPatch makePatch(Model3dFace face) {
    return new GouraudPolygon3dPatch(
        getVertices(face.getIndices()), 
        getNormals(face.getIndices()), 
        face.getColor(),
        face.getRenderOptions());
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
