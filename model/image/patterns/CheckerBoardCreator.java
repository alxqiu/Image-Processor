package model.image.patterns;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;

/**
 * Function object that creates a checkerboard with black and white squares of a certain cellsize.
 * Implementation of PatternCreator that uses BasicImage/BasicPixel.
 */
public class CheckerBoardCreator implements PatternCreator {
  private final int w;
  private final int h;
  private final int cellSize;

  /**
   * Creates a new CheckerBoardCreator object.
   *
   * @param w        width
   * @param h        height
   * @param cellSize cell size
   * @throws IllegalArgumentException if any of the parameters are <=0
   */
  public CheckerBoardCreator(int w, int h, int cellSize)
          throws IllegalArgumentException {
    if (w <= 0 || h <= 0 || cellSize <= 0) {
      throw new IllegalArgumentException("Parameters cannot be <= 0.");
    } else {
      this.w = w;
      this.h = h;
      this.cellSize = cellSize;
    }
  }

  @Override
  public IImage create() {
    IPixel[] pixels = new IPixel[this.w * this.h];
    int ind = 0;
    for (int r = 0; r < this.h; r++) {
      for (int c = 0; c < this.w; c++) {
        if (c % this.cellSize < this.cellSize / 2
                && r % this.cellSize < this.cellSize / 2) {
          pixels[ind] = new BasicPixel(r, c, 255, 255, 255);
        } else if (c % this.cellSize < this.cellSize / 2
                && r % this.cellSize > this.cellSize / 2) {
          pixels[ind] = new BasicPixel(r, c, 0, 0, 0);
        } else if (c % this.cellSize >= this.cellSize / 2
                && r % this.cellSize >= this.cellSize / 2) {
          pixels[ind] = new BasicPixel(r, c, 255, 255, 255);
        } else {
          pixels[ind] = new BasicPixel(r, c, 0, 0, 0);
        }
        ind++;
      }
    }
    return new BasicImage(this.w, this.h, pixels);
  }
}
