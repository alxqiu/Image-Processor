import static org.junit.Assert.assertEquals;
import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import model.operations.IOperation;
import model.operations.filter.BlurFilter;
import model.operations.filter.SharpenFilter;
import org.junit.Test;


/**
 * A tester class for the model.operations.filter.AbstractFilter class and subclassses, also tests
 * exceptions for creating with null for BlurFilter and SharpenFilter subclasses. Testing using
 * programmatically generated images and pre-visually verified filtered images.
 */
public class FilterTest {

  private IPixel topLeft = new BasicPixel(0, 0, 1, 1, 1);
  private IPixel topRight = new BasicPixel(1, 0, 2, 2, 2);
  private IPixel bottomLeft = new BasicPixel(0, 1, 4, 4, 4);
  private IPixel bottomRight = new BasicPixel(1, 1, 3, 3, 3);
  private IImage img = new BasicImage(2, 2, new IPixel[]{this.topLeft, this.topRight,
      this.bottomLeft, this.bottomRight});

  // test enforcing of invariant:
  @Test(expected = IllegalStateException.class)
  // tests that any subclass of AbstractFilter cannot use apply() if it has a non-valid kernel...
  public void testNonOddKernel() {
    new MockFilter(img).apply();
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the constructor will throw an exception if given null
  public void testBlurGivenNull() {
    new BlurFilter(null);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the constructor will throw an exception if given null
  public void testSharpenGivenNull() {
    new SharpenFilter(null);
  }

  @Test
  // tests the apply method to make sure the values change as expected
  // (square image)
  public void testBlurApplySquare() {
    IOperation blur = new BlurFilter(this.img);
    IImage edited = blur.apply();
    assertEquals(1, edited.getPixelAt(0, 0).getRed());
    assertEquals(1, edited.getPixelAt(0, 0).getGreen());
    assertEquals(1, edited.getPixelAt(0, 0).getBlue());
    assertEquals(1, edited.getPixelAt(0, 1).getRed());
    assertEquals(1, edited.getPixelAt(1, 0).getRed());
    assertEquals(1, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // tests the apply method to make sure the values change as expected
  // (square image)
  public void testSharpenApplySquare() {
    IOperation sharpen = new SharpenFilter(this.img);
    IImage edited = sharpen.apply();
    assertEquals(3, edited.getPixelAt(0, 0).getRed());
    assertEquals(3, edited.getPixelAt(0, 0).getGreen());
    assertEquals(3, edited.getPixelAt(0, 0).getBlue());
    assertEquals(5, edited.getPixelAt(0, 1).getRed());
    assertEquals(4, edited.getPixelAt(1, 0).getRed());
    assertEquals(4, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // tests the apply method to make sure that the values change as expected
  // (square image, different results)
  public void testBlurApplySquareComplex() {
    IPixel topLeft = new BasicPixel(0, 0, 8, 8, 8);
    IPixel topRight = new BasicPixel(1, 0, 16, 16, 16);
    IPixel bottomLeft = new BasicPixel(0, 1, 24, 24, 24);
    IPixel bottomRight = new BasicPixel(1, 1, 32, 32, 32);
    IImage img = new BasicImage(2, 2, new IPixel[]{topLeft, topRight,
        bottomLeft, bottomRight});
    IOperation blur = new BlurFilter(img);
    IImage edited = blur.apply();
    assertEquals(9, edited.getPixelAt(0, 0).getRed());
    assertEquals(9, edited.getPixelAt(0, 0).getGreen());
    assertEquals(9, edited.getPixelAt(0, 0).getBlue());
    assertEquals(12, edited.getPixelAt(0, 1).getRed());
    assertEquals(10, edited.getPixelAt(1, 0).getRed());
    assertEquals(13, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // tests the apply method to make sure that the values change as expected
  // (square image, different results)
  public void testSharpenApplySquareComplex() {
    IPixel topLeft = new BasicPixel(0, 0, 8, 8, 8);
    IPixel topRight = new BasicPixel(1, 0, 16, 16, 16);
    IPixel bottomLeft = new BasicPixel(0, 1, 24, 24, 24);
    IPixel bottomRight = new BasicPixel(1, 1, 32, 32, 32);
    IImage img = new BasicImage(2, 2, new IPixel[]{topLeft, topRight,
        bottomLeft, bottomRight});
    IOperation sharpenFilter = new SharpenFilter(img);
    IImage edited = sharpenFilter.apply();
    assertEquals(26, edited.getPixelAt(0, 0).getRed());
    assertEquals(26, edited.getPixelAt(0, 0).getGreen());
    assertEquals(26, edited.getPixelAt(0, 0).getBlue());
    assertEquals(38, edited.getPixelAt(0, 1).getRed());
    assertEquals(32, edited.getPixelAt(1, 0).getRed());
    assertEquals(44, edited.getPixelAt(1, 1).getRed());
  }
}
