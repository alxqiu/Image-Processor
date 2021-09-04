package controller.commands.layercommands;

import model.ILayeredModel;
import model.operations.OperationType;

/**
 * Sharpen implementation and subclass of layered (targeted) command on the current layer.
 */
public class Sharpen extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.applyIOperation(OperationType.SHARPEN, currLayer);
  }
}
