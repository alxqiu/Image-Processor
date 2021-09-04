import model.BasicProcessorModel;
import model.IProcessorModel;
import model.LayeredProcessorModel;
import model.ProcessorModelCreator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test class that tests if the builder class ProcessorModelCreator works as expected.
 */
public class TestProcessorModelCreator {

  // testing creation of the correct implementation
  @Test
  public void testCreateBasic() {
    IProcessorModel newModel = ProcessorModelCreator.create(
            ProcessorModelCreator.ProcessorType.BASIC);
    assertTrue(newModel instanceof BasicProcessorModel);
  }

  // testing LAYERED value.
  @Test
  public void testCreateLayered() {
    IProcessorModel newModel = ProcessorModelCreator.create(
            ProcessorModelCreator.ProcessorType.LAYERED);
    assertTrue(newModel instanceof LayeredProcessorModel);
  }

  // testing that create will throw exception if given null.
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNull() {
    IProcessorModel newModel = ProcessorModelCreator.create(null);
  }
}
