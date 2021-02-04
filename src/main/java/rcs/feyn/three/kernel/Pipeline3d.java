package rcs.feyn.three.kernel;

import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.math.linalg.Vector4d;

public final class Pipeline3d {  
  
  private Pipeline3d() {
    throw new AssertionError();
  }
  
  public static Vector3d[] getClippedViewSpaceCoordinates(Vector3d[] vertices, Matrix44 view) {
    Vector3d[] viewVertices = Pipeline3d
        .worldToViewSpaceCoordinates(vertices, view);
    
    return Pipeline3d
        .clipViewSpaceCoordinates(viewVertices);
  }
  
  public static Vector3d[][] getClippedViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals, Matrix44 view) {
    Vector3d[][] viewVerticesAndNormals = Pipeline3d
        .worldToViewSpaceCoordinates(vertices, normals, view);
    
    return Pipeline3d
        .clipViewSpaceCoordinates(viewVerticesAndNormals[0], viewVerticesAndNormals[1]);
  }
  
  public static Vector3d[] getDeviceCoordinates(Vector3d[] vertices, Matrix44 projection, Matrix44 viewPort) {
    Vector3d[] ndcVertices = Pipeline3d
        .viewToNormalizedDeviceCoordinates(vertices, projection);
    
    return Pipeline3d
        .ndcToDeviceCoordinates(ndcVertices, viewPort);
  }

//private static Vector3d[] viewSpaceToWorldCoordinates(Vector3d[] vertices, Matrix44 view) {
//  int size = vertices.length;
//  
//  Matrix44 inverse = view.inverse();
//  
//  Vector3d[] transformed = new Vector3d[size];
//  for (int i = 0; i < size; i++) {
//    transformed[i] = vertices[i].affineTransform(inverse);
//  }
//
//  return transformed;
//}

  private static Vector3d[] worldToViewSpaceCoordinates(Vector3d[] vertices, Matrix44 view) {
    int size = vertices.length;
    
    Vector3d[] transformed = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      transformed[i] = vertices[i].affineTransform(view);
    }

    return transformed;
  }

  private static Vector3d[][] worldToViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals, Matrix44 view) {
    int size = vertices.length;
    
    Vector3d[] transformedVertices = new Vector3d[size];
    Vector3d[] transformedNormals  = new Vector3d[size];
    for (int i = 0; i < vertices.length; i++) {
      transformedVertices[i] = vertices[i].affineTransform(view);
      transformedNormals [i] = normals [i].affineTransformAsVector(view);
    }
    
    return new Vector3d[][]{ transformedVertices, transformedNormals };
  }

  private static Vector3d[] clipViewSpaceCoordinates(Vector3d[] vertices) {
    if (FeynApp3d.getViewFrustum().triviallyNotVisible(vertices)) {
      return new Vector3d[]{};
    }
    return FeynApp3d.getViewFrustum().clipToNearPlane(vertices);
  }

  private static Vector3d[][] clipViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals) {
    if (FeynApp3d.getViewFrustum().triviallyNotVisible(vertices)) {
      return new Vector3d[][]{ new Vector3d[]{}, new Vector3d[]{} };
    }
    return FeynApp3d.getViewFrustum().clipToNearPlane(vertices, normals);
  }

  private static Vector3d[] ndcToDeviceCoordinates(Vector3d[] vertices, Matrix44 viewPort) {
    int size = vertices.length;
    
    Vector3d[] vpc = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      vpc[i] = vertices[i].affineTransform(viewPort);
    }
    return vpc;
  }

  private static Vector3d[] viewToNormalizedDeviceCoordinates(Vector3d[] vertices, Matrix44 projection) {
    int size = vertices.length;
    
    Vector3d[] ndc = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      Vector4d v4 = projection.mul(vertices[i].toVector4d().w(1));
      Vector3d v3;
      
      if (v4.z() > 0) {
        v3 = v4.homogeneousNormalize().toVector3d();
      } else {
        v3 = v4.pointWiseDivLocal(-v4.w(), -v4.w(), -v4.w(), v4.w()).toVector3d();
      }
      
      ndc[i] = v3;
    }
    return ndc;
  }
}