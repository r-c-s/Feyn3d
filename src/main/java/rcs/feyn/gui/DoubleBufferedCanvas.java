package rcs.feyn.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import rcs.feyn.color.FeynColor;
import rcs.feyn.three.gfx.Graphics3d;
import rcs.feyn.three.gfx.Raster;

public abstract class DoubleBufferedCanvas extends JComponent {
   
  private static final long serialVersionUID = 1L;

  private final Object renderingLock = new Object();
  
  private BufferedImage imageBuffer = null;
  private Graphics3d graphics = null;  
  private FeynColor background = FeynColor.black;

  public DoubleBufferedCanvas(Graphics3d graphics) {
    this.addComponentListener(new ComponentResized());
    this.graphics = graphics; 
  }

  protected abstract void render(Graphics3d graphics);
  
  protected abstract void setWidth(int width);
  
  protected abstract void setHeight(int height);

  public final void repaintSynchronous() {
    Dimension size = getSize();
    paintImmediately(0, 0, size.width, size.height);
  }

  public final void paint(Graphics g) { 
    synchronized (renderingLock) {   
      graphics.fill(background);
      render(graphics);
      g.drawImage(imageBuffer, 0, 0, null);
      g.dispose();  
    } 
  }

  public void setBackgroundColor(FeynColor background) {
    this.background = background;
  }
  
  protected void screenshot(String filepath) {
    if (imageBuffer == null) {
      return;
    }
    try {
      File outputfile = new File(filepath);
      synchronized (renderingLock) {
        ImageIO.write(imageBuffer, "png", outputfile);
      }
    } 
    catch (IOException e) { }
  }
  
  protected void setImageBuffer(BufferedImage imageBuffer) {
    this.imageBuffer = imageBuffer;
  }
  
  private class ComponentResized implements ComponentListener {

    @Override
    public final void componentResized(ComponentEvent e) {
      synchronized (renderingLock) {
        Dimension size = getSize();
        int w = size.width, h = size.height;
        setWidth(w);
        setHeight(h);
        imageBuffer = (BufferedImage) createImage(w, h); 
        graphics.setRaster(new Raster(
            ((DataBufferInt) imageBuffer.getRaster().getDataBuffer()).getData(), 
            w, h));
      }
    }

    @Override
    public void componentHidden(ComponentEvent e) { }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }
  }
}
