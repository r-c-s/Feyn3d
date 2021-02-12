package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.three.render.renderers.GouraudPolygon3dRenderer;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class GouraudPolygon3dPatch extends Polygon3dPatch {
  
  protected Vector3d[] normals;

  public GouraudPolygon3dPatch(Vector3d[] vertices, Vector3d[] normals, FeynColor color, RenderOptions3d options) {
    super(vertices, color, options);
    this.normals = normals;
  }

  @Override
  public final void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
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
        .getClippedViewSpaceCoordinates(vertices, normals, view);
    Vector3d[] clippedViewVertices = viewSpaceCoordinates[0];
    Vector3d[] clippedViewNormals = viewSpaceCoordinates[1];
    
    if (clippedViewVertices.length < 3) {
      return;
    }
    
    Vector3d[] deviceCoordinates = Pipeline3d
        .getDeviceCoordinates(clippedViewVertices, projection, viewPort);
  
    int numVerticesAndNormals = clippedViewVertices.length;
    
    double[] intensities = new double[numVerticesAndNormals];
    for (int i = 0; i < intensities.length; i++) {
      intensities[i] = LightingUtils.computeLightingIntensity(
              clippedViewVertices[i], 
              clippedViewNormals[i],
              view,
              options.isEnabled(RenderOptions3d.Option.bothSidesShaded));
    }

    int[] colors = new int[numVerticesAndNormals];
    for (int i = 0; i < numVerticesAndNormals; i++) {
      colors[i] = ColorUtils.mulRGB(color.getRGBA(), intensities[i]);
      if (options.isEnabled(RenderOptions3d.Option.applyLightingColor)) {
        colors[i] = LightingUtils.applyLightsourceColorTo(
            clippedViewVertices[i], 
            clippedViewNormals[i], 
            view, 
            colors[i]);
      }
    }

    GouraudPolygon3dRenderer.render(
        graphics,
        deviceCoordinates,
        colors);
  }
}
