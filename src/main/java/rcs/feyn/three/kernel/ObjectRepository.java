package rcs.feyn.three.kernel;

import java.util.LinkedList;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import rcs.feyn.three.render.Renderable3d;
import rcs.feyn.three.render.patches.Patch3d;

public class ObjectRepository {

  private final LinkedList<Renderable3d> singleObjects = new LinkedList<>();
  
  private final LinkedList<Iterable<? extends Renderable3d>> listsOfObjects = new LinkedList<>();
  
  ObjectRepository() {
    listsOfObjects.add(singleObjects);
  }
  
  public void add(Renderable3d renderable) {
    singleObjects.add(renderable);
  }

  public void add(Iterable<? extends Renderable3d> listOfRenderables) {
    listsOfObjects.add(listOfRenderables);
  }

  public void remove(Renderable3d renderable) {
    singleObjects.remove(renderable);
  }

  public Stream<Patch3d> patches() {
    return listsOfObjects
        .stream()
        .flatMap(listOfObjects -> StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(listOfObjects.iterator(), Spliterator.ORDERED), false))
        .map(Renderable3d::getRenderablePatches)
        .flatMap(Stream::of);
  }
}
