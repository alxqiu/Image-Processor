package model.operations.filter;

import model.image.IImage;

/**
 * Applies the blurring effect on the model.image. Inherits apply() and
 * pixelApply from model.operations.filter.AbstractFilter.
 */
public class BlurFilter extends AbstractFilter {
  /**
   * Will construct a new model.operations.filter.BlurFilter object with a 3x3 kernel that has
   * 1/4 at the center, 1/8 on the edges, and 1/16 on the corners, like so:
   * [[1/16, 1/8, 1/16],
   * [1/8, 1/4, 1/8],
   * [1/16, 1/8, 1/16]].
   *
   * @param img IImage to apply Blur effect to.
   * @throws IllegalArgumentException if given null.
   */
  public BlurFilter(IImage img) throws IllegalArgumentException {
    super(img);
    this.kernel = new double[3][3];
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        if (r == 0 || r == 2) {
          if (c != 1) {
            this.kernel[r][c] = 0.0625;
          } else {
            this.kernel[r][c] = 0.125;
          }
        } else {
          if (c != 1) {
            this.kernel[r][c] = 0.125;
          } else {
            this.kernel[r][c] = 0.25;
          }
        }
      }
    }
  }
}

