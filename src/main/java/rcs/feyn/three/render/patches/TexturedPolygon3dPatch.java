package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.renderers.TexturedPolygon3dRenderer;
import rcs.feyn.three.view.ViewUtils;

import java.util.Arrays;
import java.util.Optional;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector2d;
import rcs.feyn.math.Vector3d;

public class TexturedPolygon3dPatch extends Polygon3dPatch {
  
  protected TextureRaster textureData;
  protected int alpha;
  protected double zoom;

  public TexturedPolygon3dPatch(
      Vector3d[] vertices, 
      TextureRaster data, 
      int alpha, 
      double zoom, 
      RenderOptions3d options) {
    super(vertices, FeynColor.white, options);
    this.textureData = data;
    this.alpha = alpha;
    this.zoom = zoom;
  } 
  
  @Override
  public final boolean isTransparent() {
    return alpha < 255;
  }

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    if (!options.isEnabled(Option.textured) 
        || options.isEnabled(Option.meshOnly)) {
      super.render(graphics, view, projection, viewPort);
      return;
    }
    
    Vector3d center = GeoUtils3d.getCenter(vertices);
    Vector3d surfaceNormal = GeoUtils3d.getNormal(vertices);
    boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, surfaceNormal);
    
    if (isBackfaceToCamera && shouldCullIfBackface()) {
      return;
    }
    
    Vector3d[] viewVertices = Pipeline3d.toViewSpaceCoordinates(vertices, view);
    Vector3d[] clippedViewVertices = Pipeline3d.clipViewSpaceCoordinates(viewVertices);
    
    int numClippedViewVertices = clippedViewVertices.length;
    
    if (numClippedViewVertices < 3) {
      return;
    }
    
    Vector3d[][] triangulatedClippedViewVertices = GeoUtils3d.triangulate(clippedViewVertices);
    
    Vector2d[] textureCoordinates = getTextureCoordinates(clippedViewVertices);
    Vector2d[][] triangulatedTextureCoordinates = GeoUtils3d.triangulate(textureCoordinates);
    
    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    Vector3d normalForLighting = shouldReverseNormalForLighting ? surfaceNormal.mul(-1) : surfaceNormal;

    double intensity = 1.0;
    if (options.isEnabled(Option.flatShaded)) {
      intensity = LightingUtils.computeLightingIntensity(center, normalForLighting);
    }

    boolean shouldApplyLightingColor = options.isEnabled(Option.applyLightingColor) && LightingUtils.hasColoredLightsources();
    
    int[] colors = null;
    if (shouldApplyLightingColor) {
      colors = new int[numClippedViewVertices];
      int initialColor = ColorUtils.mulRGB(color.getRGBA(), intensity);
      for (int i = 0; i < numClippedViewVertices; i++) {
        colors[i] = LightingUtils.applyLightsourceColorTo(
            clippedViewVertices[i], 
            surfaceNormal.affineTransformAsVector(view), 
            view, 
            initialColor);
      }
    }
    
    int[][] triangulatedColors = shouldApplyLightingColor ? GeoUtils3d.triangulate(colors) : null;

    for (int i = 0; i < triangulatedClippedViewVertices.length; i++) {     
      TexturedPolygon3dRenderer.render(
        graphics,
        Pipeline3d.toDeviceCoordinates(triangulatedClippedViewVertices[i], projection, viewPort),
        intensity,
        Optional.ofNullable(shouldApplyLightingColor ? triangulatedColors[i] : null),
        textureData,
        triangulatedTextureCoordinates[i],
        alpha);
    }
  }

  protected Vector2d[] getTextureCoordinates(Vector3d[] clippedViewVertices) {
    Vector3d center = GeoUtils3d.getCenter(clippedViewVertices);
    Vector3d normal = GeoUtils3d.getNormal(clippedViewVertices);
    
    Vector3d axis = normal.crossProd(Vector3d.Z_AXIS);
    double theta = normal.angleBetween(Vector3d.Z_AXIS);
    
    Vector3d[] rotated = Arrays.stream(clippedViewVertices)
        .map(vertex -> vertex.rotate(center, axis, theta))
        .map(vertex -> vertex.subLocal(center))
        .map(vertex -> vertex.z(0))
        .toArray(Vector3d[]::new);
    
    Vector3d topBar = rotated[1].sub(rotated[0]);   
    double phi = topBar.angleBetween(Vector3d.X_AXIS, Vector3d.NEG_Y_AXIS);    
    
    rotated = Arrays.stream(rotated)
        .map(vertex -> vertex.rotateLocal(Vector3d.ZERO, Vector3d.Z_AXIS, phi))
        .toArray(Vector3d[]::new);

    double minX = 0;
    double minY = 0;
    double maxX = 0;
    double maxY = 0;
    
    for (Vector3d vertex : rotated) {
      minX = Math.min(minX, vertex.x());
      minY = Math.min(minY, vertex.y());
    }
    
    for (Vector3d vertex : rotated) {
      vertex.addLocal(Math.abs(minX), Math.abs(minY), 0);
    }
    
    for (Vector3d vertex : rotated) {
      maxX = Math.max(maxX, vertex.x());
      maxY = Math.max(maxY, vertex.y());
    }
    
    int tdw = textureData.getWidth() - 1;    
    int tdh = textureData.getHeight() - 1;

    double finalMaxX = maxX;
    double finalMaxY = maxY;
    
    return Arrays.stream(rotated)
        .map(vertex -> new Vector2d(vertex.x() / finalMaxX * tdw, vertex.y() / finalMaxY * tdh))
        .toArray(Vector2d[]::new);
        
  }
}
