import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import model.operations.Mosaic;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Testing the ability of the Mosaic operation on a small scale.
 */
public class MosaicTests {

  @Test
  public void testSimpleMosaic() {
    // simple IImage:
    IPixel topLeft = new BasicPixel(0, 0, 2, 8, 18);
    IPixel topRight = new BasicPixel(1, 0, 4, 12, 24);
    IPixel bottomLeft = new BasicPixel(0, 1, 6, 16, 28);
    IPixel bottomRight = new BasicPixel(1, 1, 8, 20, 34);
    IImage simpleSquare = new BasicImage(2, 2,
            new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});

    // apply operation with 1 seed
    IImage result = new Mosaic(simpleSquare, 1, new Random()).apply();
    for (int i = 0; i < result.getWidth(); i++) {
      for (int j = 0; j < result.getHeight(); j++) {
        IPixel currPixel = result.getPixelAt(i, j);
        assertEquals(5, currPixel.getRed());
        assertEquals(14, currPixel.getGreen());
        assertEquals(26, currPixel.getBlue());
      }
    }
  }
}
