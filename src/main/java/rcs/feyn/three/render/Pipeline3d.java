package rcs.feyn.three.render;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.math.Vector4d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.view.ViewFrustum3d;

public final class Pipeline3d {  
  
  private Pipeline3d() {
    throw new AssertionError();
  }
  
  public static Vector3d[] toViewSpaceCoordinates(Vector3d[] vertices, Matrix44 view) {
    return worldToViewSpaceCoordinates(vertices, view);
  }
  
  public static Vector3d[][] toViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals, Matrix44 view) {
    return worldToViewSpaceCoordinates(vertices, normals, view);
  }

  public static Vector3d[] clipViewSpaceCoordinates(Vector3d[] vertices) {
    ViewFrustum3d viewFrustum = FeynApp3d.getView().getViewFrustum();
    if (viewFrustum.triviallyNotVisible(vertices)) {
      return new Vector3d[]{};
    }
    return viewFrustum.clipToNearPlane(vertices);
  }

  public static Vector3d[][] clipViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals) {
    ViewFrustum3d viewFrustum = FeynApp3d.getView().getViewFrustum();
    if (viewFrustum.triviallyNotVisible(vertices)) {
      return new Vector3d[][]{ new Vector3d[]{}, new Vector3d[]{} };
    }
    return viewFrustum.clipToNearPlane(vertices, normals);
  }
  
  public static Vector3d[] toDeviceCoordinates(Vector3d[] vertices, Matrix44 projection, Matrix44 viewPort) {
    Vector3d[] ndcVertices = viewToNormalizedDeviceCoordinates(vertices, projection);
    return ndcToDeviceCoordinates(ndcVertices, viewPort);
  }

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

  private static Vector3d[] ndcToDeviceCoordinates(Vector3d[] vertices, Matrix44 viewPort) {
    int size = vertices.length;
    
    Vector3d[] vpc = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      vpc[i] = vertices[i].affineTransform(viewPort);
    }
    return vpc;
  }
}