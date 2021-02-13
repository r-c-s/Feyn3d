package rcs.feyn.three.render.renderers;

import rcs.feyn.math.Vector3d;
import rcs.feyn.utils.HexaConsumer;
import rcs.feyn.utils.TriConsumer;

public class RenderUtils {
  
  public static void triangulate(
      Vector3d[] vertices, 
      TriConsumer<Vector3d, Vector3d, Vector3d> consumer) {
    
    triangulateWithIndex(vertices, (va, vb, vc, a, b, c) -> {
      consumer.accept(va, vb, vc);
    });
  }  
  
  public static void triangulateWithIndex(
      Vector3d[] vertices, 
      HexaConsumer<Vector3d, Vector3d, Vector3d, Integer, Integer, Integer> consumer) {
    
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
  
  public static Vector3d cartesianToBarycentric(int x, int y, Vector3d a, Vector3d b, Vector3d c) {
    double x1 = a.x();
    double x2 = b.x();
    double x3 = c.x();
    double y1 = a.y();
    double y2 = b.y();
    double y3 = c.y();
    
    double y2y3 = y2 - y3;
    double x3x2 = x3 - x2;
    double x1x3 = x1 - x3;
    double y1y3 = y1 - y3;
    double y3y1 = y3 - y1;
    double xx3  = x  - x3;
    double yy3  = y  - y3;
    
    double d = y2y3 * x1x3 + x3x2 * y1y3;
    double lambda1 = (y2y3 * xx3 + x3x2 * yy3) / d;
    double lambda2 = (y3y1 * xx3 + x1x3 * yy3) / d;
    
    return new Vector3d(
      lambda1,
      lambda2,
      1 - lambda1 - lambda2);
  }
}
