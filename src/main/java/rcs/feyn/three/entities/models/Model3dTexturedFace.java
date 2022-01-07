package rcs.feyn.three.entities.models;

import java.util.LinkedList;

import rcs.feyn.color.AlphaEnabled;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.GouraudTexturedPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.render.patches.TexturedPolygon3dPatch;
import rcs.feyn.three.view.ViewUtils;

public class Model3dTexturedFace extends Model3dFace implements AlphaEnabled {
  
  private TextureRaster textureData;
  private int alpha;
  private double zoom;
  
  private TextureRaster lastTextureData;
  private int lastAlpha;
  
  public Model3dTexturedFace(int[] indices, TextureRaster textureData) {
    this(indices, textureData, 255, 1);
  }

  public Model3dTexturedFace(int[] indices, TextureRaster textureData, int alpha, double zoom) {
    super(indices, null);
    setTextureData(textureData);
    setAlpha(alpha);
    this.color = new FeynColor(textureData.getAverageColor());
    this.zoom = zoom;
  }
  
  public TextureRaster getTextureData() {
    return textureData;
  }
  
  public synchronized void setTextureData(TextureRaster textureData) {
    this.lastTextureData = textureData;
    this.textureData = textureData;
  }
  
  @Override
  public int getAlpha() {
    return alpha;
  }

  @Override
  public synchronized void setAlpha(int alpha) {
    this.lastAlpha = this.alpha;
    this.alpha = alpha;
  }

  @Override
  public Polygon3dPatch[] makePatch(Model3dVertices vertices) {
    if (matchesLastPatch(vertices)) {
      return lastPatches;
    }
    
    synchronized(this) {
      lastVertices = getVertices(vertices.getVertices());
      lastCameraPosition = FeynRuntime.getView().getCamera().getPosition();
    }
    
    LinkedList<Polygon3dPatch> newPatches = new LinkedList<>();
    
    TriangulatedVerticesAndNormals verticesAndNormals = getTriangulatedVerticesAndNormals(vertices);
    Vector3d[][] polygons = verticesAndNormals.getVertices();
    Vector3d[][] normals = verticesAndNormals.getNormals();
    
    for (int i = 0; i < polygons.length; i++) {
      Vector3d[] polygon = polygons[i];

      Vector3d center = GeoUtils3d.getCenter(polygon);
      Vector3d normal = GeoUtils3d.getNormal(polygon);
      boolean isBackfaceToCamera = ViewUtils.isBackFace(FeynRuntime.getView().getCamera().getPosition(), center, normal);
      
      if (isBackfaceToCamera && shouldCullIfBackface()) {
        continue;
      }
      
      Polygon3dPatch newPatch;
      if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded) && options.isEnabled(Option.textured)) {
        newPatch = new GouraudTexturedPolygon3dPatch(
            polygon, 
            normals[i], 
            textureData,
            alpha,
            zoom,
            options);
      } else if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded)) { 
        newPatch = new GouraudPolygon3dPatch(
            polygon, 
            normals[i], 
            color,
            options);
        
      } else if (options.isEnabled(Option.textured)) {
        newPatch = new TexturedPolygon3dPatch(
            polygon, 
            textureData,
            alpha,
            zoom,
            options);
      } else {
        newPatch = new Polygon3dPatch(
            polygon, 
            color,
            options);
      }
      newPatches.add(newPatch);
    }
    
    return lastPatches = newPatches.toArray(Polygon3dPatch[]::new);
  }
  
  @Override
  protected synchronized boolean matchesLastPatch(Model3dVertices vertices) {
    return textureData == lastTextureData 
        && alpha == lastAlpha 
        && super.matchesLastPatch(vertices);
  }
}
