package controller;

import controller.commands.AddBlank;
import controller.commands.CurrentLayer;
import controller.commands.IImageCommand;
import controller.commands.NumLayers;
import controller.commands.SetCurrent;
import controller.commands.filecommands.Export;
import controller.commands.filecommands.Import;
import controller.commands.layercommands.Blur;
import controller.commands.layercommands.Greyscale;
import controller.commands.layercommands.MakeInvis;
import controller.commands.layercommands.MakeVis;
import controller.commands.layercommands.Remove;
import controller.commands.layercommands.Sepia;
import controller.commands.layercommands.Sharpen;
import model.ILayeredModel;
import view.IImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A controller class to handle inputs and outputs. Can read from a file or read inputs from the
 * user. Processes commands to manipulate, add, and remove individual layers of an ILayeredModel,
 * and can read commands from a batch file, provided that they are given in the same format.
 */
public class ImageController implements IController {
  private final Readable rd;
  private final IImageView view;
  private final ILayeredModel model;
  private final Map<String, IImageCommand> commands;

  /**
   * Creates a new ImageController object with the given model, readable and view.
   * Initializes commands to those defined in the constructor.
   *
   * @param model model to control and mutate.
   * @param rd readable object to read input from.
   * @param view view to transmit messages to.
   * @throws IllegalArgumentException if either argument is null.
   */
  public ImageController(ILayeredModel model, Readable rd, IImageView view)
          throws IllegalArgumentException {
    if (model == null || rd == null || view == null) {
      throw new IllegalArgumentException("no null args");
    }
    this.rd = rd;
    this.view = view;
    this.model = model;

    commands = new HashMap<>();
    commands.put("add blank", new AddBlank());
    commands.put("num layers", new NumLayers());
    commands.put("current layer", new CurrentLayer());
    commands.put("set current", new SetCurrent());
    commands.put("remove", new Remove());
    commands.put("make vis", new MakeVis());
    commands.put("make invis", new MakeInvis());
    commands.put("blur", new Blur());
    commands.put("greyscale", new Greyscale());
    commands.put("sepia", new Sepia());
    commands.put("sharpen", new Sharpen());
    commands.put("import", new Import());
    commands.put("export", new Export());
    commands.put("batch ", new Batch());
  }

  @Override
  public void startProcessing() {
    Scanner sc = new Scanner(this.rd);
    try {
      view.renderMessage("Enter command or type directory of script: \n");
      processNextLines(sc);
      view.renderMessage("Pleasure working with you :)");
    } catch (IOException io) {
      throw new IllegalStateException("file reading/writing failed.");
    }
  }

  /*

  Controls that require a target <index>: 1/1 done
    -set current <index>

  Controls that assume target is selected
  (assumes current layer index is already defined): 7/7 done
    -blur, sharpen, greyscale, sepia
    -make invis
    -make vis
    -remove

  Controls that require a filename <fn>: 3/3 done
    -export <fn> --> needs current layer too.
    -import <fn>
    -read batch <fn>

  Don't require target or filename: 4/4 done
    -current layer
    -num layers
    -add blank
    -close program

  Another rule for batch files: the first # of a comment
   need to be spaced exactly one space out from the end of a command.

   No commands should have trailing spaces.
   */

  /**
   * Processes the next lines of the Scanner while the scanner still has input. Mutates
   * the model according to the commands defined.
   *
   * @param sc Scanner to iterate through. Each line is processed to see if it is a valid command.
   * @throws IOException if file reading/writing fails during this process. Most IOExceptions, such
   *          as with importing/exporting causes a message to be transmitted to the view.
   */
  private void processNextLines(Scanner sc) throws IOException {
    while (sc.hasNextLine()) {
      // Assume each line is typed perfectly, except for those that
      // require filename or number. Non-case sensitive...
      String currLine = sc.nextLine().toLowerCase();

      // if its a comment or has a length of 0, just continue.
      if (currLine.startsWith("#") || currLine.length() == 0) {
        continue;
      }
      if (currLine.equals("close program")) {
        // need to break here, can't have a method that breaks for us....
        break;
      }

      // if a " #" appears in the line, ignore all text after it.
      if (currLine.contains("#")) {
        currLine = currLine.substring(0, currLine.indexOf('#') - 1);
      }


      // if selecting a certain layer....need to be able to parse for the index
      String tempCmd = currLine;

      // checking for file name command and set current command
      if ( tempCmd.length() > 12
              && tempCmd.startsWith("set current ")) {
        // we can hard code here, because tempCmd will at least have set current....
        tempCmd = tempCmd.substring(0, 11);
      } else {
        tempCmd = checkForFileNameCmd(currLine);
      }

      //DESIGN CHOICE: the controller IS NOT the one who keeps track of current layer
      //as that is the model's job, only provides the means for a user to see and set the current
      //layer.

      if (this.commands.containsKey(tempCmd)) {
        IImageCommand cmd = this.commands.get(tempCmd);
        try {
          this.view.renderMessage(cmd.goCommand(this.model, currLine) + "\n");
        } catch (IllegalArgumentException ia) {
          this.view.renderMessage("Command failed: " + ia.getMessage() + "\n");
        }
      } else {
        this.view.renderMessage("unrecognized command, try again. \n");
      }
    }
    // just exit when done...
  }

  /**
   * Ensures that tempCmd is given as the correct key in containsKey, for the sake of
   * checking for file names.
   *
   * @param currLine a current line to traverse.
   * @return a command key if the right characters have been found. Otherwise, just the
   *                  current line as it was given.
   */
  private String checkForFileNameCmd(String currLine) {
    if (!(currLine.startsWith("export")
            || currLine.startsWith("import")
            || currLine.startsWith("batch "))) {
      return currLine;
    }

    String temp = currLine.substring(0, 6);
    if (temp.equals("export") || temp.equals("import")
            || temp.equals("batch ")) {
      return temp;
    } else {
      return currLine;
    }
  }

  /**
   * Private class integrated in this ImageController class, so that it has access to the
   * private processNextLines method. Makes more sense to do this, rather than have to make that
   * method public or manually process lines.
   */
  private class Batch implements IImageCommand {

    @Override
    public String goCommand(ILayeredModel model, String toTraverse)
            throws IllegalArgumentException {
      // we can trust that toTraverse is at least "batch "

      // meaning that toTraverse could be this for example: "batch script.bat"

      String extension = toTraverse.substring(toTraverse.length() - 4);

      // check file extension:
      if (extension.equals(".bat") || extension.equals(".txt")) {
        try {
          Scanner newScan = new Scanner(new File(toTraverse.substring(6)));
          processNextLines(newScan);
        } catch (IOException io) {
          // this case includes if file not found
          return "batch reading unsuccessful with IOException";
        }
        return "batch reading successful";
      } else {
        return "batch file format unrecognized, must be in format \"batch dir/script.bat\"";
      }
    }
  }
}
