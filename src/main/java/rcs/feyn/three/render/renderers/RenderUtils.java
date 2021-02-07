package rcs.feyn.three.render.renderers;

import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.utils.HexaConsumer;
import rcs.feyn.utils.TriConsumer;

public class RenderUtils {
  
  public static void triangulate(Vector3d[] vertices, TriConsumer<Vector3d, Vector3d, Vector3d> consumer) {
    triangulateWithIndex(vertices, (va, vb, vc, a, b, c) -> {
      consumer.accept(va, vb, vc);
    });
  }  
  
  public static void triangulateWithIndex(Vector3d[] vertices, HexaConsumer<Vector3d, Vector3d, Vector3d, Integer, Integer, Integer> consumer) {
    for (int i = 1; i < vertices.length-1; i++) { 
      int ia = 0;
      int ib = i;
      int ic = i+1;
      Vector3d va = vertices[0];
      Vector3d vb = vertices[i];
      Vector3d vc = vertices[i+1];
      consumer.accept(va, vb, vc, ia, ib, ic);
    }    
  }
}
