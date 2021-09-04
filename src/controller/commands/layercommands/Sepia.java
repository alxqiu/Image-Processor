package controller.commands.layercommands;

import model.ILayeredModel;
import model.operations.OperationType;

/**
 * Sepia implementation and subclass of layered (targeted) command on the current layer.
 */
public class Sepia extends AbstractLayerCommand {
  @Override
  protected void specificOperation(ILayeredModel model, int currLayer) {
    model.applyIOperation(OperationType.SEPIA, currLayer);
  }
}
