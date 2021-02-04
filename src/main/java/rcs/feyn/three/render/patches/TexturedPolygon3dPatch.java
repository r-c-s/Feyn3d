package rcs.feyn.three.render.patches;

import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.kernel.FeynApp3d;
import rcs.feyn.three.optics.LightingUtils;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.three.render.renderers.TexturedPolygon3dRenderer;
import rcs.feyn.three.view.ViewUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class TexturedPolygon3dPatch extends Polygon3dPatch {
  
  private Raster textureData;
  protected Vector3d[] normals;

  public TexturedPolygon3dPatch(Vector3d[] vertices, Raster data, RenderOptions3d options) {
    super(vertices, FeynColor.white, options);
    this.textureData = data;
  } 

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    Vector3d center = getCenter();
    Vector3d normal = GeoUtils3d.getNormal(vertices);
    
    if (!options.isEnabled(RenderOptions3d.Option.meshOnly)
      && options.isEnabled(RenderOptions3d.Option.cullIfBackface) 
      && !color.isTransparent()
      && ViewUtils.isBackFace(FeynApp3d.getCamera().getPosition(), center, normal)) {
      return;
    }

    Vector3d[] vpcVertices = getViewPointCoordinateVertices(view, projection, viewPort);

    double intensity = 1.0;
    if (options.isEnabled(RenderOptions3d.Option.flatShaded)) {
      intensity = LightingUtils.computeLightningIntensity(
            center, 
            normal, 
            options.isEnabled(RenderOptions3d.Option.bothSidesShaded) || options.isEnabled(RenderOptions3d.Option.meshOnly));
    }
    
    TexturedPolygon3dRenderer.render(
      graphics,
      vpcVertices, 
      intensity,
      textureData);
  }
}
