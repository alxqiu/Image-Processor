package controller.importexport;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Implementation of IImporter that can import jpeg and png images while disregarding
 * their alpha values, and returns a new BasicImage with all pixels from the original file.
 */
public class AdvancedBasicImporter implements IImporter {

  @Override
  public IImage importFrom(String fileName) throws IllegalArgumentException, IOException {
    if (fileName == null) {
      throw new IllegalArgumentException("given null instead of file name");
    }
    BufferedImage img;
    try {
      img = ImageIO.read(new FileInputStream(fileName));
    } catch (FileNotFoundException fnf) {
      throw new IllegalArgumentException("file not found!");
    }

    int width = img.getWidth();
    int height = img.getHeight();

    IPixel[] pixels = new IPixel[width * height];
    int pixelIter = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        // color constructor disregards alpha, even if .getRGB returns with alpha
        Color colors = new Color(img.getRGB(i, j));
        pixels[pixelIter] = new BasicPixel(i, j, colors.getRed(),
                colors.getGreen(), colors.getBlue());
        pixelIter++;
      }
    }
    return new BasicImage(width, height, pixels);
  }
}
