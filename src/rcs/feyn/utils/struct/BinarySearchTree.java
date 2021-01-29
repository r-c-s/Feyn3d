package rcs.feyn.utils.struct;

import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<T> implements Iterable<T> {
  
  private Comparator<? super T> comparator;
  private Node root;
  private int size;

  private class Node {
    T item; 
    Node top;
    Node left;
    Node right;
    
    public Node(T item) {
      this(item, null, null, null);
    }

    public Node(T item, Node top, Node left, Node right) {
      this.item  = item; 
      this.top   = top;
      this.left  = left;
      this.right = right;
    }
  }

  public BinarySearchTree() {
    this(new NaturalOrder<T>());
  }

  public BinarySearchTree(Comparator<? super T> comparator) {
    this.comparator = comparator;
    this.size = 0;
  }

  public void add(T item) { 
    if (null == root) { 
      root = new Node(item);
    }
    else {
      add(item, root); 
    }
    size++;
  }

  private void add(T item, Node root) { 
    int cmp = comparator.compare(item, root.item);
    if (cmp < 0) {
      if (null == root.left) {
        root.left = new Node(item, root, null, null); 
      }
      else {
        add(item, root.left);
      }
    }
    else {
      if (null == root.right) {
        root.right = new Node(item, root, null, null); 
      }
      else {
        add(item, root.right);
      }
    }
  }

  public boolean contains(T item) {
    return null != get(item, root);
  } 

  private Node get(T item, Node root) {
    if (null == root || item == root.item) {
      return root;
    }
    int cmp = comparator.compare(item, root.item);
    if (cmp < 0) {
      return get(item, root.left);
    }
    else {
      return get(item, root.right);
    }
  }

  public void clear() {
    root = null;
    size = 0;
  }

  public int size() {
    return size;
  }

  public T min() {
    return min(root).item;
  }

  public T max() {
    return max(root).item;
  }

  private Node min(Node root) {
    if (null == root) {
      return null;
    }
    Node min = root;
    while (null != min.left) {
      min = min.left;
    }
    return min;
  }

  private Node max(Node root) {
    if (null == root) {
      return null;
    }
    Node max = root;
    while (null != max.right) {
      max = max.right;
    }
    return max;
  } 

  public Iterator<T> iterator() {
    return new OrderedBinaryTreeIterator();
  }
  
  private class OrderedBinaryTreeIterator implements Iterator<T> { 
    private Node current = min(root); 

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        return null;
      }
      
      T item  = current.item;
      current = selectNext(current);
      return item;
    }
    
    private Node selectNext(Node curr) {
      if (null != curr.right) {
        return min(curr.right);
      }
      if (null != curr.top && curr.top.left == curr) {
        return curr.top;
      }
      Node next = curr;
      while (null != next.top && next.top.right == next) {
        next = next.top;
      }
      return next.top;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  } 
}
