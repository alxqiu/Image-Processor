import static org.junit.Assert.assertEquals;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import org.junit.Test;

/**
 * A tester class for the model.image.IImage interface.
 */
public class ImageTest {

  IPixel p1 = new BasicPixel(0, 0, 120, 120, 120);
  IPixel p2 = new BasicPixel(0, 1, 240, 241, 242);

  IPixel[] pixels = {p1, p2};
  IImage img = new BasicImage(1, 2, this.pixels);


  @Test(expected = IllegalArgumentException.class)
  // tests that an exception is thrown if the x and y values for any of the pixels in
  // the list is negative
  public void testNegativePixels() {
    IPixel p1 = new BasicPixel(-1, 0, 120, 120, 120);
    IPixel p2 = new BasicPixel(0, 1, 240, 241, 242);
    IPixel[] pixels = {p1, p2};
    IImage badImg = new BasicImage(1, 2, pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that an exception is thrown if the pixels have x and y values outside of
  // the width and height
  public void testOOBPixels() {
    IPixel p1 = new BasicPixel(0, 0, 120, 120, 120);
    IPixel p2 = new BasicPixel(0, 3, 240, 241, 242);
    IPixel[] pixels = {p1, p2};
    IImage badImg = new BasicImage(1, 2, pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that an exception is thrown if the pixels have the same x and y values
  public void testRepeatPixels() {
    IPixel p1 = new BasicPixel(0, 0, 120, 120, 120);
    IPixel p2 = new BasicPixel(0, 0, 240, 241, 242);
    IPixel[] pixels = {p1, p2};
    IImage badImg = new BasicImage(1, 2, pixels);
  }


  @Test
  // tests the getPixelAt method
  public void testGetPixelAt() {
    assertEquals(this.p1, this.img.getPixelAt(0, 0));
    assertEquals(this.p2, this.img.getPixelAt(0, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the exception for getPixelAt method
  // (x-value)
  public void testGetPixelAtX() {
    this.img.getPixelAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the exception for getPixelAt method
  // (y-value)
  public void testGetPixelAtY() {
    this.img.getPixelAt(0, -1);
  }

  @Test
  // tests if the getWidth observer returns the correct width of the model.image
  public void testGetWidth() {
    assertEquals(1, this.img.getWidth());
  }

  @Test
  // tests if the getHeight observer returns the correct width of the model.image
  public void testGetHeight() {
    assertEquals(2, this.img.getHeight());
  }

  @Test
  // test for the factory method
  public void testFactory() {
    IPixel[] singlePixelArr = new BasicPixel[]{new BasicPixel(0, 0, 20, 20, 20)};
    IImage singlePixelExample = img.createImage(1, 1, singlePixelArr);
    assertEquals(singlePixelExample.getPixelAt(0, 0), singlePixelArr[0]);
    assertEquals(singlePixelExample.getHeight() * singlePixelExample.getWidth(), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  // testing exceptions for the factory method
  public void testExceptionFactory() {
    IPixel[] singlePixelArr = new BasicPixel[]{new BasicPixel(-1, -1, 20, 20, 20)};
    img.createImage(1, 1, singlePixelArr);
  }

  @Test(expected = IllegalArgumentException.class)
  // testing exceptions for the factory method, based on invalid size of image to create
  public void testExceptionFactoryInvalidSize() {
    IPixel[] singlePixelArr = new BasicPixel[]{new BasicPixel(0, 0, 20, 20, 20)};
    img.createImage(0, 1, singlePixelArr);
  }

  @Test(expected = IllegalArgumentException.class)
  // testing exceptions for the factory method, given null
  public void testExceptionFactoryGivenNull() {
    img.createImage(1, 1, null);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if the constructor for model.image.BasicImage throws the right exception for an
  // invalid height
  public void testConstructor1() {
    IImage t = new BasicImage(20, 0, new IPixel[3]);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if the constructor for model.image.BasicImage throws the right exception
  // for an invalid width
  public void testConstructor2() {
    IImage t = new BasicImage(0, 20, new IPixel[3]);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if the constructor for model.image.BasicImage throws the right exception
  // for an invalid list of Pixels
  public void testConstructorList() {
    IPixel[] pixels = new IPixel[1];
    pixels[0] = new BasicPixel(0, 0, 0, 0, 0);
    IImage t = new BasicImage(20, 20, pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests if the constructor for model.image.BasicImage throws the right exception if given
  // an array of model.image.IPixel that contains an model.image.IPixel with a position out
  // of bounds
  public void givenInvalidPixel() {
    IPixel[] pixels = new IPixel[4];
    pixels[0] = new BasicPixel(0, 0, 0, 0, 0);
    pixels[1] = new BasicPixel(0, 1, 0, 0, 0);
    pixels[2] = new BasicPixel(1, 0, 0, 0, 0);
    // this pixel has a y of 2, the max y value for a 2x2 model.image should be 1
    pixels[3] = new BasicPixel(0, 2, 0, 0, 0);
    IImage newImage = new BasicImage(2, 2, pixels);
  }
}