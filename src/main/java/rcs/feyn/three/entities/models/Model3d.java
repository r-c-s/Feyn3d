package rcs.feyn.three.entities.models;

import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.three.entities.Sprite3d;
import rcs.feyn.three.render.patches.Patch3d;

import java.util.Arrays;

import rcs.feyn.color.FeynColor;
import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;

public class Model3d extends Sprite3d implements Renderable3d {
  
  protected Model3dFace[] faces;
  protected Model3dVertices vertices;
  
  private Model3dVertices lastSnapshot;
  private boolean changedSinceLastSnapshot = true;
  
  protected Model3d() { }

  public Model3d(Model3dVertices vertices, Model3dFace[] faces) {
    this.vertices = vertices;
    this.faces = faces;
  }
  
  final void setVertices(Model3dVertices vertices) {
    this.vertices = vertices;
  }
  
  final void setFaces(Model3dFace[] faces) {
    this.faces = faces;
  }

  public final Vector3d getCenterOfMass() {
    return vertices.centerOfMass();
  }

  public final Vector3d getCenter() {
    return vertices.center();
  }

  public final Model3dVertices getVertices() {
    return vertices;
  }

  public final Model3dFace[] getFaces() {
    return faces;
  }

  public final void setColorToAllFaces(FeynColor color) {
    for (var face : faces) {
      face.setColor(color);
    }
  }

  @Override
  public synchronized void translate(Vector3d v3d) {
    super.translate(v3d);
    vertices.translate(v3d);
    changedSinceLastSnapshot = true;
  }

  @Override
  public synchronized void transform(Matrix44 m44) {
    super.transform(m44);
    vertices.transform(m44);
    changedSinceLastSnapshot = true;
  }

  @Override
  public final Patch3d[] getRenderablePatches() {
    if (isHidden()) {
      return new Patch3d[] {};
    }
    
    if (changedSinceLastSnapshot) { 
      synchronized(this) {
        if (changedSinceLastSnapshot) {
          lastSnapshot = vertices.clone();
          changedSinceLastSnapshot = false;
        }
      }
    }
    
    return Arrays.stream(faces)
        .map(face -> face.makePatch(lastSnapshot))
        .toArray(Patch3d[]::new);
  }
}
