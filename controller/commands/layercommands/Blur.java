package controller.commands.layercommands;

import model.ILayeredModel;
import model.operations.OperationType;

/**
 * Blur implementation and subclass of layered (targeted) command on the current layer.
 */
public class Blur extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.applyIOperation(OperationType.BLUR, currLayer);
  }
}
