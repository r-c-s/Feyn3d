package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.GouraudTexturedPolygon3dRenderer;
import rcs.feyn.three.render.renderers.RenderOptions3d;

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
    if (!options.isEnabled(RenderOptions3d.Option.gouraudShaded) 
        || options.isEnabled(RenderOptions3d.Option.meshOnly)) {
      super.render(graphics, view, projection, viewPort);
      return;
    }
    
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    if (cullIfBackface(center, normal)) {
      return;
    }
    
    Vector3d[][] viewSpaceCoordinates = Pipeline3d
        .toViewSpaceCoordinates(vertices, normals, view);
    Vector3d[] viewVertices = viewSpaceCoordinates[0];
    Vector3d[] viewNormals = viewSpaceCoordinates[1];
    
    if (viewVertices.length < 3) {
      return;
    }

    // can't clip textured polygons
    
    Vector3d[] deviceCoordinates = Pipeline3d
        .getDeviceCoordinates(viewVertices, projection, viewPort);
    
    int numVerticesAndNormals = viewVertices.length;
    
    double[] intensities = new double[numVerticesAndNormals];
    for (int i = 0; i < numVerticesAndNormals; i++) {
      intensities[i] = LightingUtils.computeLightingIntensity(
              viewVertices[i], 
              viewNormals[i],
              view,
              options.isEnabled(RenderOptions3d.Option.bothSidesShaded));
    }

    int[] colors = null;
    if (options.isEnabled(RenderOptions3d.Option.applyLightingColor) 
        && LightingUtils.hasColoredLightsources()) {
      colors = new int[numVerticesAndNormals];
      for (int i = 0; i < numVerticesAndNormals; i++) {
        colors[i] = LightingUtils.applyLightsourceColorTo(
            vertices[i], 
            normals[i],
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
