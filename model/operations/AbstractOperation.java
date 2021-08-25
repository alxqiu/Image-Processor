package model.operations;

import model.image.IImage;
import model.image.IPixel;

/**
 * Highest level of operation hierarchy under IOperation interface. Child classes are
 * AbstractColorTransform and AbstractFilter, both of which have their own child
 * filter/transform classes.
 */
public abstract class AbstractOperation implements IOperation {
  protected final IImage img;

  /**
   * Constructor for all AbstractOperations, sets the IImage to be operated on.
   *
   * @param img IImage to take in and apply an effect to.
   * @throws IllegalArgumentException if given null.
   */
  public AbstractOperation(IImage img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("img cannot be null");
    }
    this.img = img;
  }

  @Override
  public IImage apply() {
    IPixel[] pixels = new IPixel[img.getHeight() * img.getWidth()];
    int pixelsIter = 0;
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        pixels[pixelsIter] = this.pixelApply(j, i);
        pixelsIter++;
      }
    }
    return img.createImage(img.getWidth(), img.getHeight(), pixels);
  }

  /**
   * Applies the effect dictated by the kernel to a single pixel of the field img
   * model.image.IImage. Able to be used for any kernel size, assuming all kernels possess
   * equal and odd width and height. Also is able to apply any pixel color transformation
   * from the Abstract ColorTransform subclasses.
   *
   * @param i x coordinate of the pixel to apply the RGB value shift to.
   * @param j y coordinate of the pixel to apply the RGB value shift to.
   * @return a new model.image.IPixel object of the implementation existing within the IImage
   *              in this AbstractFilter.
   * @throws IllegalArgumentException if i or j are out of bounds.
   * @throws IllegalStateException    if matrix of subclass does not follow the invariants of
   *                                  AbstractColorTransform or AbstractFilter.
   */
  protected abstract IPixel pixelApply(int i, int j)
          throws IllegalArgumentException, IllegalStateException;
}
