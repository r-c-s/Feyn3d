package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;
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
      Raster data, 
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
    
    Vector3d center = GeoUtils3d.getCenter(vertices);
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
    
    if (clippedViewVertices.length < 3) {
      return;
    }

    Vector3d[][] triangulatedClippedViewVertices = GeoUtils3d.triangulate(clippedViewVertices);
    Vector3d[][] triangulatedClippedViewNormals = GeoUtils3d.triangulate(clippedViewNormals);
    
    for (int i = 0; i < triangulatedClippedViewVertices.length; i++) {
      Vector3d[] triangle = triangulatedClippedViewVertices[i];
      Vector3d[] normals = triangulatedClippedViewNormals[i];

      Vector3d[] deviceCoordinates = Pipeline3d.toDeviceCoordinates(triangle, projection, viewPort);
      
      double[] intensities = new double[3];
      
      boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
      
      for (int j = 0; j < 3; j++) {
        intensities[j] = LightingUtils.computeLightingIntensity(
            triangle[j], 
            shouldReverseNormalForLighting ? normals[j].mul(-1) : normals[j],
            view);
      }

      boolean applyLightingColor = options.isEnabled(Option.applyLightingColor) && LightingUtils.hasColoredLightsources();

      int[] colors = null;
      if (applyLightingColor) {
        colors = new int[3];
        for (int k = 0; k < 3; k++) {
          int initialColor = ColorUtils.mulRGB(color.getRGBA(), intensities[k]);
          colors[k] = LightingUtils.applyLightsourceColorTo(
              triangle[k], 
              shouldReverseNormalForLighting ? normals[k].mul(-1) : normals[k],
              view,
              initialColor);
        }
      }
      
      int tdw = textureData.getWidth();    
      int tdh = textureData.getHeight();

      // todo: improve this by using triagle; this distorts the shape of the texture
      Vector2d[] textureCoordinates = new Vector2d[] {
          new Vector2d(0, 0),
          new Vector2d(0, tdh - 1),
          new Vector2d(tdw - 1, tdh - 1)
      };
      
      GouraudTexturedPolygon3dRenderer.render(
        graphics,
        deviceCoordinates, 
        intensities,
        Optional.ofNullable(colors),
        textureData,
        textureCoordinates,
        alpha,
        zoom); 
    }    
  }
}
