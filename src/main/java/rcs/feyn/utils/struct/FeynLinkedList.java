package rcs.feyn.utils.struct;

import java.util.Iterator;

import rcs.feyn.utils.struct.DoublyLinkedList;

public class FeynLinkedList<T extends FeynGarbageCollectable> extends FeynCollection<T> {
  
  private final DoublyLinkedList<T> linkedList;
  
  public FeynLinkedList() {
    linkedList = new DoublyLinkedList<T>();
  }

  public void add(T sprite) { 
    linkedList.add(sprite); 
  }

  public void clear() {
    linkedList.clear();
  }

  public int size() {
    return linkedList.size();
  }
  
  public DoublyLinkedList<T> list() {
    return linkedList;
  }

  @Override
  public Iterator<T> iterator() {
    return linkedList.iterator();
  }
}
