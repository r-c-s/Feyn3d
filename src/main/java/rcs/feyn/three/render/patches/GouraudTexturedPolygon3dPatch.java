package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.Pipeline3d;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.renderers.GouraudTexturedPolygon3dRenderer;
import rcs.feyn.three.view.ViewUtils;

import java.util.Optional;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector2d;
import rcs.feyn.math.Vector3d;

public class GouraudTexturedPolygon3dPatch extends TexturedPolygon3dPatch {

  protected Vector3d[] normals;

  public GouraudTexturedPolygon3dPatch(
      Vector3d[] vertices, 
      Vector3d[] normals,
      TextureRaster data, 
      int alpha, 
      double zoom,
      RenderOptions3d options) {
    super(vertices, data, alpha, zoom, options);
    this.normals = normals;
  } 

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    if (!options.isEnabled(Option.gouraudShaded) 
        || !options.isEnabled(Option.textured)
        || options.isEnabled(Option.meshOnly)) {
      super.render(graphics, view, projection, viewPort);
      return;
    }
    
    Vector3d center = getCenter();
    Vector3d surfaceNormal = GeoUtils3d.getNormal(vertices);
    boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, surfaceNormal);
    
    if (isBackfaceToCamera && shouldCullIfBackface()) {
      return;
    }
    
    Vector3d[][] viewSpaceCoordinates = Pipeline3d.toViewSpaceCoordinates(vertices, normals, view);
    Vector3d[] viewVertices = viewSpaceCoordinates[0];
    Vector3d[] viewNormals = viewSpaceCoordinates[1];

    Vector3d[][] clippedViewSpaceCoordinates = Pipeline3d.clipViewSpaceCoordinates(viewVertices, viewNormals);
    Vector3d[] clippedViewVertices = clippedViewSpaceCoordinates[0];
    Vector3d[] clippedViewNormals = clippedViewSpaceCoordinates[1];
    
    int numClippedViewVertices = clippedViewVertices.length;
    if (numClippedViewVertices < 3) {
      return;
    }

    Vector3d[][] triangulatedClippedViewVertices = GeoUtils3d.triangulate(clippedViewVertices);

    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    
    double[] intensities = new double[numClippedViewVertices];      
    for (int j = 0; j < intensities.length; j++) {
      intensities[j] = LightingUtils.computeLightingIntensity(
          clippedViewVertices[j], 
          shouldReverseNormalForLighting ? clippedViewNormals[j].mul(-1) : clippedViewNormals[j],
          view);
    }

    boolean shouldApplyLightingColor = options.isEnabled(Option.applyLightingColor) && LightingUtils.hasColoredLightsources();
    
    int[] colors = null;
    if (shouldApplyLightingColor) {
      colors = new int[numClippedViewVertices];
      for (int i = 0; i < numClippedViewVertices; i++) {
        int initialColor = ColorUtils.mulRGB(color.getRGBA(), intensities[i]);
        colors[i] = LightingUtils.applyLightsourceColorTo(
            clippedViewVertices[i], 
            shouldReverseNormalForLighting? clippedViewNormals[i].mul(-1) : clippedViewNormals[i], 
            view, 
            initialColor);
      }
    }

    double[][] triangulatedIntensities = GeoUtils3d.triangulate(intensities);
    int[][] triangulatedColors = shouldApplyLightingColor ? GeoUtils3d.triangulate(colors) : null;
    
    Vector2d[] textureCoordinates = getTextureCoordinates(clippedViewVertices);
    Vector2d[][] triangulatedTextureCoordinates = GeoUtils3d.triangulate(textureCoordinates);

    for (int i = 0; i < triangulatedClippedViewVertices.length; i++) {      
      GouraudTexturedPolygon3dRenderer.render(
        graphics,
        Pipeline3d.toDeviceCoordinates(triangulatedClippedViewVertices[i], projection, viewPort), 
        triangulatedIntensities[i],
        Optional.ofNullable(shouldApplyLightingColor ? triangulatedColors[i] : null),
        textureData,
        triangulatedTextureCoordinates[i],
        alpha);  
    }    
  }
}
