package model;

import java.util.ArrayList;

import model.image.IImage;
import model.image.patterns.PatternCreator;
import model.operations.OperationType;

/**
 * Interface that represents a layered set of IImages. Contains observer and mutator
 * methods that allow a user to interact with these sets of IImages like individual layers.
 * .removeAt() and addImage() are still useable and inherited from IProcessorModel to allow
 * for new layers, while these new methods contains means to check and tweak visiblity states,
 * and identify which is the "current layer". Behavioral changes to overriden methods addImage(),
 * removeAt(), applyIOperation(), and addFromPattern() are documented here.
 *
 * <p>
 * Change in HW7: added ability to interact with layer names. The layers are stored
 * in a map of integer keys and string values, with the integer values representing
 * indices of the respective layer.
 * </p>
 */
public interface ILayeredModel extends LayeredModelState, IProcessorModel {

  /**
   * Mutator method that allows renaming of each indexed layer. If name is
   * already present, the name put will be name + "-copy".
   *
   * @param idx  if index does not exist in key mappings.
   * @param name new name to rename given layer index to.
   * @throws IllegalArgumentException if index doesn't exist.
   */
  void renameLayerAt(int idx, String name) throws IllegalArgumentException;

  /**s
   * Sets the given layer as "invisible". Does nothing if index is already invisible.
   *
   * @param index the layer to perform this on.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  void makeLayerInvisible(int index) throws IllegalArgumentException;

  /**
   * An observer method that gets the list of invisible image indices.
   *
   * @return the list of invisible images' indices
   */
  @Override
  ArrayList<Integer> getInvisibleLayers();

  /**
   * Sets the given layer as "visible". Does nothing if index is already visible.
   *
   * @param index the layer to perform this on.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  void makeLayerVisible(int index) throws IllegalArgumentException;

  /**
   * An observer method to identify if the given index represents an
   * invisible layer.
   *
   * @param index the layer to perform this on.
   * @return true if layer is invisible, false otherwise.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  @Override
  boolean isLayerInvisible(int index) throws IllegalArgumentException;

  /**
   * Observer method to examine which layer is currently being modified.
   *
   * @return -1 if there are no layers, and the index of the current layer otherwise.
   */
  @Override
  int getCurrentLayer();

  /**
   * Setter that allows users to change the layer that holds "current" status.
   *
   * @param i the layer to perform this on.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  void setCurrentLayer(int i) throws IllegalArgumentException;

  /**
   * Adds a blank layer to the top level of this model that is white. Increases max index of
   * layers by 1. If no layers exist, then a 100x100 IImage is created. Mutates the added
   * layer's map name from placeholder to "new blank", by using renameLayerAt, which will
   * add on as many "-copy" strings to the name to keep it unique.
   */
  void addBlankLayer();

  /**
   * Inherited method that simply adds the given IImage to the history of images processed.
   * This time, the ILayeredModel restricts IImages to be added to be within the same dimensions
   * of the previous layer, if there are existing layers in the model. NEW IN HW7: adds
   * placeholder name to names map "placeholder n", where n represents the current layer
   * (most recently added). Also mutates the current layer such that it will be the image
   * most recently added.
   *
   * @throws IllegalArgumentException if given image is null or not of the same size of any
   *                                  existing layers if any.
   */
  @Override
  void addImage(IImage toAdd) throws IllegalArgumentException;

  /**
   * Method that removes an IImage from the history of edited/imported IImages
   * from the given index, and returns a reference to it. SPECIFIC TO THIS INTERFACE:
   * this now removes the specified layer from collection of invisible layers and adjusts
   * existing register of invisible layers accordingly to match.
   *
   * @param i an integer index to remove from. Should be greater than or equal to 0.
   * @return a reference to the IImage that was removed.
   * @throws IllegalArgumentException if given an index greater than number of IImages - 1
   *                                  or less than 0.
   */
  @Override
  IImage removeAt(int i) throws IllegalArgumentException;

  /**
   * HW6 DESIGN CHANGE: changed this from taking in an IOperation to using an enum to designate
   * operation type to perform, and an index to designate which IImage to apply to.
   *
   * <p> SPECIFIC to this interface: this now replaces the IImage (layer) at the specific index
   * with the specific IOperation applied to it. Any IImage this is done on retains visiblity
   * status. Retains the same signature as the IProcessorModel method, but now throws an
   * IllegalArgumentException if the given index doesn't match the index for the "current layer".
   * </p>
   *
   * @param toPerform enum designation for IOperation to apply.
   * @param index     IImage index to apply the operation on.
   * @throws IllegalArgumentException if given null in instead of OperationType, if index is
   *                                  out of bounds, or index is not current layer.
   */
  @Override
  void applyIOperation(OperationType toPerform, int index) throws IllegalArgumentException;

  /**
   * Adds the IImage associated with the given PatternCreator object to the history of IImages
   * processed. SPECIFIC to this interface: ensures that the IImage creates from the given object
   * is the same size as previous layer.
   *
   * @param fromPattern a PatternCreator object like CheckerBoardCreator.
   * @throws IllegalArgumentException if given null or created IImage is not the same size
   *                                  as a past layer, unless there are no layers.
   */
  @Override
  void addFromPattern(PatternCreator fromPattern) throws IllegalArgumentException;
}
