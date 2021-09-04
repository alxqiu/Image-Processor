package controller.commands.layercommands;

import model.ILayeredModel;

/**
 * Mutates the model such that the layer at the given index will not be considered visible,
 * if it was not before. If it was already, then an exception will be thrown from the model,
 * as that is a model responsibilty and not a controller responsibility.
 */
public class MakeVis extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.makeLayerVisible(currLayer);
  }
}
