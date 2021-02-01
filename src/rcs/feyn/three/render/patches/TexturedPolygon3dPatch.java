package rcs.feyn.three.render.patches;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.renderers.RenderOptions3d;
import rcs.feyn.gfx.Raster;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class TexturedPolygon3dPatch extends Polygon3dPatch {
  
  private Raster data;

  public TexturedPolygon3dPatch(Vector3d[] vertices, Raster data, RenderOptions3d options) {
    super(vertices, null, options);
    this.data = data;
  } 

  @Override
  public void render(Graphics3d graphics, Matrix44 view, Matrix44 projection, Matrix44 viewPort) {
    // todo
  }
}
