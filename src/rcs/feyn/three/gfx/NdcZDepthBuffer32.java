package rcs.feyn.three.gfx;

import java.util.Arrays;

public class NdcZDepthBuffer32 implements DepthBuffer {

  protected float[] depthBuffer; 

  public NdcZDepthBuffer32(int size) {
    depthBuffer = new float[size];
    clear();
  } 

  public int size() {
    return depthBuffer.length;
  }

  public void clear() {
    Arrays.fill(depthBuffer, 1.0f);
  } 
  
  @Override
  public boolean checkAndSetDepth(int index, double invZ) {
    if (index < 0 || index >= depthBuffer.length) {
      return false;
    }
    if (0 < invZ && invZ < depthBuffer[index]) {
      depthBuffer[index] = (float) invZ;
      return true;
    } 
    return false;
  } 
}
