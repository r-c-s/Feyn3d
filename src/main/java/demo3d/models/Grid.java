package demo3d.models;

import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dTexturedFace;
import rcs.feyn.three.entities.models.Model3dVertices;
import rcs.feyn.three.gfx.TextureRaster;
import rcs.feyn.color.FeynColor;
import rcs.feyn.math.MathUtils;
import rcs.feyn.math.Vector3d;

import static rcs.feyn.three.render.RenderOptions3d.Option.*;

public class Grid extends Model3d {
  
  public Grid(double xDim, double zDim) {
    this(xDim, zDim, 1);
  }

  public Grid(double xDim, double zDim, int count) {
    setVertices(xDim, zDim, count);
    setFaces(count);
  }

  public Grid(double xDim, double zDim, int count, TextureRaster texture) {
    setVertices(xDim, zDim, count);
    setFaces(count, texture);
  }
  
  private void setVertices(double xDim, double zDim, int count) {
    double dx =  xDim / count;
    double dz =  zDim / count; 
    double x0 = -xDim / 2;
    double z0 = -zDim / 2;
    
    Vector3d[] v = new Vector3d[MathUtils.squared(count + 1)];
    for (int i = 0, k = 0; i <= count; i++) {
      for (int j = 0; j <= count; j++) {
        v[k++] = new Vector3d(x0 + i*dx, 0, z0 + j*dz);
      }
    }  
    
    vertices = new Model3dVertices(v);
  }
  
  private void setFaces(int count) {
    faces = new Model3dFace[count*count]; 

    for (int i = 0, j = 0; i < vertices.getVertices().length - count - 2; i++) {
      if ((i+1) % (count+1) != 0) {
        faces[j] = new Model3dFace(new int[]{i, i+1, i+count+2, i+count+1}, FeynColor.black); 
        faces[j].getRenderOptions().disable(cullIfBackface);
        faces[j].getRenderOptions().disable(flatShaded);
        j++;
      }
    }
  }
  
  private void setFaces(int count, TextureRaster texture) {
    faces = new Model3dTexturedFace[count*count]; 
    
    for (int i = 0, j = 0; i < vertices.getVertices().length - count - 2; i++) {
      if ((i+1) % (count+1) != 0) {
        faces[j] = new Model3dTexturedFace(new int[]{i, i+1, i+count+2, i+count+1}, texture); 
        faces[j].getRenderOptions().disable(cullIfBackface);
        faces[j].getRenderOptions().disable(flatShaded);
        j++;
      }
    }
  }
}