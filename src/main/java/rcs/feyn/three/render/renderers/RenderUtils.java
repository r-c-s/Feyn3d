package rcs.feyn.three.render.renderers;

import rcs.feyn.math.Vector2d;
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
  
  /**
   * a, b, c are treated as Vector2d; z is ignored
   */
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

//  public static Vector3d cartesianToBarycentric(Vector3d point, Vector3d a, Vector3d b, Vector3d c) {
//    double areaABC = (b.sub(a).crossProd(c.sub(a))).length();
//    
//    double lambda1 = (b.sub(point).crossProd(c.sub(point))).length()  / areaABC;
//    double lambda2 = (c.sub(point).crossProd(a.sub(point))).length()  / areaABC;
//    
//    return new Vector3d(
//      lambda1,
//      lambda2,
//      1 - lambda1 - lambda2);
//  }
  
  public static Vector2d barycentricToCartesian(Vector3d bary, Vector2d a, Vector2d b, Vector2d c) {
    return new Vector2d(
        bary.x() * a.x() + bary.y() * b.x() + bary.z() * c.x(),
        bary.x() * a.y() + bary.y() * b.y() + bary.z() * c.y());
  }
}
