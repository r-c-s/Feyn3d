package rcs.feyn.event;

import rcs.feyn.math.Vector2d;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.Particle3d;

public class DragRotateObject implements DeltaMouseListener {
  
  private Particle3d object;
  
  private Vector3d focus;
  private Vector2d delta;
  private double dragAmount;
  
  private boolean dragged = false;

  public DragRotateObject(Particle3d object, Vector3d focus, double dragAmount) {
    this.object = object;
    this.focus = focus;
    this.dragAmount = dragAmount;
  }

  public Particle3d getObject() {
    return object;
  }

  public void setObject(Particle3d camera3d) {
    this.object = camera3d;
  }

  public Vector3d getFocus() {
    return new Vector3d(focus);
  }

  public void setFocus(Vector3d focus) {
    this.focus = focus;
  }

  public double getDragAmount() {
    return dragAmount;
  }

  public void setDragAmount(double dragAmount) {
    this.dragAmount = dragAmount;
  }

  public void update() { 
    if (dragged) { 
      if (focus == null) {
        object.rotate(object.getSideVector(), -dragAmount * delta.y());
        object.rotate(Vector3d.Y_AXIS, -dragAmount * delta.x());
      } else {
        object.rotate(focus, object.getSideVector(), -dragAmount * delta.y());
        object.rotate(focus, Vector3d.Y_AXIS, -dragAmount * delta.x());
      }
      dragged = false;
    }
  }

  @Override
  public void deltaMouseDragged(Vector2d r, Vector2d dr) {
    this.delta = dr;
    this.dragged = true;
  }

  @Override
  public void deltaMouseMoved(Vector2d r, Vector2d dr) {
    // do nothing
  } 
}
