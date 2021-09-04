import controller.IController;
import controller.ImageController;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import model.ILayeredModel;
import model.LayeredProcessorModel;
import org.junit.Test;
import view.BasicView;
import view.IImageView;

import static org.junit.Assert.assertEquals;

/**
 * A testing class for the controller.
 */
public class ControllerTest {

  private Readable sr = new StringReader("add blank");
  private Appendable sb = new StringBuilder();
  private ILayeredModel processor = new LayeredProcessorModel();
  private IImageView view = new BasicView(sb);

  @Test (expected = IllegalArgumentException.class)
  // tests if the correct exception was thrown with the given null arguments
  public void testNullModel() {
    IController c = new ImageController(null, sr, view);
  }

  @Test (expected = IllegalArgumentException.class)
  // tests if the correct exception was thrown with the given null arguments
  public void testNullReader() {
    IController c = new ImageController(processor, null, view);
  }

  @Test (expected = IllegalArgumentException.class)
  // tests if the correct exception was thrown with the given null arguments
  public void testNullView() {
    IController c = new ImageController(processor, sr, null);
  }

  @Test
  // test opening and closing messages
  public void testOpenThenClose() {
    Readable rd = new StringReader("close program\n");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "Pleasure working with you :)");
  }

  @Test
  // test invis
  public void makeInvis() {
    Readable rd = new StringReader("add blank\nmake invis\n");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "applied make invis on layer #1\n"
            + "Pleasure working with you :)");
    assertEquals(processor.getInvisibleLayers(), Arrays.asList(new Integer[]{0}));
  }

  @Test
  // test vis
  public void makeVis() {
    Readable rd = new StringReader("add blank\nmake invis\nmake vis");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "applied make invis on layer #1\n"
            + "applied make vis on layer #1\n"
            + "Pleasure working with you :)");
    assertEquals(processor.getInvisibleLayers(), new ArrayList<Integer>());
  }

  @Test
  // test unrecognized and then do a normal command, showing that bad input doesn't throw off
  // our controller's function
  public void unrecognizedThenRecognized() {
    Readable rd = new StringReader("add blank\nooga booga\nmake vis");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "unrecognized command, try again. \n"
            + "applied make vis on layer #1\n"
            + "Pleasure working with you :)");
    assertEquals(processor.getInvisibleLayers(), new ArrayList<Integer>());
  }

  @Test
  // testing import from
  public void importFrom() {
    Readable rd = new StringReader("import res/potat.ppm\nimport res/potat3.png"
            + "\nimport res/potat.jpg\n");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();

    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "imported res/potat.ppm successfully!\n"
            + "imported res/potat3.png successfully!\n"
            + "imported res/potat.jpg successfully!\n"
            + "Pleasure working with you :)");

    // ensure that the images are present on the model's end
    assertEquals(processor.numImages(), 3);
  }

  @Test
  // test removal
  public void removeCurrent() {
    Readable rd = new StringReader("add blank\nadd blank\ncurrent layer\nremove");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "add blank performed. created blank layer #2\n"
            + "current layer is layer #2\n"
            + "applied remove on layer #2\n"
            + "Pleasure working with you :)");

    // affirm that everything is here.
    assertEquals(processor.getCurrentLayer(), 0);
    assertEquals(processor.numImages(), 1);
  }

  // test some illegal moves
  @Test
  // testing failed import/export/batch file names
  public void makeFailedStuff() {
    Readable rd = new StringReader("add blank\nimport res/oogachaga.png"
            + "\nexport res/no.ogr\nbatch nonexistence.txt");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "file importing failed with IOException\n"
            + "unrecognized file type\n"
            + "batch reading unsuccessful with IOException\n"
            + "Pleasure working with you :)");
  }


  @Test
  // test loading from batch instructions
  public void testLoadingBatch() {
    Readable rd = new StringReader("batch test/addblank.txt\n");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "add blank performed. created blank layer #2\n"
            + "add blank performed. created blank layer #3\n"
            + "current layer is layer #3\n"
            + "batch reading successful\n"
            + "Pleasure working with you :)");
    // verify correct layer information:

    assertEquals(processor.getCurrentLayer(), 2);
    assertEquals(processor.numImages(), 3);
  }

  @Test
  // test loading from batch instructions, and that we can continue to use controller after
  // the script has been run
  public void testExampleScript1() {
    Readable rd = new StringReader("batch res/example-script-1.txt\nadd blank");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "add blank performed. created blank layer #2\n"
            + "set new current layer to #1\n"
            + "num layers 2\n"
            + "applied remove on layer #1\n"
            + "num layers 1\n"
            + "add blank performed. created blank layer #2\n"
            + "applied make invis on layer #2\n"
            + "applied make vis on layer #2\n"
            + "batch reading successful\n"
            + "add blank performed. created blank layer #3\n"
            + "Pleasure working with you :)");

    // the batch file adds two layers, affirm that there are three, to prove that our
    // controller can work after using the batch
    assertEquals(processor.numImages(), 3);
  }

  @Test
  // test loading from batch instructions, and that we can continue to use controller after
  // the script has been run
  public void testExampleScript2() {
    Readable rd = new StringReader("batch res/example-script-2.txt\nadd blank"
            + "\nremove\nclose program");
    IController c = new ImageController(processor, rd, view);
    c.startProcessing();
    assertEquals(sb.toString(), "Enter command or type directory of script: \n"
            + "add blank performed. created blank layer #1\n"
            + "add blank performed. created blank layer #2\n"
            + "set new current layer to #2\n"
            + "applied remove on layer #2\n"
            + "current layer is layer #1\n"
            + "applied make invis on layer #1\n"
            + "add blank performed. created blank layer #2\n"
            + "applied remove on layer #2\n"
            + "set new current layer to #1\n"
            + "applied make vis on layer #1\n"
            + "applied remove on layer #1\n"
            + "imported res/potat.jpg successfully!\n"
            + "applied sharpen on layer #1\n"
            + "applied greyscale on layer #1\n"
            + "file writing to res/potat3.png successful\n"
            + "batch reading successful\n"
            + "add blank performed. created blank layer #2\n"
            + "applied remove on layer #2\n"
            + "Pleasure working with you :)");

    // assert there is only one layer left.
    assertEquals(processor.numImages(), 1);
  }
}
