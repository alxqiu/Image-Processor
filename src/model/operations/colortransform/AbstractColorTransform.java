package model.operations.colortransform;

import model.image.IImage;
import model.image.IPixel;
import model.operations.AbstractOperation;

/**
 * Abstract class that extends AbstractOperation to specialize in color transformations.
 */
public abstract class AbstractColorTransform extends AbstractOperation {
  // note: this is final, unlike the kernel matrix, which may not be a fixed size.
  // According to piazza @1507, color matrices MUST be 3x3.
  protected final double[][] colorMatrix;

  /**
   * Creates a new AbstractColorTransform object, for child color transformation objects
   * to call in their own constructors.
   *
   * @param img IImage to apply color transformation to.
   * @throws IllegalArgumentException if img is null.
   */
  public AbstractColorTransform(IImage img) throws IllegalArgumentException {
    super(img);
    colorMatrix = new double[3][3];

    // INVARIANT: all subclasses of AbstractColorTransform will possess a 3x3 colorMatrix,
    // also enforced in pixelApply to not have subclasses re-assign colorMatrix to a new
    // matrix. Enforced with the colorMatrix being final....
  }

  @Override
  protected IPixel pixelApply(int i, int j)
          throws IllegalArgumentException, IllegalStateException {
    if (this.colorMatrix.length != 3 || this.colorMatrix[0].length != 3) {
      throw new IllegalStateException("colorMatrix must be 3x3");
    }
    if (i < 0 || i > this.img.getWidth() - 1 || j < 0 || j > this.img.getHeight() - 1) {
      throw new IllegalArgumentException("i or j is out of bounds");
    }

    IPixel basePixel = this.img.getPixelAt(i, j);

    double newR = 0.0;
    double newG = 0.0;
    double newB = 0.0;

    for (int k = 0; k < 3; k++) {
      double currColor = this.colorMatrix[k][0] * basePixel.getRed()
              + this.colorMatrix[k][1] * basePixel.getGreen()
              + this.colorMatrix[k][2] * basePixel.getBlue();
      switch (k) {
        case 0:
          newR = currColor;
          break;
        case 1:
          newG = currColor;
          break;
        case 2:
          newB = currColor;
          break;
        default:
          // should not get here.
      }
    }

    return basePixel.createPixel(i, j, newR, newG, newB);
  }
}
