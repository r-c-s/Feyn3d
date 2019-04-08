package rcs.feyn.two.kernel;

import rcs.feyn.struct.DoublyLinkedList;
import rcs.feyn.two.gfx.Graphics2d;
import rcs.feyn.two.render.Renderable2d;

public class RenderKernel2d {
    
  private final DoublyLinkedList<Renderable2d>
    singleObjects = new DoublyLinkedList<>();
  
  private final DoublyLinkedList<Iterable<? extends Renderable2d>> 
    listsOfObjects = new DoublyLinkedList<>(singleObjects);
  
  private int numThreads;

  public RenderKernel2d(int numThreads) {
    this.numThreads = numThreads;
  }

  public int getNumThreads() {
    return numThreads;
  }

  public void setNumThreads(int numThreads) {
    this.numThreads = numThreads;
  }

  public void add(Renderable2d renderable) {
    singleObjects.add(renderable);
  }

  public void add(Iterable<? extends Renderable2d> listOfRenderables) {
    listsOfObjects.add(listOfRenderables);
  }

  public void remove(Renderable2d renderable) {
    singleObjects.remove(renderable);
  }

  public Iterable<? extends Renderable2d> remove(Iterable<? extends Renderable2d> listOfRenderables) {
    return listsOfObjects.remove(listOfRenderables);
  }

  public void renderAll(Graphics2d graphics) {
    listsOfObjects.forEach(list ->
      list.forEach(object ->
        object.render(graphics)));
  }
}
