package model.operations;

import model.image.IImage;

/**
 * An interface to represent operations done on an IImage.
 */
public interface IOperation {
  /**
   * Applies the specified model.operations.filter to the IImage this IOperation was
   * constructed with.
   *
   * @return the edited model.image in the same implementation as the IImage field
   *            of the IOperation implementation.
   */
  IImage apply();
}
