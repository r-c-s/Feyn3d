package rcs.feyn.three.render.renderers;

import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;

public class Polygon3dRenderer {

  public static void render(Graphics3d graphics, Vector3d[] deviceCoordinates, int color) {
    if (deviceCoordinates.length != 3) {
      throw new IllegalArgumentException("Can only render triangles");
    }

    Vector3d va = deviceCoordinates[0];
    Vector3d vb = deviceCoordinates[1];
    Vector3d vc = deviceCoordinates[2];
    
    double za = va.z();
    double zb = vb.z();
    double zc = vc.z();
    double zd = (za+zb+zc)/3;
    
    double xa = va.x();
    double xb = vb.x();
    double xc = vc.x();
    double xd = (xa+xb+xc)/3;
    
    double ya = va.y();
    double yb = vb.y();
    double yc = vc.y();
    double yd = (ya+yb+yc)/3; 
    
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
    
    int screenW = graphics.getRaster().getWidth();
    int screenH = graphics.getRaster().getHeight(); 
     
    int ymin = MathUtils.roundToInt(MathUtils.max(MathUtils.min(ya, yb, yc), 0));
    int ymax = MathUtils.roundToInt(MathUtils.min(MathUtils.max(ya, yb, yc), screenH));
    
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
      int xmax = MathUtils.roundToInt(MathUtils.min(MathUtils.max(ximin, xjmin, xkmin), screenW));

      double invZ = zd + (y-yd)*dZdy + (xmin-xd)*dInvZdx;
      
      for (int x = xmin; x < xmax; x++, invZ += dInvZdx) {
        graphics.putPixel(x, y, invZ, color); 
      } 
    } 
  }
}