package rcs.feyn.three.entities.primitives;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.three.render.patches.Point3dPatch;

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
        new Point3dPatch(pos, color, options) 
    };
  }
}