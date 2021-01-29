package rcs.feyn.utils.struct;

import java.util.Iterator;
import java.util.stream.StreamSupport;

public class DoublyLinkedList<T> implements Iterable<T> { 
 
  private Node head;
  private Node tail;
  
  private int N = 0;

  private class Node {
    T item;
    Node prev;
    Node next;
    
    public Node(T item) {
      this(item, null, null);
    }

    public Node(T item, Node prev, Node next) {
      this.item = item;
      this.prev = prev;
      this.next = next;
    }
  }

  public DoublyLinkedList() { }

  public DoublyLinkedList(T item) {
    add(item);
  }

  public boolean isEmpty() {
    return head == null;
  }
  
  public void clear() {
    head = null;
    tail = null;
    N = 0;
  }
  
  public int size() {
    return N;
  }

  public void add(T item) {
    if (isEmpty()) {
      Node n = new Node(item); 
      head = n;
      tail = n;
    }
    else {
      Node n = new Node(item, tail, null);
      tail.next = n;
      tail      = n;
    }
    N++;
  }
  
  public void addAll(DoublyLinkedList<? extends T> other) {
    for (T item : other) {
      add(item);
    }
  }

  public boolean contains(T item) {
    return null != get(item);
  }

  public T remove(T item) {
    Node n = get(item);
    if (null == n) {
      return null;
    }
    else {
      unlink(n);
      return n.item;
    }
  }

  public T removeFirst() {
    if (!isEmpty()) {
      unlink(head);
      return head.item;
    }
    else {
      return null;
    }
  }

  public T removeLast() {
    if (!isEmpty()) {
      unlink(tail);
      return tail.item;
    }
    else {
      return null;
    }
  }

  private Node get(T item) {
    Node current = head;
    while (current != null && current.item != item) {
      current = current.next;
      if (current == head) {
        return null;
      }
    }
    return current;
  }

  private Node unlink(Node n) {
    if (--N == 0) {
    	clear();
    }
    else {
	    if (n != head && n != tail) { 
	      n.next.prev = n.prev;
	      n.prev.next = n.next;
	    }
	    if (n == head) {
	      head = n.next; 
	    }
	    if (n == tail) {
	      tail = n.prev; 
	    }
    }
    return n;
  }

  @Override
  public Iterator<T> iterator()  {
    return new LinkedListIterator();
  }
  
  private class LinkedListIterator implements Iterator<T> {
    private Node current = head; 
    
    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public void remove() {
      unlink(null == current ? tail : current.prev);  
    }

    @Override
    public T next() {
      if (!hasNext()) {
        return null;
      } 
      
      T item  = current.item;
      current = current.next;
      return item;
    } 
  }
}
