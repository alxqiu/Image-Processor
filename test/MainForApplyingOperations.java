import controller.importexport.BasicPpmImporter;
import controller.importexport.IFileExporter;
import controller.importexport.PPMExporter;
import model.image.IImage;
import model.operations.colortransform.Greyscale;
import model.operations.colortransform.Sepia;
import model.operations.filter.BlurFilter;
import model.operations.filter.SharpenFilter;

import java.io.IOException;

/**
 * Tester code to run to simply see the output of all images we want to apply effects to.
 */
public class MainForApplyingOperations {
  /**
   * Entry point for simply importing, applying, and exporting sample images.
   *
   * @param args cmd line args.
   */
  public static void main(String[] args) {
    try {
      applyBlur();
      applySharpen();
      applyGreyscale();
      applySepia();
    } catch (IOException io) {
      // do nothing and exit
    }
  }

  /**
   * Blur both sample images 5 times.
   *
   * @throws IOException if exporting fails.
   */
  private static void applyBlur() throws IOException {
    IImage curr1 = new BasicPpmImporter().importFrom("res/potat.ppm");
    IImage curr3 = new BasicPpmImporter().importFrom("res/mountains.ppm");
    curr1 = new BlurFilter(curr1).apply();
    curr3 = new BlurFilter(curr3).apply();
    for (int i = 0; i < 5; i++) {
      curr1 = new BlurFilter(curr1).apply();
      curr3 = new BlurFilter(curr3).apply();
    }
    IFileExporter newView = new PPMExporter();
    newView.export(curr1, "res/blur_potat");
    newView.export(curr3, "res/blur_mountains");
  }

  /**
   * Applies sharpen once to both sample images.
   *
   * @throws IOException if exporting fails.
   */
  private static void applySharpen() throws IOException {
    IImage curr1 = new BasicPpmImporter().importFrom("res/potat.ppm");
    IImage curr3 = new BasicPpmImporter().importFrom("res/mountains.ppm");
    curr1 = new SharpenFilter(curr1).apply();
    curr3 = new SharpenFilter(curr3).apply();
    IFileExporter newView = new PPMExporter();
    newView.export(curr1, "res/sharp_potat");
    newView.export(curr3, "res/sharp_mountains");
  }

  /**
   * Applies Greyscale once to both sample images.
   *
   * @throws IOException if exporting fails.
   */
  private static void applyGreyscale() throws IOException {
    IImage curr1 = new BasicPpmImporter().importFrom("res/potat.ppm");
    IImage curr3 = new BasicPpmImporter().importFrom("res/mountains.ppm");
    curr1 = new Greyscale(curr1).apply();
    curr3 = new Greyscale(curr3).apply();
    IFileExporter newView = new PPMExporter();
    newView.export(curr1, "res/grey_potat");
    newView.export(curr3, "res/grey_mountains");
  }

  /**
   * Applies Sepia once to both sample images.
   *
   * @throws IOException if exporting fails.
   */
  private static void applySepia() throws IOException {
    IImage curr1 = new BasicPpmImporter().importFrom("res/potat.ppm");
    IImage curr3 = new BasicPpmImporter().importFrom("res/mountains.ppm");
    curr1 = new Sepia(curr1).apply();
    curr3 = new Sepia(curr3).apply();
    IFileExporter newView = new PPMExporter();
    newView.export(curr1, "res/sepia_potat");
    newView.export(curr3, "res/sepia_mountains");
  }
}
