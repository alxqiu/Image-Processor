package model.operations;

import model.image.IImage;

/**
 * Returns a new IImage object based on the given enum and
 * IImage. Applied with the given enum value and IImage.
 */
public interface IOperationAdapter {
  /**
   * Returns an IImage with the specified IOperation applied to the given IImage.
   *
   * @param toPerform enum type of Operation to apply.
   * @param toApplyTo IImage to apply Operation on.
   * @return an IImage result of the applied Operation.
   * @throws IllegalArgumentException if either argument is null.
   */
  IImage adaptOperation(OperationType toPerform, IImage toApplyTo)
          throws IllegalArgumentException;
}
