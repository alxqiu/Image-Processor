package controller.importexport;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Implementation of IImporter that outputs IImage objects using the classes BasicPixel and
 * BasicImage.
 */
public class BasicPpmImporter implements IImporter {

  @Override
  public IImage importFrom(String fileName) throws IllegalArgumentException {
    if (fileName == null) {
      throw new IllegalArgumentException("given null");
    }

    Scanner input;
    try {
      input = new Scanner(new FileInputStream(fileName));
    } catch (FileNotFoundException fnf) {
      throw new IllegalArgumentException("file not found!");
    }

    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (input.hasNextLine()) {
      String s = input.nextLine();
      if ((s.length() != 0) && (s.charAt(0) != '#')) {
        builder.append(s + System.lineSeparator());
      }
    }

    input = new Scanner(builder.toString());

    String token;

    token = input.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = input.nextInt();
    int height = input.nextInt();
    input.nextInt();

    IPixel[] pixels = new IPixel[width * height];
    int pixelsIterator = 0;

    try {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int r = input.nextInt();
          int g = input.nextInt();
          int b = input.nextInt();
          pixels[pixelsIterator] = new BasicPixel(j, i, r, g, b);
          pixelsIterator++;
        }
      }
    } catch (NoSuchElementException nse) {
      throw new IllegalArgumentException("insufficient data, malformed ppm file.");
    }

    return new BasicImage(width, height, pixels);
  }
}
