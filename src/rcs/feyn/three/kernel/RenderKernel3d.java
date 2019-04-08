package rcs.feyn.three.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.math.linalg.Matrix44;
import rcs.feyn.utils.ThreadPool;

public final class RenderKernel3d {
  
  private final Collection<Patch3d> alphaBuffer = new ArrayList<>();
  
  private final ObjectRepository3d repository;
  
  private int numThreads;

  public RenderKernel3d(int numThreads, ObjectRepository3d repository) {
    this.numThreads = numThreads;
    this.repository = repository;
  }

  public void renderAll(Graphics3d graphics) {
    Matrix44 viewMatrix = FeynApp3d.getViewSpaceMatrix();
    Matrix44 projMatrix = FeynApp3d.getPerspectiveProjectionMatrix();
    Matrix44 viewPortMatrix = FeynApp3d.getViewPortMatrix();
    
    Iterator<Patch3d> iterator = repository.patchesIterator();
    
    ThreadPool tp = new ThreadPool(numThreads);
    
    for (int i = 0; i < numThreads; i++) {
      tp.runTask(() -> {
        while (true) {
          Patch3d patch = iterator.next();
          
          if (patch == null) {
            break;
          } else if (patch.getColor().isTransparent()) {
            synchronized(alphaBuffer) {
              alphaBuffer.add(patch);
            }
          } else {
            patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
          }
        }
      });
    }
    
    tp.join();
    
    Patch3d[] alphaObjs = alphaBuffer.toArray(new Patch3d[alphaBuffer.size()]);
    Arrays.parallelSort(alphaObjs, Patch3d.DEPTH_COMPARATOR);
    
    for (Patch3d patch : alphaObjs) {
      patch.render(graphics, viewMatrix, projMatrix, viewPortMatrix);
    } 
     
    alphaBuffer.clear();
  }
}