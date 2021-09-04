package controller.importexport;

import model.image.IImage;
import model.image.IPixel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation of IFileExporter that exports files to P3 (plain) PPM files.
 */
public class PPMExporter implements IFileExporter {

  @Override
  public void export(IImage img, String fileName) throws IllegalArgumentException, IOException {
    if (img == null || fileName == null) {
      throw new IllegalArgumentException("given null argument.");
    }

    BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName + ".ppm"));
    // writing:  P3 # filename.ppm width height maxColorValue
    bfw.write("P3 \n" + "# " + fileName + ".ppm \n" + img.getWidth() + " " + img.getHeight()
            + " 256 \n");
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        IPixel currPixel = img.getPixelAt(x, y);
        bfw.write(
                currPixel.getRed() + " " + currPixel.getGreen() + " "
                        + currPixel.getBlue() + " ");
      }
      if (y != img.getHeight() - 1) {
        bfw.write("\n");
      }
    }
    bfw.close();
  }
}
