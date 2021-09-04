package controller.importexport;

import java.util.Arrays;
import java.util.Locale;


/**
 * Utility class for importing and exporting across all acceptable file types, including .ppm
 * and all advanced file types.
 */
public class FileUtils {
  // reservedChars should include windows forbidden file chars
  // as well as '.', '#' and '\n'.
  public static final char[] RESERVED = {'<', '>', ':', '\"',
    '/', '\\', '|', '?', '*', '.', '#', '\n'};

  /**
   * Finds the correct IImporter object to handle the importing for the given filePath.
   * NOTE: does NOT say if the file path exists or not, just enforces that the file path
   * has a valid type.
   *
   * @param filePath String representing filepath in format "C:\\user\\dir\\file.png".
   * @return Appropriate IImporter object for the given file path.
   * @throws IllegalArgumentException if given null or file path does not
   *                                  have valid extension (.ppm, .jpg, etc.).
   */
  public static IImporter findCorrectImporter(String filePath)
          throws IllegalArgumentException {
    String extension = FileUtils.getExtension(filePath);
    ensureImgValidExtension(extension);
    if (extension.equals("ppm")) {
      return new BasicPpmImporter();
    } else {
      return new AdvancedBasicImporter();
    }
  }

  /**
   * Finds the correct IFileExporter object to handle the exporting for the given filePath.
   *
   * @param filePath String representing filepath in format "C:\\user\\dir\\file.png".
   * @return appropriate IFileExporter object that can handle the given type.
   * @throws IllegalArgumentException if given null or file path does not
   *                                  have valid extension (.ppm, .jpg, etc.).
   */
  public static IFileExporter findCorrectExporter(String filePath)
          throws IllegalArgumentException {
    String extension = FileUtils.getExtension(filePath);
    ensureImgValidExtension(extension);
    switch (extension) {
      case "ppm":
        return new PPMExporter();
      case "jpg":
        return new AdvancedUtilExporter(AdvancedFileType.JPG);
      case "jpeg":
        return new AdvancedUtilExporter(AdvancedFileType.JPEG);
      case "png":
        return new AdvancedUtilExporter(AdvancedFileType.PNG);
      default:
        // should not get here....
        throw new IllegalArgumentException("invalid file extension");
    }
  }

  /**
   * Returns the file extension from the given file path. Note, does not check if the extension
   * is valid as an image extension or txt.
   *
   * @param filePath String representing filepath in format "C:\\user\\dir\\file.png".
   * @return the file extension in a lowercase String format ("ppm", "jpg", etc.).
   * @throws IllegalArgumentException if given null or file path does not
   *                                  have valid extension (.ppm, .jpg, etc.).
   */
  public static String getExtension(String filePath)
          throws IllegalArgumentException {
    if (filePath == null || !filePath.contains(".")
            || filePath.substring(filePath.lastIndexOf(".")).length() == 1) {
      // potential causes of index out of bounds exceptions: if there is no '.' char, or
      // if there is nothing after the '.' char.
      throw new IllegalArgumentException("file path must be full: include name, directory, "
              + "and extension specified (.ppm, .png ...)");
    }
    return filePath.substring(filePath.lastIndexOf('.') + 1);
  }

  /**
   *Returns the file name from the given file path.
   *
   *@param filePath String representing filepath in format "C:\\user\\dir\\file.png".
   *@return the file name without an extension "res\potat.png" returns "potat".
   *@throws IllegalArgumentException if given null or file path does not
   *                                 have valid extension (.ppm, .jpg, etc.).
   */
  public static String fileName(String filePath) throws IllegalArgumentException {
    if (filePath == null || !filePath.contains(".")) {
      throw new IllegalArgumentException("given null instead of filePath, "
              + "need a valid file name with extension");
    }
    String truncated = filePath;
    if (filePath.contains("\\")) {
      if (truncated.substring(truncated.lastIndexOf('\\')).length() == 1) {
        throw new IllegalArgumentException("invalid path, need file name "
                + "and extension after last \\");
      }
      // this ensures there is at least one final character after the last folder
      truncated = truncated.substring(filePath.lastIndexOf('\\') + 1);
    }
    return truncated.substring(0, truncated.lastIndexOf("."));
  }

  /**
   * Ensures that the given extension is valid, across the supported image file types, including
   * ppm and all advanced file types.
   *
   * @param extension lowercased string file extension with no period.
   * @throws IllegalArgumentException if it is not a supported file type.
   */
  public static void ensureImgValidExtension(String extension) throws IllegalArgumentException {
    // so far the valid file types: ppm, png, jpeg, and jpg
    extension = extension.toLowerCase(Locale.ROOT);
    if (!Arrays.asList(getImageExtensions()).contains(extension)) {
      throw new IllegalArgumentException("invalid file extension, must be "
              + Arrays.toString(getImageExtensions()));
    }
  }

  /**
   * Ensures that the given string is exactly "txt".
   * @param extension a string that appears after the "." in a valid file path.
   * @throws IllegalArgumentException if String is not "txt".
   */
  public static void ensureTxtExtension(String extension) throws IllegalArgumentException {
    if (!extension.equals("txt")) {
      throw new IllegalArgumentException("file extension must end with txt");
    }
  }


  /**
   * Returns the valid image file extensions supported (ppm, png, jpeg, and jpg),
   * in an array formatted exactly as shown.
   * @return array of valid image file extensions.
   */
  public static String[] getImageExtensions() {
    String[] fileTypes = new String[AdvancedFileType.values().length + 1];
    fileTypes[0] = "ppm";
    for (int i = 1; i < fileTypes.length; i++) {
      // not hard coding in the advanced file types.
      fileTypes[i] = AdvancedFileType.values()[i - 1].toString().toLowerCase(Locale.ROOT);
    }
    return fileTypes;
  }

  /**
   * Checks if the file name has any illegal characters, specified in RESERVED.
   * @param fileName file name, without extension or leading file path.
   * @returns true if the file name has illegal characters, false otherwise.
   * @throws IllegalArgumentException if given null.
   */
  public static boolean fileNameHasReserved(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("given null instead of file name");
    }
    for (char c : RESERVED) {
      if (fileName.contains(String.valueOf(c))) {
        return true;
      }
    }
    return false;
  }
}
