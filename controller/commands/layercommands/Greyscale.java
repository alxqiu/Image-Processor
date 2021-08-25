package controller.commands.layercommands;

import model.ILayeredModel;
import model.operations.OperationType;

/**
 * Greyscale implementation and subclass of layered (targeted) command on the current layer.
 */
public class Greyscale extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.applyIOperation(OperationType.GREYSCALE, currLayer);
  }
}
