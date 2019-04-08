package rcs.feyn.three.gfx;

import java.util.Arrays;

public class NdcZDepthBuffer64 implements DepthBuffer {

  protected double[] depthBuffer; 

  public NdcZDepthBuffer64(int size) {
    depthBuffer = new double[size];
    clear();
  }

  public int size() {
    return depthBuffer.length;
  }

  public void clear() {
    Arrays.fill(depthBuffer, 1.0d);
  } 

  @Override
  public boolean checkAndSetDepth(int index, double invZ) {
    if (index < 0 || index >= depthBuffer.length) {
      return false;
    } 
    if (0 < invZ && invZ < depthBuffer[index]) {
      depthBuffer[index] = invZ;
      return true;
    } 
    return false;
  } 
}
