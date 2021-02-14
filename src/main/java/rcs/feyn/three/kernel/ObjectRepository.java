package rcs.feyn.three.kernel;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.three.render.patches.Patch3d;
import rcs.feyn.utils.struct.DoublyLinkedList;

public class ObjectRepository {

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
  
	public Stream<Patch3d> patches() {
		return StreamSupport
		    .stream(Spliterators.spliteratorUnknownSize(listsOfObjects.iterator(), Spliterator.ORDERED), false)
		    .flatMap(listOfObjects -> StreamSupport
		        .stream(Spliterators.spliteratorUnknownSize(listOfObjects.iterator(), Spliterator.ORDERED), false))
		    .map(Renderable3d::getRenderablePatches)
		    .flatMap(Stream::of);
  }
}
