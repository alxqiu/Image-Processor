package controller.importexport;

import model.image.IImage;
import model.image.IPixel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that handles exporting for jpg, jpeg and png images.
 */
public class AdvancedUtilExporter implements IFileExporter {
  private final AdvancedFileType currType;

  /**
   * Constructs a new AdvancedUtilExporter object based on the given type of file to
   * export as.
   *
   * @param type enum value such as JPG, JPEG or PNG.
   */
  public AdvancedUtilExporter(AdvancedFileType type) {
    currType = type;
  }


  @Override
  public void export(IImage img, String fileName) throws IllegalArgumentException, IOException {
    BufferedImage bImg = new BufferedImage(img.getWidth(),
            img.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img.getWidth(); i++) {
      for (int j = 0; j < img.getHeight(); j++) {
        IPixel currPix = img.getPixelAt(i, j);
        int convertedRGB = new Color(currPix.getRed(),
                currPix.getGreen(), currPix.getBlue()).getRGB();
        bImg.setRGB(i, j , convertedRGB);
      }
    }

    String extension;
    switch (this.currType) {
      case PNG:
        extension = "png";
        break;
      case JPG:
        extension = "jpg";
        break;
      case JPEG:
        extension = "jpeg";
        break;
      default:
        throw new IllegalStateException("currType cannot be null");
    }

    File output = new File(fileName + "." + extension);

    // not assuming we have the type specified in the file name.

    // need to create a BufferedImage out of width, height, and TYPE_INT_RGB
    ImageIO.write(bImg, extension, output);
  }
}
