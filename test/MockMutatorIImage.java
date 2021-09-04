import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;

/**
 * Mock class that contains a field and has its IImage methods mutate that field.
 * Tells us if the Model methods can successfully perform without fear of outside mutation.
 */
public class MockMutatorIImage implements IImage {
  private boolean hasBeenMutated;

  /**
   * Creates a new MockMutatorIImage object, sets hasBeenMutated to false.
   */
  public MockMutatorIImage() {
    hasBeenMutated = false;
  }


  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    // this is the "observer" that tells us if this has been mutated.
    if (hasBeenMutated) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public IPixel getPixelAt(int x, int y) throws IllegalArgumentException {
    return new BasicPixel(0, 0, 0, 0,0);
  }

  @Override
  public IImage createImage(int w, int h, IPixel[] pixels) throws IllegalArgumentException {
    return new MockMutatorIImage();
  }

  /**
   * mutates the mock class.
   */
  public void mutate() {
    hasBeenMutated = true;
  }
}
