import model.IProcessorModel;
import model.ProcessorModelCreator;
import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import model.image.patterns.CheckerBoardCreator;
import model.operations.OperationType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class that tests all behavior and exceptions for the BasicProcessorModel implementation of
 * IProcessorModel.
 */
public class BasicProcessorModelTests {

  IProcessorModel testModel;

  @Before
  public void init() {
    testModel = ProcessorModelCreator.create(ProcessorModelCreator.ProcessorType.BASIC);
  }

  /*
  Testing no outside mutation
   */

  // testing that addImage won't suffer from outside mutation
  @Test
  public void testNoMutationAddImage() {
    MockMutatorIImage mockImage = new MockMutatorIImage();
    testModel.addImage(mockImage);
    // this one mutates the mockImage....
    mockImage.mutate();
    // if object was mutated, will return 1, otherwise will return 0.
    assertEquals(testModel.getImageAt(0).getHeight(), 0);
    assertEquals(mockImage.getHeight(), 1);
  }

  // testing that applyIOperation won't suffer from outside mutation
  @Test
  public void testNoMutationRemoveAt() {
    MockMutatorIImage mockImage = new MockMutatorIImage();
    testModel.addImage(mockImage);
    // this one mutates the mockImage....
    mockImage.mutate();
    // if object was mutated, will return 1, otherwise will return 0.
    assertEquals(testModel.removeAt(0).getHeight(), 0);
    assertEquals(mockImage.getHeight(), 1);
  }

  /*
  Testing core functionality
   */
  @Test
  // adding a single image, and then another
  public void testAddTwoImages() {
    assertEquals(testModel.numImages(), 0);
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 1, 2, 3)}));
    assertEquals(testModel.getImageAt(0).getPixelAt(0, 0),
        new BasicPixel(0, 0, 1, 2, 3));
    assertEquals(testModel.numImages(), 1);
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 1, 2, 3)}));
    assertEquals(testModel.numImages(), 2);
  }

  // adding images of multiple types, then checking their traits....
  @Test
  // adding a single image, and then another of MockTinyIImage
  public void testAddTwoImagesDiffTypes() {
    assertEquals(testModel.numImages(), 0);
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 1, 2, 3)}));
    assertEquals(testModel.getImageAt(0).getPixelAt(0, 0),
        new BasicPixel(0, 0, 1, 2, 3));
    assertEquals(testModel.numImages(), 1);

    // adding from MockTinyIImage type.
    testModel.addImage(new MockTinyIImage());
    assertEquals(testModel.numImages(), 2);
    assertEquals(testModel.getImageAt(1).getPixelAt(0, 0),
        new BasicPixel(0, 0, 255, 255, 255));
    assertEquals(testModel.getImageAt(1).getHeight(), 0);
    assertEquals(testModel.getImageAt(1).getWidth(), 0);
  }

  @Test
  // testing that getter returns the right images
  public void testGetTwoImages() {
    // testing we are accessing the first image
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 1, 2, 3)}));
    assertEquals(testModel.getImageAt(0).getPixelAt(0, 0),
        new BasicPixel(0, 0, 1, 2, 3));

    // testing that we are accessing the second image
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 4, 5, 6)}));
    assertEquals(testModel.getImageAt(1).getPixelAt(0, 0),
        new BasicPixel(0, 0, 4, 5, 6));
  }

  @Test
  // testing that removal method can do the trick....
  public void testRemoveTwoImages() {
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 1, 2, 3)}));
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 4, 5, 6)}));
    assertEquals(testModel.numImages(), 2);
    IImage removed = testModel.removeAt(1);
    // assert that there is one image remaining and it is the first one we added....
    assertEquals(testModel.numImages(), 1);
    // assert that the removed image is the right one....
    assertEquals(removed.getPixelAt(0, 0),
        new BasicPixel(0, 0, 4, 5, 6));
    assertEquals(testModel.getImageAt(0).getPixelAt(0, 0),
        new BasicPixel(0, 0, 1, 2, 3));
    testModel.removeAt(0);
    assertEquals(testModel.numImages(), 0);
  }

  @Test
  // testing we have the right number of images
  public void testNumImages() {
    assertEquals(testModel.numImages(), 0);
    testModel.addFromPattern(new CheckerBoardCreator(1, 1, 1));
    assertEquals(testModel.numImages(), 1);
    testModel.addFromPattern(new CheckerBoardCreator(1, 1, 1));
    assertEquals(testModel.numImages(), 2);
    testModel.addFromPattern(new CheckerBoardCreator(1, 1, 1));
    assertEquals(testModel.numImages(), 3);

    // remove from middle
    testModel.removeAt(1);
    assertEquals(testModel.numImages(), 2);
  }

  @Test
  // testing applyIOperation using Blur
  public void testApplyBlur() {
    IPixel topLeft = new BasicPixel(0, 0, 8, 8, 8);
    IPixel topRight = new BasicPixel(1, 0, 16, 16, 16);
    IPixel bottomLeft = new BasicPixel(0, 1, 24, 24, 24);
    IPixel bottomRight = new BasicPixel(1, 1, 32, 32, 32);
    IImage squareToCheck = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.addImage(squareToCheck);

    testModel.applyIOperation(OperationType.BLUR, 0);

    IImage edited = testModel.getImageAt(0);
    assertEquals(9, edited.getPixelAt(0, 0).getRed());
    assertEquals(9, edited.getPixelAt(0, 0).getGreen());
    assertEquals(9, edited.getPixelAt(0, 0).getBlue());
    assertEquals(12, edited.getPixelAt(0, 1).getRed());
    assertEquals(10, edited.getPixelAt(1, 0).getRed());
    assertEquals(13, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // testing applyIOperation using Sharpen
  public void testApplySharpen() {
    IPixel topLeft = new BasicPixel(0, 0, 8, 8, 8);
    IPixel topRight = new BasicPixel(1, 0, 16, 16, 16);
    IPixel bottomLeft = new BasicPixel(0, 1, 24, 24, 24);
    IPixel bottomRight = new BasicPixel(1, 1, 32, 32, 32);
    IImage squareToCheck = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.applyIOperation(OperationType.SHARPEN, 0);

    IImage edited = testModel.getImageAt(0);
    assertEquals(26, edited.getPixelAt(0, 0).getRed());
    assertEquals(26, edited.getPixelAt(0, 0).getGreen());
    assertEquals(26, edited.getPixelAt(0, 0).getBlue());
    assertEquals(38, edited.getPixelAt(0, 1).getRed());
    assertEquals(32, edited.getPixelAt(1, 0).getRed());
    assertEquals(44, edited.getPixelAt(1, 1).getRed());
  }

  @Test
  // testing applyIOperation using Sepia
  public void testApplySepia() {
    IPixel topLeft = new BasicPixel(0, 0, 100, 125, 150);
    IPixel topRight = new BasicPixel(1, 0, 50, 75, 100);
    IPixel bottomLeft = new BasicPixel(0, 1, 150, 100, 50);
    IPixel bottomRight = new BasicPixel(1, 1, 200, 225, 250);
    IImage squareToCheck = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.applyIOperation(OperationType.SEPIA, 0);

    IImage edited = testModel.getImageAt(0);
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

  @Test
  // testing applyIOperation using Greyscale
  public void testApplyGreyscale() {
    IPixel topLeft = new BasicPixel(0, 0, 100, 125, 150);
    IPixel topRight = new BasicPixel(1, 0, 50, 75, 100);
    IPixel bottomLeft = new BasicPixel(0, 1, 150, 100, 50);
    IPixel bottomRight = new BasicPixel(1, 1, 200, 225, 250);
    IImage squareToCheck = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.applyIOperation(OperationType.GREYSCALE, 0);
    IImage edited = testModel.getImageAt(0);
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
  // testing application of filter to existing image, and remembering original image...
  public void testApplyToExistingInModel() {
    IPixel topLeft = new BasicPixel(0, 0, 100, 125, 150);
    IPixel topRight = new BasicPixel(1, 0, 50, 75, 100);
    IPixel bottomLeft = new BasicPixel(0, 1, 150, 100, 50);
    IPixel bottomRight = new BasicPixel(1, 1, 200, 225, 250);
    IImage squareToCheck = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.addImage(squareToCheck);
    testModel.applyIOperation(OperationType.GREYSCALE, 0);

    // checking first image:
    IImage unedited = testModel.getImageAt(0);
    assertEquals(100, unedited.getPixelAt(0, 0).getRed());
    assertEquals(125, unedited.getPixelAt(0, 0).getGreen());
    assertEquals(150, unedited.getPixelAt(0, 0).getBlue());
    assertEquals(150, unedited.getPixelAt(0, 1).getRed());
    assertEquals(100, unedited.getPixelAt(0, 1).getGreen());
    assertEquals(50, unedited.getPixelAt(0, 1).getBlue());

    // checking second (edited) image:
    IImage edited = testModel.getImageAt(1);
    assertEquals(121, edited.getPixelAt(0, 0).getRed());
    assertEquals(121, edited.getPixelAt(0, 0).getGreen());
    assertEquals(121, edited.getPixelAt(0, 0).getBlue());
    assertEquals(107, edited.getPixelAt(0, 1).getRed());
    assertEquals(107, edited.getPixelAt(0, 1).getGreen());
    assertEquals(107, edited.getPixelAt(0, 1).getBlue());
  }

  @Test
  // tests that the addFromPattern method will get the right image
  public void testAddFromPattern() {
    assertEquals(testModel.numImages(), 0);
    testModel.addFromPattern(new CheckerBoardCreator(4, 4, 1));
    assertEquals(testModel.numImages(), 1);
    assertEquals(testModel.getImageAt(0).getWidth(), 4);
    assertEquals(testModel.getImageAt(0).getHeight(), 4);
    // check if right pixel is white.
    assertEquals(testModel.getImageAt(0).getPixelAt(0, 0),
        new BasicPixel(0, 0, 255, 255, 255));
  }

  @Test
  // testing having multiple images added in different ways...
  public void varietyPack() {
    assertEquals(testModel.numImages(), 0);

    // index 0: add from pattern
    testModel.addFromPattern(new CheckerBoardCreator(8, 8, 2));

    // index 1: add from addImage
    testModel.addImage(new BasicImage(1, 1,
        new IPixel[]{new BasicPixel(0, 0, 4, 5, 6)}));

    // index 2: applyIOperation
    IPixel topLeft = new BasicPixel(0, 0, 8, 8, 8);
    IPixel topRight = new BasicPixel(1, 0, 16, 16, 16);
    IPixel bottomLeft = new BasicPixel(0, 1, 24, 24, 24);
    IPixel bottomRight = new BasicPixel(1, 1, 32, 32, 32);
    IImage squareToBlur = new BasicImage(2, 2,
        new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});
    testModel.applyIOperation(OperationType.BLUR, 0);

    assertEquals(testModel.numImages(), 4);

    // test that index 0 has right image:
    assertEquals(testModel.getImageAt(0).getWidth(), 8);
    assertEquals(testModel.getImageAt(0).getHeight(), 8);

    // test that index 1 has right image:
    assertEquals(testModel.getImageAt(1).getWidth(), 1);
    assertEquals(testModel.getImageAt(1).getHeight(), 1);

    // test that applyIOperation yielded the right image:
    IImage edited = testModel.getImageAt(2);
    assertEquals(9, edited.getPixelAt(0, 0).getRed());
    assertEquals(9, edited.getPixelAt(0, 0).getGreen());
    assertEquals(9, edited.getPixelAt(0, 0).getBlue());
    assertEquals(12, edited.getPixelAt(0, 1).getRed());
    assertEquals(10, edited.getPixelAt(1, 0).getRed());
    assertEquals(13, edited.getPixelAt(1, 1).getRed());

    // test that importFrom gave us the right image:
    assertEquals(testModel.getImageAt(3).getWidth(), 4);
    assertEquals(testModel.getImageAt(3).getHeight(), 4);
  }

  /*
  Testing Exceptions for all methods....
   */

  @Test(expected = IllegalArgumentException.class)
  // tests that the mutator will throw exception if given null
  public void testAddImageNull() {
    testModel.addImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the getter will throw exception if out of bounds (no images added so far)
  public void testGetImageAtNoImages() {
    testModel.getImageAt(0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the getter will throw exception if out of bounds (one above images present)
  public void testGetImageAtTwoImages() {
    testModel.addFromPattern(new CheckerBoardCreator(20, 20, 4));
    testModel.addFromPattern(new CheckerBoardCreator(30, 30, 3));
    testModel.getImageAt(2);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the remover will throw exception if out of bounds (no images added so far)
  public void testRemoveAtNoImages() {
    testModel.removeAt(0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the remover will throw exception if out of bounds (one above images present)
  public void testRemoveAtTwoImages() {
    testModel.addFromPattern(new CheckerBoardCreator(20, 20, 4));
    testModel.addFromPattern(new CheckerBoardCreator(30, 30, 3));
    testModel.removeAt(2);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the applyIOperation will throw exception if given null
  public void testApplyGivenNull() {
    testModel.applyIOperation(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  // tests that the model will throw exception if addFromPattern is given null
  public void testAddFromPatternGivenNull() {
    testModel.addFromPattern(null);
  }
}
