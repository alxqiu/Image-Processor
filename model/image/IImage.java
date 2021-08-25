package model.image;

/**
 * Image interface that allow observers for width, height, and individual pixels represented
 * with model.image.IPixel.
 */
public interface IImage {

  /**
   * Observer method for width, should return the number of pixels wide the model.image will be.
   *
   * @return number of pixels wide this model.image is, an integer at least 0.
   */
  int getWidth();

  /**
   * Observer method for height, should return the number of pixels tall the model.image will be.
   *
   * @return number of pixels tall this model.image is, an integer at least 0.
   */
  int getHeight();

  /**
   * Observer method for a pixel type P at the given x and y coordinate.
   *
   * @param x x coordinate, must be at least 0
   * @param y y coordinate, must be at least 0
   * @return Pixel object of model.image.IPixel type
   * @throws IllegalArgumentException if given x or y coordinate less than 0 or out of
   *                                  bounds of model.image width or height.
   */
  IPixel getPixelAt(int x, int y) throws IllegalArgumentException;

  /**
   * Creates a new Image of the implementation type that this method can be called from.
   *
   * @param w      -- the width of the model.image
   * @param h      -- the height of the model.image
   * @param pixels -- the array of of pixels that make up the model.image
   * @return -- a completed model.image.IImage of the implementation's type
   * @throws IllegalArgumentException if the list given is null or empty or has any repeated pixel
   *                                  positions or has any pixels out of bounds, or if w or h is
   *                                  less than 1.
   */
  IImage createImage(int w, int h, IPixel[] pixels) throws IllegalArgumentException;
}
