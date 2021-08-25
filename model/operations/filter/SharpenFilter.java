package model.operations.filter;

import model.image.IImage;

/**
 * Subclass of model.operations.filter.AbstractFilter that will apply a Sharpening effect on an
 * model.image.IImage, using a 5x5 kernel that emphasizes edges between regions of constrast.
 */
public class SharpenFilter extends AbstractFilter {

  /**
   * Will construct a new model.operations.filter.SharpenFilter object with a 5x5 with the values:
   * [[-1/8, -1/8, -1/8, -1/8, -1/8],
   * [-1/8, 1/4, 1/4, 1/4, -1/8],
   * [-1/8, 1/4, 1, 1/4, -1/8],
   * [-1/8, 1/4, 1/4, 1/4, -1/8],
   * [-1/8, -1/8, -1/8, -1/8, -1/8]].
   *
   * @param img IImage to apply Sharpen effect to.
   * @throws IllegalArgumentException if given null.
   */
  public SharpenFilter(IImage img) throws IllegalArgumentException {
    super(img);
    this.kernel = new double[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (i == 2 && j == 2) {
          kernel[i][j] = 1;
        } else if ((i <= 3 && i >= 1) && (j <= 3 && j >= 1)) {
          kernel[i][j] = 0.25;
        } else {
          kernel[i][j] = -0.125;
        }
      }
    }
  }
}
