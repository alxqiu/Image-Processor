package controller;

import controller.importexport.DialogType;
import controller.importexport.IImporter;
import controller.importexport.IFileExporter;
import controller.importexport.FileUtils;

import model.ILayeredModel;
import model.operations.OperationType;
import view.ISwingView;

import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * IController implementation that uses a model and view, and mutates the view
 * and model in synchronized actions. Tells the view when to render images and
 * features, and tells the controller to mutate in certain ways from actions.
 * Specific for implementations of the ILayeredModel interface. Contains listener
 * classes that are used when the controller is "started".
 */
public class SwingController implements IController {
  private final ILayeredModel model;
  private final ISwingView view;
  private OperationType currOperation;

  /**
   * Constructs a new SwingController object out of a view and a model.
   *
   * @param model model to mutate and observe.
   * @param view  view to mutate to display changes to the model.
   * @throws IllegalArgumentException if either argument is null.
   */
  public SwingController(ILayeredModel model, ISwingView view) throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("given null as model or view");
    }
    this.model = model;
    this.view = view;
    // just make the current option the first type of operation, for now
    currOperation = OperationType.values()[0];
  }


  @Override
  public void startProcessing() {
    view.startViewing();
    view.setListener(new SwingController.ButtonsListener());
    view.setListener(new SwingController.OpsDropdownListener());
    view.setListener(new SwingController.IOListener());
    view.setListener(new SwingController.LayerSelectListener());
    view.setListener(new SwingController.VisibleCheckboxListener());
    view.setListener(new SwingController.LayerRenameListener());
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException
            | InstantiationException | IllegalAccessException e) {
      //handle exception here.
    }
  }

  /**
   * Returns the index of the top most visible
   * layer below the current layer.
   *
   * @return current layer if there are no visible layers below current,
   *                    or the index of the highest visible layer below this.
   *                    If there are no layers, this should return -1, also equal
   *                    to the current layer.
   */
  private int getTopMostVisibleLayer() {
    int topMostVisibleLayer = model.getCurrentLayer();
    for (int i = model.getCurrentLayer(); i >= 0; i--) {
      if (!model.getInvisibleLayers().contains(i)) {
        topMostVisibleLayer = i;
        break;
      }
    }
    return topMostVisibleLayer;
  }

  /**
   * Listener class that handles button actions, such as apply, add blank, and remove.
   */
  private class ButtonsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String[] buttonsSet = new String[]{"apply curr operation", "find script",
        "add blank", "add copy", "remove"};
      if (!Arrays.asList(buttonsSet).contains(e.getActionCommand())) {
        return;
      }
      try {
        switch (e.getActionCommand()) {
          case "apply curr operation":
            // per Piazza @1678: allow operations to be applied to invisible layers
            if (model.getCurrentLayer() != -1) {
              model.applyIOperation(currOperation, model.getCurrentLayer());
              view.renderMessage("applied " + currOperation + " to "
                      + (model.getNamesMap().get(model.getCurrentLayer())));
            } else {
              view.renderMessage("no layers present");
              return;
            }
            break;
          case "find script":
            File selectedFile = view.openDialogBox(DialogType.OPEN, "txt");
            if (selectedFile == null) {
              return;
            }
            try {
              String filePath = selectedFile.getAbsolutePath();
              FileUtils.ensureTxtExtension(FileUtils.getExtension(filePath));
              new ImageController(model,
                      new InputStreamReader(new FileInputStream(filePath)), view).startProcessing();
              view.renderMessage("executed script " + FileUtils.fileName(filePath));
              model.setCurrentLayer(model.numImages() - 1);
            } catch (IllegalArgumentException ia) {
              // graceful error handling thrown from model or util classes
              view.renderMessage(ia.getMessage());
            }
            break;
          case "add blank":
            model.addBlankLayer();
            view.renderMessage("added new blank layer at layer #"
                    + (model.getCurrentLayer() + 1));
            break;
          case "add copy":
            if (model.numImages() == 0) {
              view.renderMessage("no layers present");
              return;
            }

            // adds a freshly visible copy of the current layer
            int prevCurrentIdx = model.getCurrentLayer();
            String prevName = model.getNamesMap().get(prevCurrentIdx);
            model.addImage(model.getImageAt(prevCurrentIdx));
            model.renameLayerAt(model.getCurrentLayer(), prevName);
            view.renderMessage("added copy of " + prevName + " at layer #"
                    + (model.getCurrentLayer() + 1));
            break;
          case "remove":
            if (model.getCurrentLayer() != -1) {
              int removedLayer = model.getCurrentLayer();
              String removedName = model.getNamesMap().get(removedLayer);
              model.removeAt(removedLayer);
              view.renderMessage("removed " + removedName);
              if (model.numImages() == 0) {
                // if we removed down to last layer, we clear the IImage render
                view.renderIImage(null);
                view.adjustLayeredState(0, -1, model.getNamesMap());
                view.adjustVisibilityState(model.getInvisibleLayers(), model.getCurrentLayer());
                return;
              }
            } else {
              view.renderMessage("no layers present");
              return;
            }
            break;
          default:
            // should not get here
        }
        if (model.getInvisibleLayers().contains(model.getCurrentLayer())) {
          view.renderIImage(null);
        } else {
          view.renderIImage(model.getImageAt(model.getCurrentLayer()));
        }
        view.adjustLayeredState(model.numImages(), model.getCurrentLayer(), model.getNamesMap());
        view.adjustVisibilityState(model.getInvisibleLayers(), model.getCurrentLayer());
      } catch (IOException io) {
        throw new IllegalStateException("view transmission failed");
      }
    }
  }

  private class VisibleCheckboxListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!e.getActionCommand().equals("visible")) {
        return;
      }
      try {
        JCheckBox visibility = (JCheckBox) e.getSource();
        if (!visibility.isSelected()) {
          model.makeLayerInvisible(model.getCurrentLayer());
          int topMostVisibleLayer = getTopMostVisibleLayer();
          // if not visible image below the layer that was just
          // made invisible, then render null
          if (topMostVisibleLayer == model.getCurrentLayer()) {
            view.renderIImage(null);
          } else {
            view.renderIImage(model.getImageAt(topMostVisibleLayer));
          }
        } else {
          model.makeLayerVisible(model.getCurrentLayer());
          view.renderIImage(model.getImageAt(model.getCurrentLayer()));
        }
      } catch (IOException io) {
        throw new IllegalStateException("view rendering failed");
      }
    }
  }

  /**
   * Listener class that handles dropdown menu actions for operation selections.
   * NOTE: for this Listener class and subsequent listeners, was told by TA in OH
   * that casting is okay to gain access to swing component methods like JComboBox
   * as that prevents us from having to add more public facing functionality for
   * access to specific components of the view.
   */
  private class OpsDropdownListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!e.getActionCommand().equals("operations list")) {
        return;
      }
      // only the operations list dropdown menu is guaranteed to give us
      // that action command, we can trust that it is a JComboBox<String>
      JComboBox<?> dropdown = (JComboBox<?>) e.getSource();
      for (int i = 0; i < dropdown.getItemCount(); i++) {
        if (dropdown.getItemAt(i) == null) {
          throw new IllegalArgumentException("dropdown items cannot be null");
        }
      }
      currOperation = OperationType.values()[dropdown.getSelectedIndex()];
      try {
        view.renderMessage("selected operation " + currOperation.toString());
      } catch (IOException ioException) {
        throw new IllegalStateException("view transmission failed");
      }
    }
  }

  /**
   * Listener class that handles dropdown menu actions for layer selections.
   */
  private class LayerSelectListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!e.getActionCommand().equals("layers list")) {
        return;
      }
      JComboBox<?> dropdown = (JComboBox<?>) e.getSource();
      if (dropdown.getItemCount() == 0 || dropdown.getSelectedItem() == null
              || dropdown.getSelectedItem().equals("no layers present")) {
        // conditions of the dropdown that may be true that we cannot
        // work with.
        return;
      }
      for (int i = 0; i < dropdown.getItemCount(); i++) {
        if (dropdown.getItemAt(i) == null) {
          throw new IllegalArgumentException("dropdown items cannot be null");
        }
      }
      // each index of the dropdown should correspond to each layer's index + 1.
      // what looks like "1" should correspond to layer #0.
      model.setCurrentLayer(dropdown.getSelectedIndex());
      try {
        if (model.getInvisibleLayers().contains(model.getCurrentLayer())) {
          int topVisible = getTopMostVisibleLayer();
          if (getTopMostVisibleLayer() != model.getCurrentLayer()) {
            view.renderIImage(model.getImageAt(topVisible));
          } else {
            view.renderIImage(null);
          }
        } else {
          view.renderIImage(model.getImageAt(model.getCurrentLayer()));
        }
        view.adjustVisibilityState(model.getInvisibleLayers(), model.getCurrentLayer());
      } catch (IOException ioException) {
        throw new IllegalStateException("view transmission failed");
      }
    }
  }

  /**
   * Listener class that handles import and export actions.
   */
  private class IOListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String[] ioSet = new String[]{"export", "import", "export all"};
      if (!Arrays.asList(ioSet).contains(e.getActionCommand())) {
        return;
      }
      try {
        switch (e.getActionCommand()) {
          case "export":
            int topMostVisible = getTopMostVisibleLayer();
            // ensure that we export only the topmost visible layer below the "current" layer.
            if (model.numImages() == 0 || (topMostVisible == model.getCurrentLayer()
                    && (model.isLayerInvisible(model.getCurrentLayer())))) {
              view.renderMessage("no visible layers present or below current layer");
              return;
            }
            File selectedFile = view.openDialogBox(DialogType.SAVE, FileUtils.getImageExtensions());
            if (selectedFile == null) {
              view.renderMessage("file already exists with same name in directory");
              return;
            }
            try {
              IFileExporter exporter = FileUtils.findCorrectExporter(
                      selectedFile.getAbsolutePath());
              // findCorrectExporter ensures that the path is valid and has a valid type.
              // remove the extension from the call to export:
              String truncatedPath = selectedFile.getAbsolutePath().substring(0,
                      selectedFile.getAbsolutePath().lastIndexOf('.'));

              if (FileUtils.fileNameHasReserved(
                      FileUtils.fileName(selectedFile.getAbsolutePath()))) {
                view.renderMessage("file name should not contain reserved chars "
                        + Arrays.toString(FileUtils.RESERVED));
                return;
              }

              exporter.export(model.getImageAt(topMostVisible), truncatedPath);
              view.renderMessage("exported to " + selectedFile.getAbsolutePath());

            } catch (IllegalArgumentException ia) {
              view.renderMessage(ia.getMessage());
            }
            break;
          case "import":
            selectedFile = view.openDialogBox(DialogType.OPEN, FileUtils.getImageExtensions());
            if (selectedFile == null) {
              return;
            }
            String filePath = selectedFile.getAbsolutePath();
            IImporter importer;
            try {
              importer = FileUtils.findCorrectImporter(filePath);
              model.addImage(importer.importFrom(filePath));
              model.renameLayerAt(model.getCurrentLayer(), FileUtils.fileName(filePath));
              view.renderIImage(model.getImageAt(model.getCurrentLayer()));
            } catch (IllegalArgumentException ia) {
              // graceful error handling thrown from model or util classes
              view.renderMessage(ia.getMessage());
              return;
            }
            view.adjustLayeredState(model.numImages(),
                    model.getCurrentLayer(), model.getNamesMap());
            view.adjustVisibilityState(model.getInvisibleLayers(), model.getCurrentLayer());
            break;

          case "export all":
            // export all is not bound by the restriction of topmost visible layer

            selectedFile = view.openDialogBox(DialogType.SAVE, FileUtils.getImageExtensions());
            if (model.numImages() == 0) {
              view.renderMessage("no visible layers present or below current layer");
              return;
            }
            if (selectedFile == null) {
              view.renderMessage("Please select a file.");
              return;
            }
            if (FileUtils.fileNameHasReserved(
                    FileUtils.fileName(selectedFile.getAbsolutePath()))) {
              view.renderMessage("file name should not contain reserved chars "
                      + Arrays.toString(FileUtils.RESERVED));
              return;
            }
            try {
              IFileExporter exporter = FileUtils.findCorrectExporter(
                      selectedFile.getAbsolutePath());
              // findCorrectExporter ensures that the path is valid and has a valid type.
              // remove the extension from the call to export:
              String truncatedPath = selectedFile.getAbsolutePath().substring(0,
                      selectedFile.getAbsolutePath().lastIndexOf('.'));
              FileWriter layeredFile = new FileWriter(
                      truncatedPath + " exported_layers.txt", true);
              for (int i = 0; i < model.numImages(); i++) {
                exporter.export(model.getImageAt(i), truncatedPath + (i + 1));
                layeredFile.append("\nimport " + truncatedPath + (i + 1) + "."
                        + FileUtils.getExtension(selectedFile.getAbsolutePath()));
                if (model.isLayerInvisible(i)) {
                  layeredFile.append("\nmake invis");
                }
              }
              layeredFile.append("\nclose program");
              layeredFile.close();
              view.renderMessage("Successfully exported all images.");
            } catch (IOException excep) {
              view.renderMessage("Exporting layers unsuccessful." + excep.getMessage());
            }
            break;
          default:
            // should not get here
        }
      } catch (IOException io) {
        throw new IllegalStateException("view transmission failed");
      }
    }
  }

  /**
   * Listener implementation that mutates the names according
   * to the new names given by the JTextField.
   */
  private class LayerRenameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!e.getActionCommand().equals("rename layer")) {
        return;
      }
      JTextField renameBox = (JTextField) e.getSource();
      String newName = renameBox.getText();
      renameBox.setText("");

      try {
        // check for reserved characters here
        if (FileUtils.fileNameHasReserved(newName)) {
          view.renderMessage("name should not have reserved chars "
                  + Arrays.toString(FileUtils.RESERVED));
          return;
        }
        if (newName.length() > 20) {
          view.renderMessage("name should not be more than 20 chars");
          return;
        }
        if (model.getNamesMap().containsValue(newName)) {
          view.renderMessage("name already exists");
          return;
        }
        int currLayer = model.getCurrentLayer();
        String oldName = model.getNamesMap().get(currLayer);
        model.renameLayerAt(currLayer, newName);
        view.adjustLayeredState(model.numImages(), currLayer, model.getNamesMap());
        view.adjustVisibilityState(model.getInvisibleLayers(), model.getCurrentLayer());
        view.renderMessage("renamed " + oldName + " to " + newName);
      } catch (IOException io) {
        throw new IllegalStateException("view transmission failed");
      }
    }
  }
}
