package controller.commands;

import model.ILayeredModel;

/**
 * Interface defining behavior for a command object that will mutate a model and return
 * a String back to the controller to process or transmit.
 */
public interface IImageCommand {
  /**
   * The command is executed and mutates the given model accordingly. The String
   * may used to varying extents by the command, and may contain relevant information to
   * perform the command.
   *
   * @return outcome of the command, or "No Output" if command does not have output.
   * @throws IllegalArgumentException if given null.
   */
  String goCommand(ILayeredModel model, String toTraverse) throws IllegalArgumentException;
}
