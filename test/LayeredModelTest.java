import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import model.ILayeredModel;
import model.ProcessorModelCreator;
import org.junit.Before;
import org.junit.Test;

/**
 * A tester class for the new interface methods in the ILayeredModel interface and
 * its implementation.
 */
public class LayeredModelTest {

  ILayeredModel layer;

  @Before
  public void init() {
    layer = (ILayeredModel) ProcessorModelCreator.create(
            ProcessorModelCreator.ProcessorType.LAYERED);
  }

  @Test
  // tests the addBlank method
  public void testAddBlank() {
    assertEquals(-1, layer.getCurrentLayer());
    layer.addBlankLayer();
    assertEquals(0, layer.getCurrentLayer());
    layer.addBlankLayer();
    assertEquals(2, layer.numImages());
  }

  @Test
  // tests the setCurrentLayer method
  public void testSetcurrentLayer() {
    layer.addBlankLayer();
    layer.addBlankLayer();
    layer.addBlankLayer();
    assertEquals(2, layer.getCurrentLayer());
    layer.setCurrentLayer(2);
    assertEquals(2, layer.getCurrentLayer());
    layer.setCurrentLayer(1);
    assertEquals(1, layer.getCurrentLayer());
  }

  @Test
  // tests the getCurrentLayer method
  public void testGetCurrentLayer() {
    layer.addBlankLayer();
    assertEquals(0, layer.getCurrentLayer());
  }

  @Test
  // test if the isLayerInvisible method works as intended
  public void testIsLayerInvisible() {
    layer.addBlankLayer();
    layer.addBlankLayer();
    layer.makeLayerInvisible(1);
    assertFalse(layer.isLayerInvisible(0));
    assertTrue(layer.isLayerInvisible(1));
  }


  @Test(expected = IllegalArgumentException.class)
  // tests the makeLayerInvisible exception (actually tests the private method,
  // but because the private method is called in every method,
  // it is sufficient to just check it once)
  public void testInvisibleLayerOOB() {
    layer.addBlankLayer();
    layer.makeLayerInvisible(2);
  }

  @Test
  // tests if the layer was added to the invisible layers
  public void testInvisibleLayerAdd() {
    layer.addBlankLayer();
    layer.makeLayerInvisible(0);
    assertEquals(1, layer.getInvisibleLayers().size());
  }

  @Test(expected = IllegalArgumentException.class)
  // tests the visibleLayer out of boudns exception
  public void testMakeLayerVisibleOOB() {
    layer.addBlankLayer();
    layer.makeLayerVisible(2);
  }

  @Test
  // tests if the layer was removed from the invisible layers
  public void testVisibleLayerAdd() {
    layer.addBlankLayer();
    layer.makeLayerInvisible(0);
    assertEquals(1, layer.getInvisibleLayers().size());
    layer.makeLayerVisible(0);
    assertEquals(0, layer.getInvisibleLayers().size());
  }

}
