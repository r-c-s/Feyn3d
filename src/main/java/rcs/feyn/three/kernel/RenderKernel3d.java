package rcs.feyn.three.kernel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import rcs.feyn.math.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.patches.Patch3d;

public final class RenderKernel3d {

  private static final Comparator<Patch3d> DEPTH_COMPARATOR = (a, b) -> {
    Vector3d cameraPos = FeynApp3d.getView().getCamera().getPosition();
    
    double thisDepth = a.getCenter().distanceSquared(cameraPos);
    double thatDepth = b.getCenter().distanceSquared(cameraPos);
    
    return Double.compare(thisDepth, thatDepth);
  };
  
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
    
    try {
      alphaBuffer.stream()
          .sorted(DEPTH_COMPARATOR)
          .forEach(patch -> patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix));
    } catch (IllegalArgumentException e) {
      // this error happens once in a while due to unpredictable 
      // comparison of doubles (floating-point arithmetic); safe to ignore
      if (!e.getMessage().equals("Comparison method violates its general contract!")) {
        throw e;
      }
    }
     
    alphaBuffer.clear();
  }
}