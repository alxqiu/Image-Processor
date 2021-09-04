import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;

/**
 * Mock class that extends BasicImage, just to show that the model can handle multiple IImage
 * types.
 */
public class MockTinyIImage implements IImage {

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public IPixel getPixelAt(int x, int y) throws IllegalArgumentException {
    return new BasicPixel(0, 0, 255, 255, 255);
  }

  @Override
  public IImage createImage(int w, int h, IPixel[] pixels) throws IllegalArgumentException {
    return new MockTinyIImage();
  }
}
