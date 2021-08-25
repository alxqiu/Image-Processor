package model.operations.colortransform;

import model.image.IImage;

/**
 * A type of color transformation that shifts every RGB value to the same in each pixel.
 */
public class Greyscale extends AbstractColorTransform {
  /**
   * Creates a new Greyscale color transformation function object.
   *
   * @param img IImage to apply Greyscale color transformation to.
   * @throws IllegalArgumentException if given null.
   */
  public Greyscale(IImage img) throws IllegalArgumentException {
    super(img);
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        switch (j) {
          case 0:
            this.colorMatrix[i][j] = 0.2126;
            break;
          case 1:
            this.colorMatrix[i][j] = 0.7152;
            break;
          case 2:
            this.colorMatrix[i][j] = 0.0722;
            break;
          default:
            // no action needed, colorMatrix cannot have be anything but 3x3.
        }
      }
    }
  }
}
