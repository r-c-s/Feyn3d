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
    
    // Asert
    assertThat(target.isEmpty()).isFalse();
    assertThat(target.size()).isEqualTo(1);
    assertThat(target.contains("Hello world!")).isTrue();
  }
}
