package rcs.feyn.three.render.primitives;

import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.three.render.patches.Point3dPatch;
import rcs.feyn.math.linalg.Vector3d;

public class Point3d extends Primitive3d {
	
  public Point3d(Vector3d position) {
    setPosition(position);
  }

  @Override
  public Vector3d getCenterOfMass() {
    return getPosition();
  }

  @Override
  public synchronized Patch3d[] getRenderablePatches() {
  	Vector3d pos = new Vector3d(position);
    return new Patch3d[] { 
        new Point3dPatch(pos, color) 
    };
  }
}