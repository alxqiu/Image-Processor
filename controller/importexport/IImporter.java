package controller.importexport;

import model.image.IImage;

import java.io.IOException;


/**
 * An interface for importing into IImages, so that we can create file objects that may
 * handle different file types and IImage implementations, such that each use of importing
 * uses this interface type and is not coupled to a specific file type or IImage/IPixel
 * implementation.
 */
public interface IImporter {

  /**
   * Takes in a file name and returns the file as a IImage object.
   *
   * @param fileName some file name for a file of the Implementation's file type, such
   *                 as PPM, PNG, etc. Includes directory, like dir/filename.ppm
   * @throws IllegalArgumentException if given file is not found, or file doesn't fit
   *                                  specifications of the file type the implementation
   *                                  of this interface is supposed to handle. Or if given
   *                                  null in fileName.
   */
  IImage importFrom(String fileName) throws IllegalArgumentException, IOException;
}
