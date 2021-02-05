package rcs.feyn.three.entities.models;

import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.three.entities.Sprite3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.math.linalg.Vector3d;

public class Model3d extends Sprite3d implements Renderable3d {
  
  protected Model3dVertices vertices;
  protected Model3dFace[] faces;

  protected Model3d() { }

  public Model3d(Model3dVertices vertices, Model3dFace[] faces) {
    this.vertices = vertices;
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

  public final void setColor(FeynColor color) {
    for (var face : faces) {
      face.setColor(color);
    }
  }

  public final void setMeshColor(FeynColor color) {
    for (var face : faces) {
      face.setColor(color);
    }
  }

  @Override
  public void translate(Vector3d v3d) {
    super.translate(v3d);
    synchronized (this) {
      vertices.translate(v3d);
    }
  }

  @Override
  public synchronized void transform(Matrix44 m44) {
    super.transform(m44);
    synchronized (this) {
      vertices.transform(m44);
    }
  }

  @Override
  public final Patch3d[] getRenderablePatches() {
    int size = faces.length;
    Patch3d[] patches = new Patch3d[size]; 
    synchronized(this) {
      for (int i = 0; i < size; i++) {
        patches[i] = faces[i].makePatch(vertices);
      }
    }
    return patches;
  }
}
