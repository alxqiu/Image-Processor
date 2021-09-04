import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import model.image.BasicPixel;
import model.image.IPixel;
import org.junit.Test;

/**
 * A tester class for the model.image.IPixel interface.
 */
public class PixelTest {

  IPixel pixel = new BasicPixel(10, 20, 1, 2, 3);

  @Test
  // tests if the getX observer returns the correct value
  public void testGetX() {
    assertEquals(10, pixel.getX());
  }

  @Test
  // tests if the getY observer returns the correct value
  public void testGetY() {
    assertEquals(20, pixel.getY());
  }

  @Test
  // tests if the getRed observer returns the correct value
  public void testGetRed() {
    assertEquals(1, pixel.getRed());
  }

  @Test
  // tests if the getGreen observer returns the correct value
  public void testGetGreen() {
    assertEquals(2, pixel.getGreen());
  }

  @Test
  // tests if the getBlue observer returns the correct value
  public void testGetBlue() {
    assertEquals(3, pixel.getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if an invalid parameter in a Pixel constructor throws the correct exception
  // (x-coordinate)
  public void testConstructorX() {
    IPixel p = new BasicPixel(-1, 0, 0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if an invalid parameter in a Pixel constructor throws the correct exception
  // (y-coordinate)
  public void testConstructorY() {
    IPixel p = new BasicPixel(0, -1, 0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if an invalid parameter in a Pixel factory method throws the correct exception
  // (x-coordinate)
  public void testFactoryX() {
    pixel.createPixel(-2, 1, 20, 20, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if an invalid parameter in a Pixel factory method throws the correct exception
  // (y-coordinate)
  public void testFactoryY() {
    pixel.createPixel(1, -1, 20, 20, 20);
  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (red > 255)
  public void testConstructorRedMax() {
    IPixel p = new BasicPixel(0, 0, 256, 0, 0);
    assertEquals(255, p.getRed());
  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (green > 256)
  public void testConstructorGreenMax() {
    IPixel p = new BasicPixel(0, 0, 0, 256, 0);
    assertEquals(255, p.getGreen());

  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (blue > 256)
  public void testConstructorBlueMin() {
    IPixel p = new BasicPixel(0, 0, 0, 0, 256);
    assertEquals(255, p.getBlue());

  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (red < 0)
  public void testConstructorRedMin() {
    IPixel p = new BasicPixel(0, 0, -1, 0, 0);
    assertEquals(0, p.getRed());

  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (green < 0)
  public void testConstructorGreenMin() {
    IPixel p = new BasicPixel(0, 0, 0, -1, 0);
    assertEquals(0, p.getGreen());

  }

  @Test
  // tests if an invalid parameter in a Pixel constructor clamps to the right value
  // (blue < 0)
  public void testConstructorBlueMax() {
    IPixel p = new BasicPixel(0, 0, 0, 0, -1);
    assertEquals(0, p.getBlue());
  }

  @Test
  // tests the factor method of IPixel
  public void testCreatePixel() {
    IPixel p = new BasicPixel(0, 0, 0, 0, 0);
    assertEquals(p, p.createPixel(0, 0, 0, 0, 0));
  }

  @Test
  // tests the equals method of IPixel
  public void testEqualPixel() {
    IPixel p = new BasicPixel(0, 0, 0, 0, 0);
    IPixel p1 = new BasicPixel(0, 0, 100, 100, 100);
    IPixel p2 = new BasicPixel(0, 0, 0, 0, 0);
    assertEquals(p, p2);
    assertNotEquals(p1, p2);
    assertNotEquals(p1, p);
    assertTrue(p.equals(p2));
    assertFalse(p.equals(p1));
  }

  @Test
  // tests the hashCode method of IPixel
  public void testHashCodePixel() {
    IPixel p = new BasicPixel(0, 0, 0, 0, 0);
    IPixel p1 = new BasicPixel(0, 0, 100, 100, 100);
    IPixel p2 = new BasicPixel(0, 0, 0, 0, 0);
    assertEquals(p.hashCode(), p2.hashCode());
    assertNotEquals(p1.hashCode(), p2.hashCode());
  }
}
