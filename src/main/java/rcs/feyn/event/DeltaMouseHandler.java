package rcs.feyn.event;
 
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import rcs.feyn.math.Vector2d;

public class DeltaMouseHandler implements MouseMotionListener {

  private final Vector2d pr = new Vector2d();
  
  private final DeltaMouseListener dml;

  public DeltaMouseHandler(DeltaMouseListener dml) {
    this.dml = dml;
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    var r  = new Vector2d(e.getX(), e.getY());
    var dr = r.sub(pr);

    dml.deltaMouseMoved(r, dr);
    
    pr.set(r);  
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    var r  = new Vector2d(e.getX(), e.getY());
    var dr = r.sub(pr);

    dml.deltaMouseDragged(r, dr);

    pr.set(r);  
  } 
}
