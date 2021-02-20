package rcs.feyn.three.render.renderers;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;

public class Line3dRenderer {

  public static void render(
      Graphics3d graphics, 
      Vector3d deviceCoordinateA, 
      Vector3d deviceCoordinateB, 
      int color) {    
    
    int x1 = MathUtils.roundToInt(deviceCoordinateA.x());
    int x2 = MathUtils.roundToInt(deviceCoordinateB.x());
    int y1 = MathUtils.roundToInt(deviceCoordinateA.y());
    int y2 = MathUtils.roundToInt(deviceCoordinateB.y());
    double z1 = deviceCoordinateA.z();
    double z2 = deviceCoordinateB.z();
    
    double sdx = deviceCoordinateA.x() - deviceCoordinateB.x();
    double sdy = deviceCoordinateA.y() - deviceCoordinateB.y();
    double slope = sdy / sdx;
    
    if (Math.abs(slope) <= 1) {
      if (x2 < x1) {
        int temp = x1; x1 = x2; x2 = temp;
            temp = y1; y1 = y2; y2 = temp;
        double t = z1; z1 = z2; z2 = t;
      } 
      double y = y1;
      double z = z1;
      double dzdx = (z2 - z1) / (x2 - x1); 
      for (int x = x1; x <= x2; x++, y += slope, z += dzdx) {  
        graphics.putPixel(x, MathUtils.roundToInt(y), z, color);  
      }
    } else {
      slope = 1 / slope;
      if (y2 < y1) {
        int temp = x1; x1 = x2; x2 = temp;
            temp = y1; y1 = y2; y2 = temp;
        double t = z1; z1 = z2; z2 = t;
      }     
      double x = x1;
      double invZ = z1;
      double dInvZdy = (z2 - z1) / (y2 - y1);  
      for (int y = y1; y <= y2; x += slope, y++, invZ += dInvZdy) {  
        graphics.putPixel(MathUtils.roundToInt(x), y, invZ, color);
      }
    }
  }
}