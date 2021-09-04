import model.image.IImage;
import model.operations.filter.AbstractFilter;

/**
 * Mock filter class that creates a kernel with a 4x4 matrix,
 * which should be illegal.
 */
public class MockFilter extends AbstractFilter {

  /**
   * Constructs a new AbstractFilter object, called by child class constructors.
   *
   * @param img IImage to apply filter to.
   * @throws IllegalArgumentException if given null.
   */
  public MockFilter(IImage img) throws IllegalArgumentException {
    super(img);
    this.kernel = new double[4][4];
  }
}
