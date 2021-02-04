package rcs.feyn.utils.struct;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DoubleLinkedListTest {
  
  @Test
  public void testAdd() {
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
  public void testRemoveFirst() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    String actual = target.removeFirst();
    
    // Assert
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("1")).isFalse();
    assertThat(actual).isEqualTo("1");
  }
  
  @Test
  public void testRemoveLast() {
    // Arrange
    DoublyLinkedList<String> target = new DoublyLinkedList<>();
    target.add("1");
    target.add("2");
    
    // Act
    String actual = target.removeLast();
    
    // Assert
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("2")).isFalse();
    assertThat(actual).isEqualTo("2");
  }
}
