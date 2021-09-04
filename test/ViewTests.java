import model.image.BasicImage;
import model.image.BasicPixel;
import model.image.IImage;
import model.image.IPixel;
import org.junit.Before;
import org.junit.Test;
import view.BasicView;
import view.IImageView;

import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Testing all relevant methods for the view.
 */
public class ViewTests {
  IImageView view;
  Appendable output;

  @Before
  public void init() {
    output = new StringBuilder();
    view = new BasicView(output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNull() {
    new BasicView(null);
  }

  @Test
  public void testDoNothingIImageNull() throws IOException {
    view.renderIImage(null);
    assertEquals(output.toString(), "");
  }

  @Test
  public void testNothingHappensRenderMessageNull() throws IOException {
    view.renderMessage(null);
    assertEquals(output.toString(), "");
  }

  @Test
  public void testRenderMessage() throws IOException {
    view.renderMessage("hello :)");
    assertEquals(output.toString(), "hello :)");
  }

  @Test
  public void testRenderIImage() throws IOException {
    IPixel topLeft = new BasicPixel(0, 0, 1, 1, 1);
    IPixel topRight = new BasicPixel(1, 0, 2, 2, 2);
    IPixel bottomLeft = new BasicPixel(0, 1, 4, 4, 4);
    IPixel bottomRight = new BasicPixel(1, 1, 3, 3, 3);
    IImage img = new BasicImage(2, 2,
            new IPixel[]{topLeft, topRight, bottomLeft, bottomRight});

    view.renderIImage(img);
    assertEquals(output.toString(), "Width: 2, Height: 2");
  }
}
