package rcs.feyn.three.render;

import rcs.feyn.three.render.patches.Patch3d;

public interface Renderable3d {
  
  public Patch3d[] getRenderablePatches();
}
