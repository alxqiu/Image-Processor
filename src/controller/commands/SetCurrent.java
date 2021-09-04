package controller.commands;

import model.ILayeredModel;

/**
 * Command object that mutates the model by giving it a new index to be its
 * current layer to observe. The mutator method will throw an exception which
 * will be caught by .go()'s caller, and inform the user of the model rule violated.
 */
public class SetCurrent implements IImageCommand {

  @Override
  public String goCommand(ILayeredModel model, String toTraverse) {
    // can reliably assume the given string is at least "set current ...."
    int newIndex = Integer.parseInt(toTraverse.substring(12));
    model.setCurrentLayer(newIndex - 1);
    return "set new current layer to #" + newIndex;
  }
}
