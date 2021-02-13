package rcs.feyn.three.gfx;

public class Raster {

  protected final int width, height;
  
  protected final int[] data;

  public Raster(int width, int height) {
    this(new int[width*height], width, height);
  } 

  public Raster(int[] data, int width, int height) {
    this.data = data;
    this.width = width;
    this.height = height;
  } 

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int size() {
    return data.length;
  }

  public int[] getData() {
    return data;
  }

  public int getPixel(int x, int y) {
    return getPixel(y * width + x);
  }

  public int getPixel(int index) {
    return data[index];
  }

  public void setPixel(int x, int y, int color) {
    setPixel(y * width + x, color);
  }

  public void setPixel(int index, int color) {
    data[index] = color;
  }
}
