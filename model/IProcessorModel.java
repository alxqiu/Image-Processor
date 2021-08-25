package model;

import model.image.IImage;
import model.image.patterns.PatternCreator;
import model.operations.OperationType;

/**
 * Interface that defines behavior that a model of an Image processing program should possess,
 * using interface type IImage to represent image objects. Retrieves images, and creates new
 * images from patterns, and stores them in the processed files history. Uses function objects
 * as arguments to fulfill all of these tasks, so that this interface is only coupled to IImage,
 * PatternCreator, and interfaces, and not to any one implementation of those interfaces.
 *
 * <p>NOTE: change during HW6: removed the i/o methods, the observer and addImage methods
 * should be enough for the controller to handle the tasks of file I/O while still making
 * relevant changes to the model. </p>
 */
public interface IProcessorModel extends ViewModelState {

  /**
   * Method that simply adds the given IImage to the history of images processed.
   *
   * @throws IllegalArgumentException if given image is null.
   */
  void addImage(IImage toAdd) throws IllegalArgumentException;

  /**
   * Method that removes an IImage from the history of edited/imported IImages
   * from the given index, and returns a reference to it.
   *
   * @param i an integer index to remove from. Should be greater than or equal to 0.
   * @return a reference to the IImage that was removed.
   * @throws IllegalArgumentException if given an index greater than number of IImages - 1
   *                                  or less than 0.
   */
  IImage removeAt(int i) throws IllegalArgumentException;

  /**
   * HW6 DESIGN CHANGE: changed this from taking in an IOperation to using an enum to designate
   * operation type to perform, and an index to designate which IImage to apply to.
   *
   * <p>Method that adds a new IImage to the history of IImages in the IProcessorModel
   * implementation class that is the result of having the given IOperation applied.
   * The IOperation will be applied to the IImage at the given index. </p>
   *
   * @param toPerform enum designation for IOperation to apply.
   * @param index IImage to apply the operation on.
   * @throws IllegalArgumentException if given null in instead of OperationType.
   */
  void applyIOperation(OperationType toPerform, int index) throws IllegalArgumentException;


  /**
   * Adds the IImage associated with the given PatternCreator object to the history of IImages
   * processed.
   *
   * @param fromPattern a PatternCreator object like CheckerBoardCreator.
   * @throws IllegalArgumentException if given null
   */
  void addFromPattern(PatternCreator fromPattern) throws IllegalArgumentException;
}