package controller.commands.filecommands;

import controller.commands.IImageCommand;
import controller.importexport.AdvancedBasicImporter;
import controller.importexport.BasicPpmImporter;
import controller.importexport.FileUtils;
import controller.importexport.IImporter;
import model.ILayeredModel;

import java.io.IOException;

/**
 * Implementation of IImage command that imports the given filename to in the format
 * of the file name typed. Returns back a String informing of what went wrong if anything fails.
 * Mutates the model such that the imported IImage is added to the top layer.
 */
public class Import implements IImageCommand {
  @Override
  public String goCommand(ILayeredModel model, String toTraverse) throws IllegalArgumentException {
    // check for ppm or advanced file type.
    IImporter importer;

    // example: import res/potat.png
    String checkFileName = toTraverse.substring(7);
    String fileExtension = checkFileName.substring(checkFileName.length() - 4);

    if (fileExtension.equals(".ppm")) {
      importer = new BasicPpmImporter();
    } else if (fileExtension.equals(".jpg") || fileExtension.equals(".png")) {
      importer = new AdvancedBasicImporter();
    } else {
      return "unrecognized file type";
    }
    try {
      // CHANGE IN HW07: NOW EDITS NAME TO NAME OF FILE
      model.addImage(importer.importFrom(checkFileName));
      model.renameLayerAt(model.getCurrentLayer(), FileUtils.fileName(toTraverse));
    } catch (IOException io) {
      return "file importing failed with IOException";
    }

    return "imported " + checkFileName + " successfully!";
  }
}
