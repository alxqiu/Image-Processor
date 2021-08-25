package model.image;

import java.util.Objects;

/**
 * Immutable representation for a type of model.image.IPixel that holds x and y positions as
 * non-negative integers, and stores RGB values as integers between 0 and 255, inclusive.
 * Enforces clamping for those values.
 */
public class BasicPixel implements IPixel {

  // INVARIANT: each x and y coordinate is at least 0. This class doesn't enforce that x and y
  // are inside the bounds of an IImage, as that is the job of IImage implementations to enforce.
  private final int x;
  private final int y;

  // INVARIANT: these are 0 to 255, inclusive.
  private final int red;
  private final int green;
  private final int blue;

  /**
   * Constructs a new model.image.BasicPixel object from given integer arguments. This constructor
   * enforces the constraints of non-negative position x and y values, and RGB values between 0
   * and 255, inclusive. If any RGB value is given as greater than 255 or less than 0, then its
   * value will be clamped to 255 or 0, respectively.
   *
   * @param x     x position of the model.image.BasicPixel in the model.image.
   * @param y     y position of the model.image.BasicPixel in the model.image.
   * @param red   R channel value integer.
   * @param green G channel value integer.
   * @param blue  B channel value integer.
   * @throws IllegalArgumentException if any of the parameters are out of range.
   */
  public BasicPixel(int x, int y, int red, int green, int blue)
          throws IllegalArgumentException {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("cannot have negative position values");
    }
    if (red > 255) {
      red = 255;
    } else if (red < 0) {
      red = 0;
    }
    if (green > 255) {
      green = 255;
    } else if (green < 0) {
      green = 0;
    }
    if (blue > 255) {
      blue = 255;
    } else if (blue < 0) {
      blue = 0;
    }

    this.x = x;
    this.y = y;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  @Override
  public int getRed() {
    return this.red;
  }

  @Override
  public int getGreen() {
    return this.green;
  }

  @Override
  public int getBlue() {
    return this.blue;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public IPixel createPixel(int x, int y, double r, double g, double b)
          throws IllegalArgumentException {
    return new BasicPixel(x, y, (int) r, (int) g, (int) b);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof IPixel) {
      IPixel p = (IPixel) other;
      return this.x == p.getX()
              && this.y == p.getY()
              && this.red == p.getRed()
              && this.blue == p.getBlue()
              && this.green == p.getGreen()
              && this.hashCode() == p.hashCode();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.red, this.green, this.blue);
  }
}
