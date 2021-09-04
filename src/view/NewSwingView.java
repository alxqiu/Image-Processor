package view;

import controller.importexport.DialogType;
import model.image.IImage;
import model.image.IPixel;
import model.operations.OperationType;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ISwingView implementation that uses a JFrame and Swing components to represent a view
 * object that can renders state changes and operations of an Image processing software.
 * Not coupled to a model, but relies on a controller object or at least a listener object
 * to dictate the results of action listeners being triggered.
 */
public class NewSwingView implements ISwingView {

  private final JTextField renderMessageOutput;
  private final JComboBox<String> operationList;
  private final JButton applyOperationButton;
  private final JLabel placeHolderImage;
  private final JButton importButton;
  private final JButton exportButton;
  private final JButton addBlankButton;
  private final JButton addCopyButton;
  private final JButton removeButton;
  private final JComboBox<String> layerSelection;
  private final JTextField renameLayer;
  private final JFrame frame;
  private final JCheckBox visible;
  private final JButton findScriptButton;
  private final JButton enterNewName;
  private final JButton exportAll;

  /**
   * Constructs a new NewSwingView object, and sets up all the components and
   * action commands. Does not make the view visible, as that is the job of the
   * method startViewing().
   */
  public NewSwingView() {
    this.frame = new JFrame();

    frame.setTitle("Layered Image Manipulation and Enhancement Program");
    frame.setSize(800, 800);

    // base that all images and controls lie on
    JPanel base = new JPanel();
    base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

    /*
    Operations selection and apply...
     */
    JPanel operationsPanel = new JPanel();
    operationsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
    base.add(operationsPanel);
    operationsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    JLabel comboboxDisplay = new JLabel("To Apply:");
    operationsPanel.add(comboboxDisplay);
    String[] options = new String[OperationType.values().length];
    for (int i = 0; i < options.length; i++) {
      options[i] = OperationType.values()[i].toString();
    }
    operationList = new JComboBox<>(options);
    // this tells the controller that the e.getSource() was from
    // this exact object.
    operationList.setActionCommand("operations list");
    operationsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    operationsPanel.add(operationList);
    // button for apply:
    applyOperationButton = new JButton("Apply");
    applyOperationButton.setActionCommand("apply curr operation");
    operationsPanel.add(applyOperationButton);
    // button to find script:
    findScriptButton = new JButton("Run Script");
    findScriptButton.setActionCommand("find script");
    operationsPanel.add(findScriptButton);


    /*
      set up images: create empty border, will render the images when they are added
      be sure to put gray border around the images, so that white blank layers can be obvious
      add placeholder component for IImage now:
      show an image with a scrollbar
    */
    JPanel imagePanel = new JPanel();
    base.add(imagePanel);
    placeHolderImage = new JLabel();
    placeHolderImage.setHorizontalAlignment(SwingConstants.CENTER);
    // getting the image will be done when renderIImage is called.

    // packing onto frame:
    JScrollPane imageScrollPane = new JScrollPane(placeHolderImage);
    imageScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 15));
    imageScrollPane.setPreferredSize(new Dimension(400, 400));
    imagePanel.add(imageScrollPane);


    /*
    add and remove layer buttons
     */
    JPanel ioPanel = new JPanel();
    ioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    ioPanel.setBorder(BorderFactory.createTitledBorder("Layer I/O"));
    base.add(ioPanel);
    /*
      import and export
     */
    importButton = new JButton("Import");
    importButton.setActionCommand("import");
    ioPanel.add(importButton);
    exportButton = new JButton("Export");
    exportButton.setActionCommand("export");
    ioPanel.add(exportButton);

    // export all:
    exportAll = new JButton("Export All");
    exportAll.setActionCommand("export all");
    ioPanel.add(exportAll);


    /*
    Layer Manipulation
     */
    JPanel layerManipulation = new JPanel();

    // layer selection dropdown
    layerManipulation.setLayout(new FlowLayout(FlowLayout.LEFT));
    layerManipulation.setBorder(BorderFactory.createTitledBorder("Layer Manipulation"));
    JLabel layerLabel = new JLabel("Current Layer: ");
    layerManipulation.add(layerLabel);
    layerSelection = new JComboBox<>();
    layerSelection.setPreferredSize(new Dimension(200, 25));
    layerSelection.setMaximumSize(layerSelection.getPreferredSize());
    layerSelection.addItem("no layers present");
    layerSelection.setActionCommand("layers list");
    layerManipulation.add(layerSelection);

    // layer visibility
    visible = new JCheckBox("Visible");
    visible.setSelected(false);
    visible.setActionCommand("visible");
    visible.setEnabled(false);
    layerManipulation.add(visible);

    // layer renaming
    JLabel renameLabel = new JLabel("Rename Layer: ");
    layerManipulation.add(renameLabel);
    renameLayer = new JTextField();
    renameLayer.setPreferredSize(new Dimension(150, 25));
    renameLayer.setMaximumSize(layerSelection.getPreferredSize());
    renameLayer.setActionCommand("rename layer");
    renameLayer.setEnabled(false);
    layerManipulation.add(renameLayer);

    // low level listener for the button, not for the controller to see.
    ActionListener renameTextFieldListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(enterNewName)) {
          renameLayer.postActionEvent();
          renameLayer.setText("");
        }
      }
    };
    //controller listens to text field, text field listens to button, button listens to click
    enterNewName = new JButton("Enter");
    enterNewName.setEnabled(false);
    enterNewName.addActionListener(renameTextFieldListener);
    layerManipulation.add(enterNewName);

    base.add(layerManipulation);

    /*
      Add & Remove Layers
     */
    JPanel addRemoveLayers = new JPanel();
    addRemoveLayers.setLayout(new FlowLayout(FlowLayout.LEFT));
    addRemoveLayers.setBorder(BorderFactory.createTitledBorder("+/- Layers"));

    // adding and removing layers
    addBlankButton = new JButton("Add Blank Layer");
    addBlankButton.setActionCommand("add blank");
    addCopyButton = new JButton("Add Copy");
    addCopyButton.setActionCommand("add copy");
    removeButton = new JButton("Remove Layer");
    removeButton.setActionCommand("remove");
    addRemoveLayers.add(addBlankButton);
    addRemoveLayers.add(addCopyButton);
    addRemoveLayers.add(removeButton);

    base.add(addRemoveLayers);


    /*
      interactions text bubble (where all render message stuff should go)
     */
    renderMessageOutput = new JTextField();
    renderMessageOutput.setEditable(false);
    // add to the bottom, separate from all other components
    frame.getContentPane().add(renderMessageOutput, BorderLayout.SOUTH);

    // making everything appear now
    frame.getContentPane().add(base);
  }

  @Override
  public void renderMessage(String msg) throws IllegalArgumentException {
    if (msg == null) {
      throw new IllegalArgumentException("given null instead of message to render");
    }
    renderMessageOutput.setText(msg);
  }

  @Override
  public void renderIImage(IImage img) {
    if (img == null) {
      // note, if null is given, then the current image will be removed.
      placeHolderImage.setIcon(null);
      return;
    }
    // can convert to BufferedImage, then use ImageIcon in swing to make it show up
    // if this is called, ensure that the model has the correct number of images for this to
    // be rendered.
    BufferedImage bImg = new BufferedImage(img.getWidth(),
            img.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img.getWidth(); i++) {
      for (int j = 0; j < img.getHeight(); j++) {
        IPixel currPix = img.getPixelAt(i, j);
        int convertedRGB = new Color(currPix.getRed(),
                currPix.getGreen(), currPix.getBlue()).getRGB();
        bImg.setRGB(i, j, convertedRGB);
      }
    }
    placeHolderImage.setIcon(new ImageIcon(bImg));
  }


  @Override
  public void setListener(ActionListener listener) {
    // OH 6/25: was told that its okay to add everything to the
    // given listener, as long as we carefully ignore other actions in
    // listeners designed for specific components.
    operationList.addActionListener(listener);
    applyOperationButton.addActionListener(listener);
    addBlankButton.addActionListener(listener);
    addCopyButton.addActionListener(listener);
    removeButton.addActionListener(listener);
    importButton.addActionListener(listener);
    exportButton.addActionListener(listener);
    layerSelection.addActionListener(listener);
    visible.addActionListener(listener);
    findScriptButton.addActionListener(listener);
    renameLayer.addActionListener(listener);
    exportAll.addActionListener(listener);
  }

  @Override
  public void startViewing() {
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.setVisible(true);
  }

  @Override
  public void adjustVisibilityState(List<Integer> invisIndices, int currLayer) {
    if (currLayer == -1) {
      this.visible.setEnabled(false);
      this.visible.setSelected(false);
    } else {
      this.visible.setSelected(!invisIndices.contains(currLayer));
      this.visible.setEnabled(true);
    }
  }


  @Override
  public void adjustLayeredState(int numImages, int currLayer, Map<Integer, String> namesMap) {
    if (numImages < 0 || currLayer > numImages - 1) {
      throw new IllegalArgumentException("currLayer cannot be higher than numImages - 1 and"
              + " numImages cannot be less than 0");
    }
    layerSelection.removeAllItems();
    if (numImages == 0 && currLayer == -1) {
      try {
        layerSelection.addItem("no layers present");
        layerSelection.setSelectedItem("no layers present");
        // additionally, the state of the JTextField for renaming is in the exact
        // same case, so we change that here
        renameLayer.setText("");
        renameLayer.setEnabled(false);
        enterNewName.setEnabled(false);

      } catch (IllegalArgumentException ia) {
        // the above call fires an action event even though we don't want it to
        // so we just do nothing.
      }
      return;
    }
    renameLayer.setEnabled(true);
    enterNewName.setEnabled(true);

    // update the names
    for (int i = 1; i <= numImages; i++) {
      layerSelection.addItem(namesMap.get(i - 1));
    }
    layerSelection.setSelectedIndex(currLayer);
  }

  @Override
  public File openDialogBox(DialogType d, String... fileTypes) throws IllegalArgumentException {
    if (d == null) {
      throw new IllegalArgumentException("DialogType cannot be null");
    }

    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Files " + Arrays.toString(fileTypes), fileTypes);
    fileChooser.setFileFilter(filter);

    int result;
    switch (d) {
      case OPEN:
        result = fileChooser.showOpenDialog(this.frame);
        break;
      case SAVE:
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = fileChooser.showSaveDialog(this.frame);
        break;
      default:
        // should not get here
        throw new IllegalArgumentException("should not reach this point");
    }

    File selection;
    if (result == JFileChooser.APPROVE_OPTION) {
      selection = fileChooser.getSelectedFile();
    } else {
      selection = null;
    }

    // this file can't mutate the view object in any way, so we
    // don't make a defensive copy.
    return selection;
  }
}
