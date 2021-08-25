import controller.IController;
import controller.ImageController;
import model.ILayeredModel;
import model.ProcessorModelCreator;
import view.BasicView;

import java.io.InputStreamReader;

/**
 * Entry point for interacting with this project. Allows interaction through console
 * and through batch file processing.
 */
public class MainEntryPoint {

  /**
   * Main method to start with.
   * @param args not used.
   */
  public static void main(String[] args) {
    IController mainControl = new ImageController((ILayeredModel)
            ProcessorModelCreator.create(ProcessorModelCreator.ProcessorType.LAYERED),
            new InputStreamReader(System.in),
            new BasicView(System.out));
    mainControl.startProcessing();
  }
}
