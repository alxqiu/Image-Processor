package model.operations.colortransform;

import model.image.IImage;

/**
 * Sepia color transformation.
 */
public class Sepia extends AbstractColorTransform {
  /**
   * Constructs a new Sepia color transformation function object.
   *
   * @param img IImage to apply Sepia color transformation to.
   * @throws IllegalArgumentException if given null.
   */
  public Sepia(IImage img) throws IllegalArgumentException {
    super(img);

    // couldn't figure out how to make this programmatically....
    this.colorMatrix[0][0] = 0.393;
    this.colorMatrix[0][1] = 0.769;
    this.colorMatrix[0][2] = 0.189;
    this.colorMatrix[1][0] = 0.349;
    this.colorMatrix[1][1] = 0.686;
    this.colorMatrix[1][2] = 0.168;
    this.colorMatrix[2][0] = 0.272;
    this.colorMatrix[2][1] = 0.534;
    this.colorMatrix[2][2] = 0.131;
  }
}
