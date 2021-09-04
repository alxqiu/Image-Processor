package controller.commands.filecommands;

import controller.commands.IImageCommand;
import controller.importexport.AdvancedFileType;
import controller.importexport.AdvancedUtilExporter;
import controller.importexport.IFileExporter;
import controller.importexport.PPMExporter;
import model.ILayeredModel;

import java.io.IOException;

/**
 * Command class that represents an exporting of the "current layer" of the given model into
 * the given filename and format. Simply returns when no layers are present, and returns
 * messages to the caller when file writing fails, which should in turn transmit to the view.
 *
 */
public class Export implements IImageCommand {
  @Override
  public String goCommand(ILayeredModel model, String toTraverse) throws IllegalArgumentException {
    if (model.numImages() == 0) {
      return "no layers present";
    }

    IFileExporter exporter;
    String checkFileName = toTraverse.substring(7);
    String fileExtension = checkFileName.substring(checkFileName.length() - 4);
    boolean isJPEG = false;

    // switch structure not identical to import.java, so hard to abstractify...
    switch (fileExtension) {
      case ".ppm":
        exporter = new PPMExporter();
        break;
      case ".png":
        exporter = new AdvancedUtilExporter(AdvancedFileType.PNG);
        break;
      case "jpeg":
        exporter = new AdvancedUtilExporter(AdvancedFileType.JPEG);
        // reduce by 1 to get rid of the period....
        isJPEG = true;
        break;
      case ".jpg":
        exporter = new AdvancedUtilExporter(AdvancedFileType.JPG);
        break;
      default:
        return "unrecognized file type";
    }
    try {
      // .export doesn't take in file extension, so we remove that from the end.
      String toPass;
      if (isJPEG) {
        toPass = checkFileName.substring(0, checkFileName.length() - 5);
      } else {
        toPass = checkFileName.substring(0, checkFileName.length() - 4);
      }
      exporter.export(model.getImageAt(model.getCurrentLayer()), toPass);
    } catch (IOException io) {
      return "file writing unsuccessful due to IOException";
    }
    return "file writing to " + checkFileName + " successful";
  }
}
