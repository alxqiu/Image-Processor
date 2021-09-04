import controller.importexport.IFileExporter;
import model.image.IImage;

import java.io.IOException;

/**
 * Mock class of IFileExporter that just throws a new IOException.
 */
public class MockExporter implements IFileExporter {

  @Override
  public void export(IImage img, String fileName) throws IllegalArgumentException, IOException {
    throw new IOException();
  }
}
