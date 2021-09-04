package controller.commands.layercommands;

import model.ILayeredModel;

/**
 * Mutates the model so that the layer at the given index will now be considered invisible.
 */
public class MakeInvis extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.makeLayerInvisible(currLayer);
  }
}
