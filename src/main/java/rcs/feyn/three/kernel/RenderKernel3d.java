package rcs.feyn.three.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.patches.Patch3d;

public final class RenderKernel3d {
  
  private final Collection<Patch3d> alphaBuffer = new ArrayList<>();
  
  private final ObjectRepository3d repository;

  public RenderKernel3d(ObjectRepository3d repository) {
    this.repository = repository;
  }

  public void renderAll(Graphics3d graphics) {
    View view = FeynApp3d.getView();
    
    var viewMatrix = view.getViewSpaceMatrix();
    var projMatrix = view.getPerspectiveProjectionMatrix();
    var viewPortMatrix = view.getViewPortMatrix();
    
    repository.patches()
    	.forEach(patch -> {
	    	if (patch.isTransparent()) {
		      alphaBuffer.add(patch);
	      } else {
	        patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
	      }
	    });
    
    Patch3d[] alphaObjs = alphaBuffer.toArray(new Patch3d[alphaBuffer.size()]);
    
    try {
      Arrays.sort(alphaObjs, Patch3d.DEPTH_COMPARATOR);
    } catch (IllegalArgumentException e) {
      // this error happens once in a while due to unpredictable 
      // floating-point arithmetic; safe to ignore
      if (!e.getMessage().equals("Comparison method violates its general contract!")) {
        throw e;
      }
    }
    
    for (Patch3d patch : alphaObjs) {
      patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
    } 
     
    alphaBuffer.clear();
  }
}