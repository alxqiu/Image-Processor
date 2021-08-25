package model.image.patterns;

import model.image.IImage;

/**
 * Interface defining behavior needed to create an IImage. Allows any place where we use this
 * function object to be decoupled from a specific IImage implementation.
 */
public interface PatternCreator {

  /**
   * Depending on how the PatternCreator implementation is created, creates an IImage.
   *
   * @return IImage created by function object.
   */
  IImage create();
}
