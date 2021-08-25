package controller.commands.layercommands;

import controller.commands.IImageCommand;
import model.ILayeredModel;

/**
 * Commands that can only be executed when there is a "current layer". These will fail
 * if there are no layers in the model when "go" is called
 */
public abstract class AbstractLayerCommand implements IImageCommand {
  @Override
  public String goCommand(ILayeredModel model, String toTraverse) throws IllegalArgumentException {
    if (model == null || toTraverse == null) {
      throw new IllegalArgumentException("given null");
    }

    if (model.numImages() == 0) {
      return "no layers present";
    }
    int currLayer = model.getCurrentLayer();
    specificOperation(model, currLayer);
    return "applied " + toTraverse + " on layer #" + (currLayer + 1);
  }

  /**
   * Apply the specific operation of the subclass of this abstract class at the current layer
   * to the model. Note, the model should automatically handle shifting of the current layer
   * in cases like removing the current layer.
   *
   * @param model the model to mutate.
   * @param currLayer the current layer of the model to apply a command to.
   */
  protected abstract void specificOperation(ILayeredModel model, int currLayer);
}
