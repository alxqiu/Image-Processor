package model;

import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import model.image.patterns.PatternCreator;
import model.operations.IOperationAdapterImpl;
import model.operations.OperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A model that treats each IImage added to it as a new layer. INVARIANT: each layer is the same
 * dimensions as the first IImage added. Operations can only be applied to the "current layer",
 * which this model contains methods to observe and change.
 */
public class LayeredProcessorModel extends BasicProcessorModel implements ILayeredModel {

  private int currentLayer;
  // collection of indexes of invisible layers.
  private List<Integer> invisibleLayers;

  // CHANGE in HW07: perhaps add in names here....
  // each index value is mapped to a String name.
  private Map<Integer, String> layerNames;

  /**
   * Constructs a new LayeredProcessorModel object.
   */
  public LayeredProcessorModel() {
    super();
    currentLayer = 0;
    invisibleLayers = new ArrayList<>();
    layerNames = new HashMap<>();
  }

  @Override
  public void renameLayerAt(int idx, String name) throws IllegalArgumentException {
    if (!layerNames.containsKey(idx)) {
      throw new IllegalArgumentException("index does not exist");
    }
    String adjustedName = name;

    while (layerNames.containsValue(adjustedName)) {
      adjustedName += "-copy";
    }

    layerNames.put(idx, adjustedName);
  }


  public Map<Integer, String> getNamesMap() {
    // returns defensive copy
    return new HashMap<>(this.layerNames);
  }

  @Override
  public ArrayList<Integer> getInvisibleLayers() {
    return new ArrayList<>(this.invisibleLayers);
  }

  @Override
  public void makeLayerInvisible(int index) throws IllegalArgumentException {
    indexOutOfBoundsChecker(index);
    invisibleLayers.add(index);
  }

  @Override
  public void makeLayerVisible(int index) throws IllegalArgumentException {
    indexOutOfBoundsChecker(index);
    // need to cast to avoid having .remove() think we are referring to int.
    invisibleLayers.remove((Integer) index);
  }

  @Override
  public boolean isLayerInvisible(int index) throws IllegalArgumentException {
    indexOutOfBoundsChecker(index);
    return invisibleLayers.contains(index);
  }

  @Override
  public int getCurrentLayer() {
    if (this.numImages() == 0) {
      return -1;
    }

    return this.currentLayer;
  }

  @Override
  public void setCurrentLayer(int i) throws IllegalArgumentException {
    indexOutOfBoundsChecker(i);
    this.currentLayer = i;
  }

  /**
   * Throws an exception if the given index is out of bounds (<0, or higher than highest index).
   *
   * @param i index to verify.
   * @throws IllegalArgumentException if index is out of bounds
   */
  private void indexOutOfBoundsChecker(int i) throws IllegalArgumentException {
    if (i < 0 || i > this.numImages() - 1) {
      throw new IllegalArgumentException("layer index out of bounds");
    }
  }

  @Override
  public void addBlankLayer() {
    // if there are not images, then the width and height will be set to 100 x 100
    // if there are images, we take the last one and get the width and height.
    int width;
    int height;

    if (this.numImages() == 0) {
      width = 100;
      height = 100;
    } else {
      IImage lastImage = this.getImageAt(this.numImages() - 1);
      width = lastImage.getWidth();
      height = lastImage.getHeight();
    }

    IPixel[] blankPixels = new IPixel[width * height];
    int blankIter = 0;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        blankPixels[blankIter] = new BasicPixel(j, i, 255, 255, 255);
        blankIter++;
      }
    }

    IImage newBlank = new BasicImage(width, height, blankPixels);
    this.addImage(newBlank);
    this.renameLayerAt(currentLayer, "new blank");
  }


  @Override
  public void addImage(IImage toAdd) throws IllegalArgumentException {
    checkIfSameSize(toAdd);
    super.addImage(toAdd);
    int newLayerIdx = super.numImages() - 1;
    this.layerNames.put(newLayerIdx, "placeholder #" + newLayerIdx);
    this.currentLayer = newLayerIdx;
  }

  /**
   * Verifies that the given IImage is of the same size as the IImage last added.
   *
   * @param toCheck an IImage to verify.
   * @throws IllegalArgumentException if given IImage is not the same size as the
   *                                  IImage just added.
   */
  private void checkIfSameSize(IImage toCheck) throws IllegalArgumentException {
    if (super.numImages() != 0) {
      IImage lastImage = super.getImageAt(super.numImages() - 1);
      if (lastImage.getWidth() != toCheck.getWidth() || lastImage.getHeight() != toCheck
          .getHeight()) {
        throw new IllegalArgumentException(
            "new layer cannot be different dimensions from previous layer");
      }
    }
  }

  @Override
  public IImage removeAt(int i) throws IllegalArgumentException {
    indexOutOfBoundsChecker(i);
    if (currentLayer == i) {
      // reset currentlayer to -1 if there are not layers left.
      if (currentLayer == 0 && this.numImages() != 1) {
        currentLayer++;
      }
      currentLayer--;
    }

    // remove the index of the invisible layer if it show up,
    // and decrease all indexes in invisible layers by 1 if they are above
    // the index to remove at.
    if (invisibleLayers.contains(i)) {
      ArrayList<Integer> tempInvisibles = new ArrayList<>();
      for (Integer j : invisibleLayers) {
        int tempIdx = j;
        if (j > i) {
          tempIdx--;
        } else if (j == i) {
          // don't bother adding back if j is equal to i.
          continue;
        }
        tempInvisibles.add(tempIdx);
      }
      invisibleLayers = tempInvisibles;
    }

    // need to adjust invisible layers anyways if we remove a single layer
    // removes the name.
    this.layerNames.remove(i);

    // adjust all keys in the map higher than the index
    ArrayList<Integer> tempIndices = new ArrayList<>(this.layerNames.keySet());
    for (Integer tempIdx : tempIndices) {
      if (tempIdx > i) {
        String tempName = this.layerNames.remove(tempIdx);
        this.layerNames.put(tempIdx - 1, tempName);
      }
    }

    return super.removeAt(i);
  }

  @Override
  public void applyIOperation(OperationType toPerform, int index)
          throws IllegalArgumentException {
    if (index != this.currentLayer) {
      throw new IllegalArgumentException("cannot apply to non-current layer");
    }
    if (toPerform == null) {
      throw new IllegalArgumentException("given null as argument");
    }
    indexOutOfBoundsChecker(index);

    // remove the current index.
    IImage toApplyTo = super.removeAt(index);

    // we don't need to change the invisibleLayers indices,
    // as everything should be added to the place it was before

    // this should enforce two invariants: layers with this operation applied to it will retain
    // their visibility state and their position. Indices should remain in the same place!

    ArrayList<IImage> toAddBack = new ArrayList<>();

    // for all IImages above the index we removed from, take them out and add back
    // in the same order.

    // CHANGE IN HW7: Debugged a issue where we iterated over super.numImages(), which
    // we forgot changed as the loop progressed.
    int tempRemovalIterator = super.numImages();
    for (int i = index; i < tempRemovalIterator; i++) {
      toAddBack.add(super.removeAt(index));
    }
    super.addImage(new IOperationAdapterImpl().adaptOperation(toPerform, toApplyTo));

    for (IImage img : toAddBack) {
      super.addImage(img);
    }
    this.currentLayer = index;
  }

  @Override
  public void addFromPattern(PatternCreator fromPattern) throws IllegalArgumentException {
    if (fromPattern == null) {
      throw new IllegalArgumentException("given null as argument");
    }
    IImage result = fromPattern.create();
    checkIfSameSize(result);
    super.addFromPattern(fromPattern);
    int newLayerIdx = super.numImages() - 1;
    this.layerNames.put(newLayerIdx, "new pattern #" + newLayerIdx);
    this.currentLayer = newLayerIdx;
  }
}
