package rcs.feyn.three.entities.models;

import java.util.LinkedList;

import rcs.feyn.color.AbstractColorable;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.three.kernel.FeynRuntime;
import rcs.feyn.three.render.RenderOptions3d;
import rcs.feyn.three.render.RenderOptions3d.Option;
import rcs.feyn.three.render.patches.GouraudPolygon3dPatch;
import rcs.feyn.three.render.patches.Polygon3dPatch;
import rcs.feyn.three.view.ViewUtils;

public class Model3dFace extends AbstractColorable {
  
  protected RenderOptions3d options = RenderOptions3d.defaults();
  
  protected int[] indices;
  
  protected Vector3d lastCameraPosition;
  protected Vector3d[] lastVertices;
  protected Polygon3dPatch[] lastPatches;

  public Model3dFace(int[] indices, FeynColor color) {
    super(color);
    this.indices = indices;
  }

  public int[] getIndices() {
    return indices.clone();
  }

  public RenderOptions3d getRenderOptions() {
    return options;
  }

  public void setRenderOptions(RenderOptions3d options) {
    this.options = options;
  }

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
      boolean isBackfaceToCamera = ViewUtils.isBackFace(lastCameraPosition, center, normal);
      
      if (isBackfaceToCamera && shouldCullIfBackface()) {
        continue;
      }
      
      Polygon3dPatch newPatch;
      if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded)) {      
        newPatch = new GouraudPolygon3dPatch(
            polygon, 
            normals[i], 
            color,
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
  
  protected synchronized boolean matchesLastPatch(Model3dVertices vertices) {
    // assumes that normals only change if vertices changed
    if (lastCameraPosition == null || !lastCameraPosition.equals(FeynRuntime.getView().getCamera().getPosition())) {
      return false;
    }
    if (lastVertices == null || lastPatches == null) {
      return false;
    }
    Vector3d[] newVertices = vertices.getVertices();
    for (int i = 0; i < indices.length; i++) {
      if (!newVertices[indices[i]].equals(lastVertices[i])) {
        return false;
      }
    }
    return true;
  }

  protected Vector3d[] getVertices(Vector3d[] vertices) {
    Vector3d[] patchVertices = new Vector3d[indices.length];
    for (int i = 0; i < indices.length; i++) {
      patchVertices[i] = vertices[indices[i]];
    }
    return patchVertices;
  }
  
  protected TriangulatedVerticesAndNormals getTriangulatedVerticesAndNormals(Model3dVertices vertices) {
    Vector3d[][] triangulatedVertices = null;
    Vector3d[][] triangulatedNormals = null;
    
    if (options.isEnabled(Option.meshOnly)) {
      triangulatedVertices = new Vector3d[1][];
      triangulatedVertices[0] = lastVertices;
      if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded)) {
        triangulatedNormals = new Vector3d[1][];
        triangulatedNormals[0] = getVertices(((Model3dGouraudVertices) vertices).getNormals());
      }
    } else {
      triangulatedVertices = GeoUtils3d.triangulate(lastVertices);
      if (vertices instanceof Model3dGouraudVertices && options.isEnabled(Option.gouraudShaded)) {
        triangulatedNormals = GeoUtils3d.triangulate(getVertices(((Model3dGouraudVertices) vertices).getNormals()));
      }
    }
    
    return new TriangulatedVerticesAndNormals(triangulatedVertices, triangulatedNormals);
  }
  
  protected boolean shouldCullIfBackface() {
    return !options.isEnabled(Option.meshOnly) && options.isEnabled(Option.cullIfBackface) && !color.isTransparent();
  }
}
