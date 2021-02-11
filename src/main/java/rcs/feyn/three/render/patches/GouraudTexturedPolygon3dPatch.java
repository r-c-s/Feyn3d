package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.three.render.renderers.TexturedPolygon3dRenderer;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class GouraudTexturedPolygon3dPatch extends Polygon3dPatch {

  protected Vector3d[] normals;
  private Raster textureData;
  private int alpha;

  public GouraudTexturedPolygon3dPatch(
      Vector3d[] vertices, 
      Vector3d[] normals,
      Raster data, 
      int alpha, 
      RenderOptions3d options) {
    
    super(vertices, FeynColor.white, options);
    this.normals = normals;
    this.textureData = data;
    this.alpha = alpha;
  } 

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    
    if (!options.isEnabled(RenderOptions3d.Option.meshOnly)
      && options.isEnabled(RenderOptions3d.Option.cullIfBackface)
      && ViewUtils.isBackFace(FeynApp3d.getCamera().getPosition(), center, normal)) {
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
    for (int i = 0; i < numVerticesAndNormals; i++) {
      intensities[i] = LightingUtils.computeLightingIntensity(
              clippedViewVertices[i], 
              clippedViewNormals[i],
              view,
              options.isEnabled(RenderOptions3d.Option.bothSidesShaded));
    }
    
    TexturedPolygon3dRenderer.render(
      graphics,
      deviceCoordinates, 
      intensities,
      textureData,
      alpha);
  }
}
