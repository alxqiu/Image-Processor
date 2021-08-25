package model;

import java.util.List;
import java.util.Map;

/**
 * ADDED IN HW7: added in new observer methods in the case that we needed a read-only
 * version of the layered model state.
 */
public interface LayeredModelState extends ViewModelState {
  /**
   * Observer for the mappings of layer indices to names.
   *
   * @return defensive copy of the map of indices and names. Returns empty
   *                                map if none.
   */
  Map<Integer, String> getNamesMap();

  /**
   * An observer method that gets the list of invisible image indices.
   * @return the list of invisible images' indices
   */
  List<Integer> getInvisibleLayers();

  /**
   * Observer method to examine which layer is currently being modified.
   *
   * @return -1 if there are no layers, and the index of the current layer otherwise.
   */
  int getCurrentLayer();

  /**
   * An observer method to identify if the given index represents an
   * invisible layer.
   *
   * @param index the layer to perform this on.
   * @return true if layer is invisible, false otherwise.
   * @throws IllegalArgumentException if given index is out of bounds.
   */
  boolean isLayerInvisible(int index) throws IllegalArgumentException;
}
