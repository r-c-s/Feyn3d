package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.kernel.Pipeline3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.three.render.renderers.Polygon3dRenderer;
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
    
    if (options.isEnabled(RenderOptions3d.Option.cullIfBackface) && 
        ViewUtils.isBackFace(
            FeynApp3d.getCamera().getPosition(), 
            GeoUtils3d.getCenter(vertices), 
            GeoUtils3d.getNormal(vertices))) {
      return;
    }

    Vector3d[][] viewVerticesNormals = Pipeline3d
        .worldToViewSpaceCoordinates(vertices, normals, view);

    Vector3d[] viewVertices = viewVerticesNormals[0];
    Vector3d[] viewNormals = viewVerticesNormals[1];
    
    Vector3d[][] clippedViewVerticesNormals = Pipeline3d
        .clipViewSpaceCoordinates(viewVertices, viewNormals);
    
    Vector3d[] clippedViewVertices = clippedViewVerticesNormals[0];
    Vector3d[] clippedViewNormals = clippedViewVerticesNormals[1];
    
    if (clippedViewVertices.length < 3) {
      return;
    }
    
    Vector3d[] ndcVertices = Pipeline3d
        .viewToNormalizedDeviceCoordinates(clippedViewVertices, projection);
    
    Vector3d[] vpcVertices = Pipeline3d
        .ndcToDeviceCoordinates(ndcVertices, viewPort);

    int colorWithLighting = options.isEnabled(RenderOptions3d.Option.applyLightingColor) 
    		? LightingUtils.applyLightning(color.getRGBA())
    		: color.getRGBA();
    
    if (options.isEnabled(RenderOptions3d.Option.lighted)) {
      double[] intensities = new double[clippedViewVertices.length];
      for (int i = 0; i < intensities.length; i++) {
        intensities[i] = LightingUtils.computeLightningIntensity(
                clippedViewVertices[i], 
                clippedViewNormals[i],
                view,
                options.isEnabled(RenderOptions3d.Option.bothSidesShaded));
      }
      
      Polygon3dRenderer.render(
          graphics,
          vpcVertices, 
          intensities,
          colorWithLighting);
    } else {
      Polygon3dRenderer.render(
          graphics, 
          vpcVertices, 
          1, 
          colorWithLighting);
    }
  }
}
