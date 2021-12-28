package rcs.feyn.utils.struct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DoubleLinkedListTest {
  
  @Test
  void testAdd() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    
    // Act
    target.add("Hello world!");
    
    // Assert
    assertThat(target.isEmpty()).isFalse();
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("Hello world!")).isTrue();
  }
  
  @Test
  void testAddNull() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    
    // Act
    assertThrows(NullPointerException.class, () -> target.add(null));
    
    // Assert
    assertThat(target.isEmpty()).isTrue();
  }
  
  @Test
  void testRemoveFirst() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    String actual = target.removeFirst();
    
    // Assert
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("2")).isTrue();
    assertThat(target.contains("1")).isFalse();
    assertThat(actual).isEqualTo("1");
  }
  
  @Test
  void testRemoveLast() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    String actual = target.removeLast();
    
    // Assert
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("1")).isTrue();
    assertThat(target.contains("2")).isFalse();
    assertThat(actual).isEqualTo("2");
  }
  
  @Test
  void testRemoveDoesNotContain() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    String actual = target.remove("3");
    
    // Assert
    assertThat(target.size()).isEqualTo(2);
    assertThat(target.contains("1")).isTrue();
    assertThat(target.contains("2")).isTrue();
    assertThat(actual).isNull();
  }

  @Test
  void testIterator() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    Iterator<String> it = target.iterator();
    
    // Assert
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo("1");
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo("2");
    assertThat(it.hasNext()).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"head", "in-between-0", "in-between-1", "in-between-2", "tail"})
  void testIteratorRemoveOne(String toRemove) {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("head");
    target.add("in-between-0");
    target.add("in-between-1");
    target.add("in-between-2");
    target.add("tail");
    
    // Act
    for (Iterator<String> it = target.iterator(); it.hasNext(); ) {
      String current = it.next();
      if (current.equals(toRemove)) {
        it.remove();
        break;
      }
    }
    
    // Assert
    assertThat(target.size()).isEqualTo(4);
    assertThat(target.contains(toRemove)).isFalse();
  }

  @Test
  void testIteratorRemoveAll() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("head");
    target.add("in-between-0");
    target.add("in-between-1");
    target.add("in-between-2");
    target.add("tail");
    
    // Act
    for (Iterator<String> it = target.iterator(); it.hasNext(); ) {
      String current = it.next();
      it.remove();
    }
    
    // Assert
    assertThat(target.isEmpty()).isTrue();
  }
}
