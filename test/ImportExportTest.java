
import controller.importexport.BasicPpmImporter;
import model.image.IImage;
import model.image.patterns.CheckerBoardCreator;
import org.junit.Test;
import controller.importexport.PPMExporter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;


/**
 * Test class to see if the images we import end up exactly the same when we output.
 */
public class ImportExportTest {

  // test exceptions for exporter

  // given null file name export
  @Test(expected = IllegalArgumentException.class)
  public void exportGivenNullFileName() throws IOException {
    new PPMExporter().export(new CheckerBoardCreator(1, 1, 1).create(), null);
  }

  // given null image
  @Test(expected = IllegalArgumentException.class)
  public void exportGivenNullImg() throws IOException {
    new PPMExporter().export(null, "some_file_name");
  }

  // NOTE: we can't test whether PPMExporter will throw an IOException if given a mock
  // buffered as PPMExporter only uses BufferedWriter and we can't reliably cause that to
  // throw an exception.


  // test exceptions for importer

  // null file name import
  @Test(expected = IllegalArgumentException.class)
  public void givenNullFileName() {
    new BasicPpmImporter().importFrom(null);
  }

  // test importer with non-existent file name
  @Test(expected = IllegalArgumentException.class)
  public void givenNonExistentFileName() {
    new BasicPpmImporter().importFrom("file_name_doesn't_exist");
  }

  // test malformed ppm file....
  @Test(expected = IllegalArgumentException.class)
  public void givenNonP3File() {
    new BasicPpmImporter().importFrom("test/notp3.ppm");
  }

  // test malformed ppm file... runs out of input in the middle of reading a pixel...
  @Test(expected = IllegalArgumentException.class)
  public void givenMalformedFile() {
    new BasicPpmImporter().importFrom("test/malformed.ppm");
  }

  // test that import works correctly:
  @Test
  public void importAndCompare() {
    IImage reference = new CheckerBoardCreator(4, 4, 1).create();
    IImage imported = new BasicPpmImporter().importFrom("res/export_test.ppm");
    for (int i = 0; i < reference.getHeight(); i++) {
      for (int j = 0; j < reference.getWidth(); j++) {
        assertEquals(reference.getPixelAt(j, i), imported.getPixelAt(j, i));
      }
    }
  }

  // test exporting an image, importing it back in, then comparing with original....
  @Test
  public void ExportThenImportAndCompare() throws IOException {
    IImage reference = new CheckerBoardCreator(4, 4, 1).create();
    // overwrite existing file....
    new PPMExporter().export(reference, "res/export_test");
    IImage imported = new BasicPpmImporter().importFrom("res/export_test.ppm");
    for (int i = 0; i < reference.getHeight(); i++) {
      for (int j = 0; j < reference.getWidth(); j++) {
        assertEquals(reference.getPixelAt(j, i), imported.getPixelAt(j, i));
      }
    }
  }
}
