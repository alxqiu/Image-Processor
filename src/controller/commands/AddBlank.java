package controller.commands;

import model.ILayeredModel;

/**
 * Command object that mutates the model by adding a new blank layer to it.
 */
public class AddBlank implements IImageCommand {

  @Override
  public String goCommand(ILayeredModel model, String toTraverse) {
    model.addBlankLayer();
    return toTraverse + " performed. created blank layer #" + model.numImages();
  }
}
