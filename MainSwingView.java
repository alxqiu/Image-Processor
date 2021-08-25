import controller.IController;
import controller.SwingController;
import model.ILayeredModel;
import model.LayeredProcessorModel;
import view.ISwingView;
import view.NewSwingView;


/**
 * Entry point for testing the Swing view.
 */
public class MainSwingView {
  /**
   * main method for entering this program.
   * @param args yes.
   */
  public static void main(String[] args) {
    ILayeredModel model = new LayeredProcessorModel();
    ISwingView view = new NewSwingView();
    IController control = new SwingController(model, view);
    control.startProcessing();
  }
}
