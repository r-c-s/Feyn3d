package rcs.feyn.three.entities.primitives;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.three.render.patches.Point3dPatch;

public class Point3d extends Primitive3d {
  
  // micro-optimization to skip equals check on getRenderablePatches()
  private final boolean isStatic;
  
  private Patch3d[] lastPatches;
  private Vector3d lastPosition;
  
  public Point3d(Vector3d position) {
    this(position, false);
  }
  
  public Point3d(Vector3d position, boolean isStatic) {
    setPosition(position);
    this.isStatic = isStatic;
  }

  @Override
  public Vector3d getCenterOfMass() {
    return getPosition();
  }

  @Override
  public synchronized Patch3d[] getRenderablePatches() {
    if (isHidden()) {
      return new Patch3d[] {};
    }
    
    if (null == lastPatches || (!isStatic && positionHasChanged())) {
      return lastPatches = new Patch3d[] { new Point3dPatch(position, color, options) };
    }
    
    return lastPatches;
  }
  
  private boolean positionHasChanged() {
    return !position.equals(lastPosition);
  }
}