package rcs.feyn.struct;

import java.util.Iterator;

import rcs.feyn.struct.DoublyLinkedList;

public class FeynLinkedList<T extends FeyngGarbageCollectable> extends FeynCollection<T> {
  
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
