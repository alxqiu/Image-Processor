package model.image;

/**
 * Interface for pixels that detail access to RGB values and position within an model.image.
 */
public interface IPixel {

  /**
   * Observer method for the red color value of this model.image.IPixel.
   *
   * @return int between 0 and 255, inclusive.
   */
  int getRed();

  /**
   * Observer method for the green color value of this model.image.IPixel.
   *
   * @return int between 0 and 255, inclusive.
   */
  int getGreen();

  /**
   * Observer method for the blue color value of this model.image.IPixel.
   *
   * @return int between 0 and 255, inclusive.
   */
  int getBlue();

  /**
   * Observer method for x position of this model.image.IPixel within its model.image.
   * Starting with 0 being the left-most x position in an model.image.
   *
   * @return int that is at least 0.
   */
  int getX();

  /**
   * Observer method for y position of this model.image.IPixel within its model.image.
   * Starting with 0 being the upper-most y position in an model.image.
   *
   * @return int that is at least 0.
   */
  int getY();

  /**
   * Creates a pixel of the implementation type that this method can be called from.
   *
   * @param x an integer to represent the x-position
   * @param y an integer to represent the y-position
   * @param r a double to represent the red channel value
   * @param g a double to represent the green channel value
   * @param b a double to represent the blue channel value
   * @return a completed model.image.IPixel of the implementation's type.
   * @throws IllegalArgumentException if x or y are <0
   */
  IPixel createPixel(int x, int y, double r, double g, double b) throws IllegalArgumentException;
}
