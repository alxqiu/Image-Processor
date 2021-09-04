import static org.junit.Assert.assertEquals;


import model.IProcessorModel;
import model.ProcessorModelCreator;
import model.image.BasicPixel;
import model.image.IImage;
import model.operations.filter.BlurFilter;
import org.junit.Before;
import org.junit.Test;
import model.image.patterns.CheckerBoardCreator;
import model.image.patterns.PatternCreator;

/**
 * A tester class for the model.view.patters.PatternCreator interface.
 */
public class ImageCreatorTest {


  @Before
  public void init() {
    IProcessorModel newModel = new ProcessorModelCreator().create(
            ProcessorModelCreator.ProcessorType.BASIC);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the Checkerboard Creator class to make sure that the exceptions for the invalid
  // parameters are thrown
  public void testBadConstructor1() {
    PatternCreator img = new CheckerBoardCreator(-1, 20, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the Checkerboard Creator class to make sure that the exceptions for the invalid
  // parameters are thrown
  public void testBadConstructor2() {
    PatternCreator img = new CheckerBoardCreator(20, -1, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the Checkerboard Creator class to make sure that the exceptions for the invalid
  // parameters are thrown
  public void testBadConstructor3() {
    PatternCreator img = new CheckerBoardCreator(20, 20, -1);
  }

  @Test
  // testing to create a generic checkerboard IImage, checking if it upholds all properties
  // of an IImage
  public void testCreateChecker() {
    IImage img = new CheckerBoardCreator(400, 400, 20).create();
    assertEquals(img.getHeight(), 400);
    assertEquals(img.getWidth(), 400);
    assertEquals(img.getPixelAt(0, 0), new BasicPixel(0, 0, 255, 255,255));
  }

  @Test
  // tests to make sure that the pixel values actually match the expected
  // (checkerboard creator , sharpen)
  public void testBlurApplyChecker() {
    IImage img = new CheckerBoardCreator(200, 200, 20).create();
    for (int i = 0; i < 10; i++) {
      img = new BlurFilter(img).apply();
    }
    IImage out = new CheckerBoardCreator(200, 200, 20).create();

    for (int j = 0; j < 10; j++) {
      out = new BlurFilter(out).apply();
    }

    assertEquals(img.getWidth(), out.getWidth());
    assertEquals(img.getHeight(), out.getHeight());
    for (int r = 0; r < img.getHeight(); r++) {
      for (int c = 0; c < img.getWidth(); c++) {
        assertEquals(img.getPixelAt(r, c), out.getPixelAt(r, c));
      }
    }
  }
}
