package controller.commands.layercommands;

import model.ILayeredModel;

/**
 * Removes the given layer from the model. The layer index supplied should be the current layer.
 */
public class Remove extends AbstractLayerCommand {

  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.removeAt(currLayer);
  }
}
