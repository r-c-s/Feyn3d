package rcs.feyn.event;

import rcs.feyn.math.Vector2d;

public interface DeltaMouseListener {

  public void deltaMouseDragged(Vector2d r, Vector2d dr);
  
  public void deltaMouseMoved(Vector2d r, Vector2d dr);
}
