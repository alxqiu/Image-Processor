package model.operations.filter;

import model.image.IImage;
import model.image.IPixel;
import model.operations.AbstractOperation;


/**
 * Abstracts the Filter classes (blur and sharpen for now).
 */
public abstract class AbstractFilter extends AbstractOperation {
  // this represents the kernel, and may be of a specific size depending on the
  // model.operations.filter subclass. Must be a square 2D matrix of odd width/height.

  // INVARIANT: we can't have the constructor for AbstractFilter enforce this odd width/height
  // square kernel restraint, so we make pixelApply throw IllegalStateException if the subclass
  // attempts to create a non-square or non-even length kernel, to enforce this invariant.
  protected double[][] kernel;

  /**
   * Constructs a new AbstractFilter object, called by child class constructors.
   *
   * @param img IImage to apply filter to.
   * @throws IllegalArgumentException if given null.
   */
  public AbstractFilter(IImage img) throws IllegalArgumentException {
    super(img);
  }


  @Override
  protected IPixel pixelApply(int i, int j) throws IllegalStateException {
    if ((this.kernel.length % 2 != 1) || (this.kernel.length != this.kernel[0].length)) {
      throw new IllegalStateException("cannot have kernel that is non-square "
              + "or has non-odd length/width");
    }
    if (i < 0 || i > this.img.getWidth() - 1 || j < 0 || j > this.img.getHeight() - 1) {
      throw new IllegalArgumentException("i or j is out of bounds");
    }
    double sumR = 0;
    double sumG = 0;
    double sumB = 0;
    int offset = this.kernel.length / 2;
    for (int r = -offset; r <= offset; r++) {
      for (int c = -offset; c <= offset; c++) {
        if (!(i + r < 0 || i + r >= img.getWidth()
                || j + c < 0 || j + c >= img.getHeight())) {
          IPixel kerPix = img.getPixelAt(i + r, j + c);
          sumR += kerPix.getRed() * this.kernel[r + offset][c + offset];
          sumG += kerPix.getGreen() * this.kernel[r + offset][c + offset];
          sumB += kerPix.getBlue() * this.kernel[r + offset][c + offset];
        }
      }
    }
    return img.getPixelAt(i, j).createPixel(i, j, sumR, sumG, sumB);
  }
}
