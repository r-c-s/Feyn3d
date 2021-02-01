package rcs.feyn.three.kernel;

import rcs.feyn.three.optics.DiffuseLightSource3d;
import rcs.feyn.color.ColorUtils;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.math.linalg.Vector4d;

public final class Pipeline3d {  
  
  private Pipeline3d() {
    throw new AssertionError();
  }

  public static Vector3d[] ndcToDeviceCoordinates(Vector3d[] vertices, Matrix44 viewPort) {
    int size = vertices.length;
    
    Vector3d[] vpc = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      vpc[i] = vertices[i].affineTransform(viewPort);
    }
    return vpc;
  }

  public static Vector3d[] viewToNormalizedDeviceCoordinates(Vector3d[] vertices, Matrix44 projection) {
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

  public static Vector3d[] worldToViewSpaceCoordinates(Vector3d[] vertices, Matrix44 view) {
    int size = vertices.length;
    
    Vector3d[] transformed = new Vector3d[size];
    for (int i = 0; i < size; i++) {
      transformed[i] = vertices[i].affineTransform(view);
    }

    return transformed;
  }

  public static Vector3d[][] worldToViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals, Matrix44 view) {
    int size = vertices.length;
    
    Vector3d[] transformedVertices = new Vector3d[size];
    Vector3d[] transformedNormals  = new Vector3d[size];
    for (int i = 0; i < vertices.length; i++) {
      transformedVertices[i] = vertices[i].affineTransform(view);
      transformedNormals [i] = normals [i].affineTransformAsVector(view);
    }
    
    return new Vector3d[][]{ transformedVertices, transformedNormals };
  }

  public static Vector3d[] clipViewSpaceCoordinates(Vector3d[] vertices) {
    if (FeynApp3d.getViewFrustum().triviallyNotVisible(vertices)) {
      return new Vector3d[]{};
    }
    return FeynApp3d.getViewFrustum().clipToNearPlane(vertices);
  }

  public static Vector3d[][] clipViewSpaceCoordinates(Vector3d[] vertices, Vector3d[] normals) {
    if (FeynApp3d.getViewFrustum().triviallyNotVisible(vertices)) {
      return new Vector3d[][]{ new Vector3d[]{}, new Vector3d[]{} };
    }
    return FeynApp3d.getViewFrustum().clipToNearPlane(vertices, normals);
  }
  
  public static final double computeLightningIntensity(
      Vector3d point, Vector3d normal, boolean bothSides) {
    return computeLightningIntensity(point, normal, null, bothSides);
  } 

  public static final double computeLightningIntensity(
      Vector3d point, Vector3d normal, Matrix44 view, boolean bothSides) {
    double ambient = FeynApp3d.getAmbientLight().getIntensity();
    double intensity = ambient;
    
    DiffuseLightSource3d lightSource = FeynApp3d.getDiffuseLightSource();
    
    if (lightSource != null) {
      double diffuse;
      
      if (view != null) {
        diffuse = lightSource.getIntensityAt(point, normal, view);
      } else {
        diffuse = lightSource.getIntensityAt(point, normal);
      }
      
      if (bothSides) {
        diffuse = Math.abs(diffuse);
      } else {
        diffuse = Math.max(0, diffuse);
      }
      intensity += (1-ambient) * diffuse;
    }
    
    return intensity;
  } 
  
  public static final int applyLightning(int color) {
    return applyLightiningColor(
        color,
        FeynApp3d.getDiffuseLightSource().getColor().getRGBA());
  }
  
  private static final int applyLightiningColor(int objectColor, int lightColor) {
    int objectAlpha = ColorUtils.getAlphaFromRGBA(objectColor);
    int lightAlpha = ColorUtils.getAlphaFromRGBA(lightColor);
    int blendAlpha = Math.abs(objectAlpha - lightAlpha);
    int blended = ColorUtils.alphaBlend(ColorUtils.setAlphaToRGBA(objectColor, blendAlpha), lightColor);
    return ColorUtils.setAlphaToRGBA(blended, objectAlpha);
  }
}