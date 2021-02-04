package rcs.feyn.gui;

import javax.swing.JFrame;

public class FeynFrame extends JFrame {
  
  private static final long serialVersionUID = 1L;

  public FeynFrame(int w, int h, String title, boolean resizable, boolean fullscreen) {
    setSize(w, h);
    setResizable(resizable);
    setVisible(true);
    setTitle(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    if (fullscreen) {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
  }
}
