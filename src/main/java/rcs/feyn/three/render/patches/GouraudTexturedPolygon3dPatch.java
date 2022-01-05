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

import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
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
    
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, normal);
    
    if (isBackfaceToCamera && shouldCullIfBackface(center, normal)) {
      return;
    }
    
    Vector3d[][] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(vertices, normals, view);
    Vector3d[] viewVertices = viewSpaceCoordinates[0];
    Vector3d[] viewNormals = viewSpaceCoordinates[1];

    Vector3d[][] clippedViewSpaceCoordinates = Pipeline3d
        .clipViewSpaceCoordinates(viewVertices, viewNormals);
    Vector3d[] clippedViewVertices = clippedViewSpaceCoordinates[0];
    Vector3d[] clippedViewNormals = clippedViewSpaceCoordinates[1];
    
    if (clippedViewVertices.length < 3) {
      return;
    }
    
    Vector3d[] deviceCoordinates = Pipeline3d
        .toDeviceCoordinates(clippedViewVertices, projection, viewPort);
    
    int numVerticesAndNormals = clippedViewVertices.length;
    
    double[] intensities = new double[numVerticesAndNormals];
    
    boolean shouldReverseNormalForLighting = !options.isEnabled(Option.bothSidesShaded) && isBackfaceToCamera;
    
    for (int i = 0; i < numVerticesAndNormals; i++) {
      intensities[i] = LightingUtils.computeLightingIntensity(
          clippedViewVertices[i], 
          shouldReverseNormalForLighting ? clippedViewNormals[i].mul(-1) : clippedViewNormals[i],
          view);
    }


    boolean applyLightingColor = options.isEnabled(Option.applyLightingColor)
        && LightingUtils.hasColoredLightsources();

    int[] colors = null;
    if (applyLightingColor) {
      colors = new int[numVerticesAndNormals];
      for (int i = 0; i < numVerticesAndNormals; i++) {
        colors[i] = LightingUtils.applyLightsourceColorTo(
            clippedViewVertices[i], 
            shouldReverseNormalForLighting ? normals[i].mul(-1) : normals[i],
            FeynColor.black.getRGBA());
      }
    }

    GouraudTexturedPolygon3dRenderer.render(
      graphics,
      deviceCoordinates, 
      intensities,
      Optional.ofNullable(colors),
      textureData,
      alpha,
      zoom);
  }
}
