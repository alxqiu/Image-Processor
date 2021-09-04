package model;

import model.image.IImage;
import model.image.IPixel;
import model.image.patterns.PatternCreator;
import model.operations.IOperationAdapterImpl;
import model.operations.OperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that stores a history of processes to be applied to any image in the history.
 * Has observer methods for model state, a method to add any IImage, the abilities to apply
 * an IOperation and create images from patterns using the respective function objects.
 *
 * <p>CHANGE in HW6: removed file i/o methods entirely to have controller deal with, as controller
 * should have sufficient information about IImages in the model thanks to observer methods to
 * export, and has the ability to add any IImage to the model when it has finished importing on
 * the controller's end.</p>
 */
public class BasicProcessorModel implements IProcessorModel {
  // NOTE: all images returned and received by this model will be shallow copies,
  // so that no image or pixel references can be mutated from the outside.
  private final List<IImage> images;

  /**
   * Creates new PpmProcessorModel with empty set of images.
   */
  public BasicProcessorModel() {
    images = new ArrayList<>();
  }

  @Override
  public void addImage(IImage toAdd) throws IllegalArgumentException {
    if (toAdd == null) {
      throw new IllegalArgumentException("given null arg");
    }
    // defensive copy...
    this.images.add(defensiveCopyGenerator(toAdd));
  }

  @Override
  public IImage getImageAt(int i) throws IllegalArgumentException {
    if (i < 0 || i > this.images.size() - 1) {
      throw new IllegalArgumentException("image index out of bounds");
    }
    return defensiveCopyGenerator(this.images.get(i));
  }

  /**
   * Helper method that returns a shallow copy of array of pixels that
   * constitute the given IImage. To avoid outside mutation.
   *
   * @param imageReference the Image to pull pixels from.
   * @return array of all pixels in the given IImage.
   * @throws IllegalArgumentException if given null instead.
   */
  private IPixel[] pixelsFromImage(IImage imageReference)
          throws IllegalArgumentException {
    if (imageReference == null) {
      throw new IllegalArgumentException("given null arg");
    }
    IPixel[] pixelsCopy = new IPixel[imageReference.getWidth() * imageReference.getHeight()];
    int pixelsIter = 0;
    // returns defensive copy....
    for (int j = 0; j < imageReference.getHeight(); j++) {
      for (int k = 0; k < imageReference.getWidth(); k++) {
        IPixel pixelRef = imageReference.getPixelAt(k, j);
        pixelsCopy[pixelsIter] = pixelRef.createPixel(k, j, pixelRef.getRed(),
                pixelRef.getGreen(), pixelRef.getBlue());
        pixelsIter++;
      }
    }
    return pixelsCopy;
  }

  /**
   * Helper method that returns a defensive copy of the given IImage.
   *
   * @param reference an IImage that could be mutated from outside.
   * @return an IImage copy in the same class implementation as the given IImage.
   * @throws IllegalArgumentException if given null.
   */
  private IImage defensiveCopyGenerator(IImage reference)
          throws IllegalArgumentException {
    if (reference == null) {
      throw new IllegalArgumentException("given null instead of IIMage");
    }
    return reference.createImage(reference.getWidth(),
            reference.getHeight(), pixelsFromImage(reference));
  }

  @Override
  public IImage removeAt(int i) throws IllegalArgumentException {
    if (i < 0 || i > this.images.size() - 1) {
      throw new IllegalArgumentException("index out of bounds");
    }
    // may be a bit overkill to do this, but it feels right to do
    return defensiveCopyGenerator(this.images.remove(i));
  }

  @Override
  public int numImages() {
    return this.images.size();
  }

  @Override
  public void applyIOperation(OperationType toPerform, int index) throws IllegalArgumentException {
    if (toPerform == null) {
      throw new IllegalArgumentException("given null for toApply");
    }

    if (index < 0 || index > this.numImages() - 1) {
      throw new IllegalArgumentException("index out of bounds");
    }

    // IOperation implementation could possibly mutate the IImage ref
    // they pass us from the outside, so we need a defensive copy....
    this.images.add(defensiveCopyGenerator(
            new IOperationAdapterImpl().adaptOperation(toPerform,
                    this.images.get(index))));
  }


  @Override
  public void addFromPattern(PatternCreator fromPattern) throws IllegalArgumentException {
    if (fromPattern == null) {
      throw new IllegalArgumentException("given null instead of fromPattern");
    }
    this.images.add(defensiveCopyGenerator(fromPattern.create()));
  }
}
