package rcs.feyn.three.render.renderers;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.linalg.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;

public class GouraudPolygon3dRenderer {

  public static void render(
      Graphics3d graphics, 
      Vector3d[] viewPortCoords, 
      double[] intensities,
      int[] colors) {
    
    int size = viewPortCoords.length;
    if (size < 3) {
      return;
    }
    
    int gw = (int) graphics.getRaster().getWidth();
    int gh = (int) graphics.getRaster().getHeight(); 
    
    RenderUtils.triangulateWithIndex(viewPortCoords, (va, vb, vc, ia, ib, ic) -> {

      double xa = va.x();
      double xb = vb.x();
      double xc = vc.x();
      double xd = (xa+xb+xc)/3;
      
      double ya = va.y();
      double yb = vb.y();
      double yc = vc.y();
      double yd = (ya+yb+yc)/3;       
      
      double za = va.z();
      double zb = vb.z();
      double zc = vc.z();
      double zd = (za+zb+zc)/3;

      int car = ColorUtils.getRedFromRGBA(colors[ia]);
      int cag = ColorUtils.getGreenFromRGBA(colors[ia]);
      int cab = ColorUtils.getBlueFromRGBA(colors[ia]);

      int cbr = ColorUtils.getRedFromRGBA(colors[ib]);
      int cbg = ColorUtils.getGreenFromRGBA(colors[ib]);
      int cbb = ColorUtils.getBlueFromRGBA(colors[ib]);

      int ccr = ColorUtils.getRedFromRGBA(colors[ic]);
      int ccg = ColorUtils.getGreenFromRGBA(colors[ic]);
      int ccb = ColorUtils.getBlueFromRGBA(colors[ic]);
      
      double u1 = xb - xa;
      double v1 = xc - xa;
      double u2 = yb - ya;
      double v2 = yc - ya;
      double u3 = zb - za;
      double v3 = zc - za;
      
      double aa = u2*v3 - u3*v2;
      double bb = u3*v1 - u1*v3;
      double cc = u1*v2 - u2*v1;
      
      double dInvZdx = -aa/cc;
      double dZdy = -bb/cc;
       
      int ymin = MathUtils.roundToInt(MathUtils.max(MathUtils.min(ya, yb, yc), 0));
      int ymax = MathUtils.roundToInt(MathUtils.min(MathUtils.max(ya, yb, yc), gh));
        
      for (int y = ymin; y <= ymax; y++) {
        double ximin = Integer.MIN_VALUE;
        double xjmin = Integer.MIN_VALUE;
        double xkmin = Integer.MIN_VALUE;
        double ximax = Integer.MAX_VALUE;
        double xjmax = Integer.MAX_VALUE;
        double xkmax = Integer.MAX_VALUE; 
        
        if ((y - yb) * (y - yc) <= 0 && yb != yc) {
          ximax = xc + (y - yc)/(yb - yc) * (xb - xc);
          ximin = ximax;
        }
        if ((y - yc) * (y - ya) <= 0 && yc != ya) {
          xjmax = xa + (y - ya)/(yc - ya) * (xc - xa);
          xjmin = xjmax;
        }
        if ((y - ya) * (y - yb) <= 0 && ya != yb) {
          xkmax = xb + (y - yb)/(ya - yb) * (xa - xb);
          xkmin = xkmax;
        }
        
        int xmin = MathUtils.roundToInt(MathUtils.max(MathUtils.min(ximax, xjmax, xkmax), 0));
        int xmax = MathUtils.roundToInt(MathUtils.min(MathUtils.max(ximin, xjmin, xkmin), gw));

        double invZ = zd + (y-yd)*dZdy + (xmin-xd)*dInvZdx;
        
        for (int x = xmin; x < xmax; x++, invZ += dInvZdx) {

          double[] t = RenderUtils.cartesianToBarycentric(x, y, va, vb, vc);
          
          int color = new FeynColor(
              MathUtils.roundToInt(car * t[0] + cbr * t[1] + ccr * t[2]),
              MathUtils.roundToInt(cag * t[0] + cbg * t[1] + ccg * t[2]),
              MathUtils.roundToInt(cab * t[0] + cbb * t[1] + ccb * t[2])).getRGBA();
          
          graphics.putPixel(x, y, invZ, color); 
        } 
      } 
    });
  }
}