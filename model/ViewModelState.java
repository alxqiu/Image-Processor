package model;

import model.image.IImage;

/**
 * Holds all the getters for the state of the model shared by both Models.
 * which will be implemented by models that use these methods to return immutable objects to
 * observe the state of the model.
 */
public interface ViewModelState {
  /**
   * Observer method to access an image, with the first index of 0 representing the first
   * image added to this model.
   *
   * @param i index of image starting at 0.
   * @return object representing image.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  IImage getImageAt(int i) throws IllegalArgumentException;

  /**
   * Observer method that returns the number of IImages that are in the history of this
   * IProcessorModel.
   *
   * @return an integer, should be at least 0.
   */
  int numImages();
}
