package controller.commands;

import model.ILayeredModel;

/**
 * Command object that informs the caller of the number of layers currently
 * existing in the given model, by accessing an observer method of the model.
 */
public class NumLayers implements IImageCommand {

  @Override
  public String goCommand(ILayeredModel model, String toTraverse) {
    return toTraverse + " " + model.numImages();
  }
}
