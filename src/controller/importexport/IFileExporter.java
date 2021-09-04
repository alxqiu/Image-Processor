package controller.importexport;

import model.image.IImage;

import java.io.IOException;

/**
 * Interface defining behavior for exporting IImage images.
 */
public interface IFileExporter {
  /**
   * Exports the given IImage to the default package, with the given filename.
   *
   * @param img      an IImage to export.
   * @param fileName the name and directory of the file to export to.
   * @throws IllegalArgumentException if given a null argument
   * @throws IOException              if file writing fails
   */
  void export(IImage img, String fileName) throws IllegalArgumentException, IOException;
}
