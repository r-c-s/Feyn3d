package demo2d;

import rcs.feyn.gui.FeynFrame;

public class Demo extends Demo2d {

    private static final long serialVersionUID = 1L;
    
    @Override
    protected void initialize() {
      super.initialize(); 
    }
    
    public static void main(String[] args) {
      FeynFrame frame = new FeynFrame(800, 800, "Demo 2d", true, false);
      Demo2d demo = new Demo();
      frame.add("Center", demo);
      frame.setVisible(true);
      demo.init();
    }
}
