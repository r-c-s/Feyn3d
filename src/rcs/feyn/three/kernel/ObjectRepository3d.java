package rcs.feyn.three.kernel;

import java.util.Iterator;

import rcs.feyn.struct.DoublyLinkedList;
import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.three.render.patches.Patch3d;

public class ObjectRepository3d {

  private final DoublyLinkedList<Renderable3d>
    singleObjects = new DoublyLinkedList<>();
  
  private final DoublyLinkedList<Iterable<? extends Renderable3d>> 
    listsOfObjects = new DoublyLinkedList<>(singleObjects); 

  public void add(Renderable3d renderable) {
    singleObjects.add(renderable);
  }

  public void add(Iterable<? extends Renderable3d> listOfRenderables) {
    listsOfObjects.add(listOfRenderables);
  }

  public void remove(Renderable3d renderable) {
    singleObjects.remove(renderable);
  }

  public Iterable<? extends Renderable3d> remove(Iterable<? extends Renderable3d> listOfRenderables) {
    return listsOfObjects.remove(listOfRenderables);
  }
  
  public Iterator<Patch3d> patchesIterator() {
    return new Iterator<Patch3d>() {
      private Iterator<Renderable3d>
        objectsIterator = objectsIterator();
      
      private Patch3d[]
        currentArray = objectsIterator.hasNext() 
          ? objectsIterator.next().getRenderablePatches()
          : null;
          
      private int currentIndex;

      @Override
      public boolean hasNext() {
        return currentArray != null 
            && currentIndex < currentArray.length
            || objectsIterator.hasNext();
      }

      @Override
      public synchronized Patch3d next() {
        if (!hasNext()) {
          return null;
        }
        if (currentIndex >= currentArray.length) {
          currentArray = objectsIterator.next().getRenderablePatches();
          currentIndex = 0;
        }
        return currentArray[currentIndex++];
      }
    };
  }

  public Iterator<Renderable3d> objectsIterator() {
    return new Iterator<Renderable3d>() {
      private Iterator<Iterable<? extends Renderable3d>>
        listsOfObjectsIterator = listsOfObjects.iterator();
     
      private Iterator<? extends Renderable3d>
        currentIterator = listsOfObjectsIterator.hasNext()
          ? listsOfObjectsIterator.next().iterator()
          : null;
      
      @Override
      public boolean hasNext() {
        return currentIterator != null
            && currentIterator.hasNext() 
            || listsOfObjectsIterator.hasNext();
      } 
      
      @Override
      public Renderable3d next() {
        if (!hasNext()) {
          return null;
        }
        if (currentIterator == null || !currentIterator.hasNext()) { 
          currentIterator = listsOfObjectsIterator.next().iterator();
        }
        return currentIterator.next();
      }
    };
  }
}
