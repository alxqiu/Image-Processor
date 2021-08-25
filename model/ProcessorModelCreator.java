package model;

/**
 * Creator class that creates a new IProcessorModel based on the given enum.
 */
public class ProcessorModelCreator {
  /**
   * Enum for all processor types that implement IProcessorModel.
   */
  public enum ProcessorType {
    BASIC, LAYERED;
  }

  /**
   * Constructs a new IProcessorModel object based on given type.
   *
   * @param type type of Processor model to create.
   * @return the constructed ProcessorModel.
   * @throws IllegalArgumentException if given null
   */
  public static IProcessorModel create(ProcessorType type) {
    if (type == null) {
      throw new IllegalArgumentException("ProcessorType cannot be null.");
    }
    switch (type) {
      case BASIC:
        return new BasicProcessorModel();
      case LAYERED:
        return new LayeredProcessorModel();
      default:
        // should not get here....this return statement is here to avoid java complaint.
        return null;
    }
  }
}
