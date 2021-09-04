package view;

import controller.importexport.DialogType;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface defining behavior for code that can mutate and interact with
 * a view of the IImage processing program that uses Swing to render a GUI.
 * Preferably the implementation has access to a JFrame or extends it.
 */
public interface ISwingView extends IImageView {
  /**
   * Sets the given listener object to all components that this view
   * has access to.
   *
   * @param listener an ActionListener object that will perform an action
   *                 depending on the action received.
   */
  void setListener(ActionListener listener);

  /**
   * Starts the view, or makes the GUI visible. Normally would put this
   * in the constructor of the view, but we wanted the controller to dictate
   * when the GUI is visible.
   */
  void startViewing();


  /**
   * Adjusts the state of the rendered visibility checkbox and other
   * means of adjusting visibilty state.
   * @param invisIndices indices that are invisible.
   * @param currLayer index of current layer.
   */
  void adjustVisibilityState(List<Integer> invisIndices, int currLayer);


  /**
   * Gives integers representing the number of layers and the current layer
   * to be viewed, so that the components of this view can be rendered properly.
   * NOTE: doesn't enforce whether these numbers are representative of the model, as
   * that is not the job of the view, rather they only enforce that these numbers make
   * sense. Updates the layer representation with the appropriate names at the given
   * indices in the map.
   *
   * @param numImages the number of layers to render the view with.
   * @param currLayer the index of the layer to be currently viewing, starting from 0.
   * @param indexNames index layers mapped to string names.
   * @throws IllegalArgumentException if numImages is less than 0 or
   *                                  currLayer is higher than the highest
   *                                  possible index (numImages - 1).
   */
  void adjustLayeredState(int numImages, int currLayer, Map<Integer, String> indexNames)
          throws IllegalArgumentException;

  /**
   * Method that allows the chooser object to be displayed in the JFrame
   * of this implementation. The controller or outside object should be able to
   * pass the enum type in for the view to render. Returns the file chosen by
   * the user.
   *
   * @param d type of dialog, either OPEN or SAVE.
   * @param extensions file extensions to look for, lowercased and without periods.
   * @return selected file, if any, otherwise null.
   * @throws IllegalArgumentException if given null.
   */
  File openDialogBox(DialogType d, String... extensions) throws IllegalArgumentException;
}
