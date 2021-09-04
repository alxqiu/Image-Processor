package controller.commands;

import model.ILayeredModel;

/**
 * Command object that informs the caller of the current layer of the model by
 * accessing an observer method of the given model.
 */
public class CurrentLayer implements IImageCommand {
  @Override
  public String goCommand(ILayeredModel model, String toTraverse) {
    if (model.getCurrentLayer() == -1) {
      return "no layers present";
    }
    return toTraverse + " is layer #" + (model.getCurrentLayer() + 1);
  }
}
