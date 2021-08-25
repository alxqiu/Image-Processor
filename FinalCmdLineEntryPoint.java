import controller.IController;
import controller.ImageController;
import controller.SwingController;
import controller.importexport.FileUtils;
import model.ILayeredModel;
import model.ProcessorModelCreator;
import view.BasicView;
import view.IImageView;
import view.ISwingView;
import view.NewSwingView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Entry point for interacting with this project. Allows interaction through console, Swing GUI
 * and through batch file processing.
 */
public class FinalCmdLineEntryPoint {

  /**
   * Main method for this new jar.
   * @param args arguments needed: -text for console, -interactive for GUI,
   *             and -script file-path.txt for scripting.
   */
  public static void main(String[] args) {
    switch (args.length) {
      // if no args, just launch the gui.
      case 0:
        launchGUI();
        return;
      case 1:
        if (args[0].equals("-text")) {
          ILayeredModel model = (ILayeredModel) ProcessorModelCreator.create(
                  ProcessorModelCreator.ProcessorType.LAYERED);
          IImageView view = new BasicView(System.out);
          IController control = new ImageController(
                  model, new InputStreamReader(System.in), view);
          control.startProcessing();
        } else if (args[0].equals("-interactive")) {
          launchGUI();
        } else {
          break;
        }
        return;
      case 2:
        if (!args[0].equals("-script")) {
          break;
        }
        String path = args[1];
        String extension = FileUtils.getExtension(path);
        if (!(extension.equals("txt") || FileUtils.getExtension(path).equals("bat"))) {
          System.out.println("Path must lead to .txt or .bat file");
          break;
        }
        try {
          InputStreamReader fileInput = new InputStreamReader(new FileInputStream(path));
          ILayeredModel model = (ILayeredModel) ProcessorModelCreator.create(
                  ProcessorModelCreator.ProcessorType.LAYERED);
          new ImageController(model, fileInput,
                  new BasicView(System.out)).startProcessing();
        } catch (FileNotFoundException fnf) {
          System.out.println("File not found");
          return;
        }
        return;
      default:
        // do nothing here, just go to outside of switch statement
    }
    System.out.println("Unrecognized Command Line Argument, the following commands are valid: \n"
            + "-script path-of-script-file\n"
            + "-text\n"
            + "-interactive\n"
            + "for script, console, and GUI control, respectively.");
  }

  /**
   * Method that launches the control from the interactive GUI controller.
   */
  private static void launchGUI() {
    ILayeredModel model = (ILayeredModel) ProcessorModelCreator.create(
            ProcessorModelCreator.ProcessorType.LAYERED);
    ISwingView view = new NewSwingView();
    IController control = new SwingController(model, view);
    control.startProcessing();
  }
}
