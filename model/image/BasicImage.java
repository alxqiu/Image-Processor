package model.image;

/**
 * Image representation using a 2D array of model.image.IPixel objects to represent a collection
 * of IPixels that have a specific height, width, and can access each individual IPixel
 * given coordinates.
 */
public class BasicImage implements IImage {
  // INVARIANT: no repetitions in pixels, and no pixel is out of bounds, and the number of of
  // pixels forms a width x height rectangle exactly. This also means that every pixel has a
  // unique position inside this IImage, and fully constitutes the IImage.
  // This array of pixels is at least 1x1.
  private final IPixel[][] pixels;

  /**
   * Constructs a new model.image.BasicImage object out of width and height in pixels and a
   * Array of IPixels to populate the model.image with.
   *
   * @param width      width of model.image in pixels.
   * @param height     height of model.image in pixels.
   * @param fromPixels some list of model.image.IPixel, with no pixels that share the same
   *                   position, and each x and y value is between 0 and the width and height
   *                   of this model.image.BasicImage, respectively.
   * @throws IllegalArgumentException if given width or height less than 1 or a list of pixels
   *                                  that doesn't have exactly width x height elements. Or if
   *                                  any pixel in the array has a position lower than 0 or
   *                                  higher than the width/height, or is repeated. Or if given
   *                                  null instead of array of model.image.IPixel.
   */
  public BasicImage(int width, int height, IPixel[] fromPixels)
          throws IllegalArgumentException {
    if (width < 1 || height < 1) {
      throw new IllegalArgumentException("width and height cannot be less than 1 pixel");
    }
    if (fromPixels == null) {
      throw new IllegalArgumentException("fromPixels cannot be null");
    }
    if (fromPixels.length != width * height) {
      throw new IllegalArgumentException("model.image cannot be created from number of "
              + "pixels that doesn't match width x height");
    }
    // perhaps iterate through each pixel while making it out of the
    // fromPixels to see if no pixel has an out-of-bounds position.

    pixels = new IPixel[height][width];
    for (IPixel p : fromPixels) {
      if (p.getX() < 0 || p.getX() > width - 1
              || p.getY() < 0 || p.getY() > height - 1) {
        throw new IllegalArgumentException("pixel position out of bounds");
      }
      if (pixels[p.getY()][p.getX()] != null) {
        throw new IllegalArgumentException("pixel at (" + p.getX() + ", "
                + p.getY() + ") already occupied");
      }
      pixels[p.getY()][p.getX()] = p;
    }
  }

  @Override
  public int getWidth() {
    return pixels[0].length;
  }

  @Override
  public int getHeight() {
    return pixels.length;
  }

  @Override
  public IPixel getPixelAt(int x, int y) throws IllegalArgumentException {
    if (x < 0 || y < 0 || x > this.getWidth() - 1 || y > this.getHeight() - 1) {
      throw new IllegalArgumentException("x or y cannot be less than 0 or greater "
              + "than width or height - 1, respectively");
    }
    // array lookup is O(1)
    IPixel pixelRef = pixels[y][x];

    // defensive copy just in case IPixel implementation is mutable.
    return pixelRef.createPixel(pixelRef.getX(), pixelRef.getY(),
            pixelRef.getRed(), pixelRef.getGreen(), pixelRef.getBlue());
  }

  @Override
  public IImage createImage(int w, int h, IPixel[] pixels)
          throws IllegalArgumentException {
    return new BasicImage(w, h, pixels);
  }
}
