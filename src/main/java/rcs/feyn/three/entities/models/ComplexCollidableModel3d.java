package rcs.feyn.three.entities.models;

import rcs.feyn.math.Matrix44;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.collision.BoundingObject3d;
import rcs.feyn.three.collision.ComplexCollidable3d;

public class ComplexCollidableModel3d extends CollidableModel3d implements ComplexCollidable3d {

  protected BoundingObject3d[] innerBoundingObjects; 

  public ComplexCollidableModel3d(
      Model3dVertices vertices, 
      Model3dFace[] faces, 
      BoundingObject3d outerBoundingObject,
      BoundingObject3d[] innerBoundingObjects) {
    super(vertices, faces, outerBoundingObject);
    this.innerBoundingObjects = innerBoundingObjects;
  } 

  @Override
  public final void translate(Vector3d v3d) {
    super.translate(v3d);
    for (BoundingObject3d sphere : getInnerBoundingObjects()) {
      sphere.translate(v3d);
    }
  }

  @Override
  public final void transform(Matrix44 m4x4) {
    super.transform(m4x4);
    for (BoundingObject3d bo : getInnerBoundingObjects()) {
      bo.transform(m4x4);
    }
  }

  @Override
  public final BoundingObject3d[] getInnerBoundingObjects() {
    return innerBoundingObjects;
  }
  
}