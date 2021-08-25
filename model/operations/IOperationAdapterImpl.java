package model.operations;

import model.image.IImage;
import model.operations.colortransform.Greyscale;
import model.operations.colortransform.Sepia;
import model.operations.filter.BlurFilter;
import model.operations.filter.SharpenFilter;

import java.util.Random;

/**
 * Implementation of IOperation adapter that returns the appropriate IImage
 * created from the IOperation specified by toPerform enum, applied on the toApplyTo IImage.
 */
public class IOperationAdapterImpl implements IOperationAdapter {

  @Override
  public IImage adaptOperation(OperationType toPerform, IImage toApplyTo)
          throws IllegalArgumentException {
    if (toPerform == null || toApplyTo == null) {
      throw new IllegalArgumentException("arguments given cannot be null");
    }

    IOperation operation;
    switch (toPerform) {
      case BLUR:
        operation = new BlurFilter(toApplyTo);
        break;
      case GREYSCALE:
        operation  = new Greyscale(toApplyTo);
        break;
      case SEPIA:
        operation = new Sepia(toApplyTo);
        break;
      case SHARPEN:
        operation = new SharpenFilter(toApplyTo);
        break;
      case MOSAIC:
        // DESIGN CHOICE: num seeds is 4% of pixels with unseeded random object.
        // Reasoning: we would have to restructure how we tell the model to take in
        // OperationTypes, and although we were able to add a Seed Level chooser to the GUI,
        // we thought it was less necessary to upend our model and controller code to
        // accommodate one operation type. Our examples still show a range of percentage of
        // seeding.
        operation = new Mosaic(toApplyTo,
                (int) (toApplyTo.getHeight() * toApplyTo.getWidth() * 0.04), new Random());
        break;
      default:
        // shouldn't get here....
        throw new IllegalArgumentException("toPerform cannot be null");
    }
    return operation.apply();
  }
}
