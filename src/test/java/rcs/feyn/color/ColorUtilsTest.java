package rcs.feyn.color;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ColorUtilsTest {
  
  @Test
  void testGetRedFromRGBA() {
    // Arrange
    FeynColor color = new FeynColor(1, 2, 3);
    
    // Act
    int red = ColorUtils.getRedFromRGBA(color.getRGBA());
    
    // Assert    
    assertThat(red).isEqualTo(color.getRed());
  }
  
  @Test
  void testGetGreenFromRGBA() {
    // Arrange
    FeynColor color = new FeynColor(1, 2, 3);
    
    // Act
    int green = ColorUtils.getGreenFromRGBA(color.getRGBA());
    
    // Assert    
    assertThat(green).isEqualTo(color.getGreen());
  }
  
  @Test
  void testGetBlueFromRGBA() {
    // Arrange
    FeynColor color = new FeynColor(1, 2, 3);
    
    // Act
    int blue = ColorUtils.getBlueFromRGBA(color.getRGBA());
    
    // Assert    
    assertThat(blue).isEqualTo(color.getBlue());
  }
  
  @Test
  void testGetAlphaFromRGBA() {
    // Arrange
    FeynColor color = new FeynColor(1, 2, 3, 4);
    
    // Act
    int alpha = ColorUtils.getAlphaFromRGBA(color.getRGBA());
    
    // Assert    
    assertThat(alpha).isEqualTo(color.getAlpha());
  }
  
  @Test
  void testMulRGBA() {
    // Arrange
    FeynColor color = new FeynColor(1, 2, 3, 4);
    
    // Act
    int result = ColorUtils.mulRGBA(color.getRGBA(), 5);
    
    // Assert    
    assertThat(result).isEqualTo(new FeynColor(5, 10, 15, 20).getRGBA());
  }
  
  @Test
  void testAddRGBA() {
    // Arrange
    FeynColor colorA = new FeynColor(1, 2, 3, 4);
    FeynColor colorB = new FeynColor(5, 6, 7, 8);
    
    // Act
    int result = ColorUtils.addRGBA(colorA.getRGBA(), colorB.getRGBA());
    
    // Assert    
    assertThat(result).isEqualTo(new FeynColor(6, 8, 10, 12).getRGBA());
  }

}
