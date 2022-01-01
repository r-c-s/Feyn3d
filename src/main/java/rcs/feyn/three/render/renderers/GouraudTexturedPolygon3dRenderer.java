package rcs.feyn.three.render.renderers;

import java.util.Optional;

import rcs.feyn.color.ColorUtils;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;
import rcs.feyn.three.kernel.FeynRuntime;

public class GouraudTexturedPolygon3dRenderer {
  
  public static void render(
      Graphics3d graphics, 
      Vector3d[] deviceCoordinates, 
      double[] intensities, 
      Optional<int[]> colors,
      Raster textureData, 
      int alpha,
      double zoom) {
    
    boolean interpolateColor = colors.isPresent();
    
    int screenW = graphics.getRaster().getWidth();
    int screenH = graphics.getRaster().getHeight(); 
    
    int tdw = textureData.getWidth();    
    int tdh = textureData.getHeight();

    RenderUtils.triangulateWithIndex(deviceCoordinates, (va, vb, vc, ia, ib, ic) -> {
      
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
      int ymax = MathUtils.roundToInt(MathUtils.min(MathUtils.max(ya, yb, yc), screenH));
      
      double A = Math.abs(cc) / 2;   
      double fa = intensities[ia] / A;  
      double fb = intensities[ib] / A;  
      double fc = intensities[ic] / A;  
      double ak0 = yc*(xb-xc)-xc*(yb-yc);  
      double ak1 = yb-yc;  
      double ak2 = xb-xc;            
      double bk0 = yc*(xa-xc)-xc*(ya-yc);  
      double bk1 = ya-yc;  
      double bk2 = xa-xc;  

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
        
        double aMin = 0.5 * Math.abs(ak0 + xmin*ak1 - y*ak2);   
        double bMin = 0.5 * Math.abs(bk0 + xmin*bk1 - y*bk2); 
        double shadeMin = fa*(aMin) + fb*(bMin) + fc*(A - aMin - bMin); 

        double aMax = 0.5 * Math.abs(ak0 + xmax*ak1 - y*ak2);   
        double bMax = 0.5 * Math.abs(bk0 + xmax*bk1 - y*bk2); 
        double shadeMax = fa*(aMax) + fb*(bMax) + fc*(A - aMax - bMax); 

        double dShadeFactorDx = (shadeMax-shadeMin) / (xmax-xmin); 
        double shadeFactor = shadeMin + (1-aMin/A)*dShadeFactorDx + (xmin-xmin)*dShadeFactorDx;

        double invZ = zd + (y-yd)*dZdy + (xmin-xd)*dInvZdx;

        for (int x = xmin; x < xmax; x++, invZ += dInvZdx, shadeFactor += dShadeFactorDx) {
          
          Vector3d t = RenderUtils.cartesianToBarycentric(x, y, va, vb, vc);
          
          double by = (tdh - 1) / zoom;
          double cx = (tdw - 1) / zoom;
          double cy = (tdh / 2) / zoom;
          
          int xdata = MathUtils.roundToInt(cx * t.z());
          int ydata = MathUtils.roundToInt(by * t.y() + cy * t.z());
          
          int pixel;
          try {
            pixel = textureData.getPixel(xdata, ydata);
          } catch (ArrayIndexOutOfBoundsException e) {
            // need to figure out why this is happening
            pixel = textureData.getPixel(tdw - 1, tdh - 1);
          }
          
          int colorWithIntensity = ColorUtils.mulRGB(pixel, shadeFactor);
          
          if (interpolateColor) {
            int[] colorz = colors.get();
            
            int interpolatedColor = ColorUtils.addRGBA(
                ColorUtils.mulRGBA(colorz[ia], t.x()),
                ColorUtils.addRGBA(
                    ColorUtils.mulRGBA(colorz[ib], t.y()),
                    ColorUtils.mulRGBA(colorz[ic], t.z())));
            
            colorWithIntensity = ColorUtils.blendRGB(
                colorWithIntensity, 
                interpolatedColor, 
                // shadeFactor comes from intensities[], which take into account
                // the ambient light, so it must be subtracted here
                // very ugly, needs a better solution
                shadeFactor - FeynRuntime.getAmbientLight().getIntensity());
          }

          int finalColor = ColorUtils.setAlphaToRGBA(colorWithIntensity, alpha);
          
          graphics.putPixel(x, y, invZ, finalColor);
        } 
      } 
    });
  }
}