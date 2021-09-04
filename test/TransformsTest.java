import static org.junit.Assert.assertEquals;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import model.operations.IOperation;
import model.operations.colortransform.Greyscale;
import model.operations.colortransform.Sepia;

import org.junit.Test;

/**
 * A tester class for the Color Transforms implementation of the IOperation interface.
 * A mix of programmatically created images and images imported from visually verified
 * filtered images.
 */
public class TransformsTest {

  private IPixel topLeft = new BasicPixel(0, 0, 1, 1, 1);
  private IPixel topRight = new BasicPixel(1, 0, 2, 2, 2);
  private IPixel bottomLeft = new BasicPixel(0, 1, 4, 4, 4);
  private IPixel bottomRight = new BasicPixel(1, 1, 3, 3, 3);
  private IImage img = new BasicImage(2, 2,
          new IPixel[]{this.topLeft, this.topRight, this.bottomLeft, this.bottomRight});

  // NOTE: changed colorMatrix in AbstractColorTransform to final, so we can't test the
  // invariant of colorMatrix always being 3x3 through a mock mutator class. By extension,
  // I also can't think of a way to trigger the IllegalStateException in pixelApply if colorMatrix
  // is final.

  @Test(expected = IllegalArgumentException.class)
  // tests that the constructor will throw an exception if given null
  public void testGreyscaleGivenNull() {
    new Greyscale(null);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the constructor will throw an exception if given null
  public void testSepiaGivenNull() {
    new Sepia(null);
  }

  @Test
  // tests the apply method to make sure the values change as expected for Greyscale
  // (square image)
  public void testGreyscaleApplySquare() {
    IOperation grey = new Greyscale(this.img);
    IImage edited = grey.apply();
    assertEquals(1, edited.getPixelAt(0, 0).getRed());
    assertEquals(1, edited.getPixelAt(0, 0).getGreen());
    assertEquals(1, edited.getPixelAt(0, 0).getBlue());
    assertEquals(4, edited.getPixelAt(0, 1).getRed());
    assertEquals(2, edited.getPixelAt(1, 0).getRed());
    assertEquals(3, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // tests the apply method to make sure the values change as expected for Sepia
  // (square image)
  public void testSepiaApplySquare() {
    IOperation sep = new Sepia(this.img);
    IImage edited = sep.apply();
    assertEquals(1, edited.getPixelAt(0, 0).getRed());
    assertEquals(1, edited.getPixelAt(0, 0).getGreen());
    assertEquals(0, edited.getPixelAt(0, 0).getBlue());
    assertEquals(5, edited.getPixelAt(0, 1).getRed());
    assertEquals(2, edited.getPixelAt(1, 0).getRed());
    assertEquals(4, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // tests the apply method to make sure that the values change as expected
  // (square image, different results)
  public void testGreyscaleApplySquareComplex() {
    IPixel topLeft = new BasicPixel(0, 0, 100, 125, 150);
    IPixel topRight = new BasicPixel(1, 0, 50, 75, 100);
    IPixel bottomLeft = new BasicPixel(0, 1, 150, 100, 50);
    IPixel bottomRight = new BasicPixel(1, 1, 200, 225, 250);
    IImage img = new BasicImage(2, 2,
            new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    IOperation greyscale = new Greyscale(img);
    IImage edited = greyscale.apply();
    assertEquals(121, edited.getPixelAt(0, 0).getRed());
    assertEquals(121, edited.getPixelAt(0, 0).getGreen());
    assertEquals(121, edited.getPixelAt(0, 0).getBlue());
    assertEquals(107, edited.getPixelAt(0, 1).getRed());
    assertEquals(107, edited.getPixelAt(0, 1).getGreen());
    assertEquals(107, edited.getPixelAt(0, 1).getBlue());
    assertEquals(71, edited.getPixelAt(1, 0).getRed());
    assertEquals(71, edited.getPixelAt(1, 0).getGreen());
    assertEquals(71, edited.getPixelAt(1, 0).getBlue());
    assertEquals(221, edited.getPixelAt(1, 1).getRed());
    assertEquals(221, edited.getPixelAt(1, 1).getGreen());
    assertEquals(221, edited.getPixelAt(1, 1).getBlue());
  }

  @Test
  // tests the apply method to make sure that the values change as expected
  // (square image, different results)
  public void testSepiaApplySquareComplex() {
    IPixel topLeft = new BasicPixel(0, 0, 100, 125, 150);
    IPixel topRight = new BasicPixel(1, 0, 50, 75, 100);
    IPixel bottomLeft = new BasicPixel(0, 1, 150, 100, 50);
    IPixel bottomRight = new BasicPixel(1, 1, 200, 225, 250);
    IImage img = new BasicImage(2, 2,
            new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    IOperation sepia = new Sepia(img);
    IImage edited = sepia.apply();
    assertEquals(163, edited.getPixelAt(0, 0).getRed());
    assertEquals(145, edited.getPixelAt(0, 0).getGreen());
    assertEquals(113, edited.getPixelAt(0, 0).getBlue());
    assertEquals(145, edited.getPixelAt(0, 1).getRed());
    assertEquals(129, edited.getPixelAt(0, 1).getGreen());
    assertEquals(100, edited.getPixelAt(0, 1).getBlue());
    assertEquals(96, edited.getPixelAt(1, 0).getRed());
    assertEquals(85, edited.getPixelAt(1, 0).getGreen());
    assertEquals(66, edited.getPixelAt(1, 0).getBlue());
    assertEquals(255, edited.getPixelAt(1, 1).getRed());
    assertEquals(255, edited.getPixelAt(1, 1).getGreen());
    assertEquals(207, edited.getPixelAt(1, 1).getBlue());
  }
}
