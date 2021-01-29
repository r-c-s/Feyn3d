package rcs.feyn.three.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.math.linalg.Matrix44;

public final class RenderKernel3d {
  
  private final Collection<Patch3d> alphaBuffer = new ArrayList<>();
  
  private final ObjectRepository3d repository;

  public RenderKernel3d(ObjectRepository3d repository) {
    this.repository = repository;
  }

  public void renderAll(Graphics3d graphics) {
    Matrix44 viewMatrix = FeynApp3d.getViewSpaceMatrix();
    Matrix44 projMatrix = FeynApp3d.getPerspectiveProjectionMatrix();
    Matrix44 viewPortMatrix = FeynApp3d.getViewPortMatrix();
    
    repository.patches()
    	.forEach(patch -> {
	    	if (patch.getColor().isTransparent()) {
	        synchronized(alphaBuffer) {
	          alphaBuffer.add(patch);
	        }
	      } else {
	        patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
	      }
	    });
    
    Patch3d[] alphaObjs = alphaBuffer.toArray(new Patch3d[alphaBuffer.size()]);
    Arrays.sort(alphaObjs, Patch3d.DEPTH_COMPARATOR);
    
    for (Patch3d patch : alphaObjs) {
      patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
    } 
     
    alphaBuffer.clear();
  }
}