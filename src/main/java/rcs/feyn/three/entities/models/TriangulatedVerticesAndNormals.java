package rcs.feyn.three.entities.models;

import rcs.feyn.math.Vector3d;

public class TriangulatedVerticesAndNormals {

  private final Vector3d[][] vertices;
  private final Vector3d[][] normals;
  
  public TriangulatedVerticesAndNormals(Vector3d[][] vertices, Vector3d[][] normals) {
    this.vertices = vertices;
    this.normals = normals;
  }

  public Vector3d[][] getVertices() {
    return vertices;
  }

  public Vector3d[][] getNormals() {
    return normals;
  }
}
