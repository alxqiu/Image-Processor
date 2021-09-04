package view;

import model.image.IImage;

import java.io.IOException;

/**
 * Implementation of IImage view, just transmits the messages given to the appendable object.
 * Textual view only essentially renders the most basic image transmitted to it, like IImage
 * dimensions and messages exactly as they appear.
 */
public class BasicView implements IImageView {
  private final Appendable output;

  /**
   * Creates a new View that has an Appendable object to transmit messages to.
   * If the appendable object fails to transmit, then it will be replaced with System.out.
   *
   * @param output an Appendable object.
   * @throws IllegalArgumentException if given null as an argument.
   */
  public BasicView(Appendable output) throws IllegalArgumentException {
    if (output == null) {
      throw new IllegalArgumentException("given null as arg");
    }
    // interpreting "invalid" appendable as an Appendable that throws an exception when
    // attempting to use it...
    Appendable temp;
    try {
      output.append("");
      temp = output;
    } catch (IOException io) {
      temp = System.out;
    }
    this.output = temp;
  }


  @Override
  public void renderMessage(String msg) throws IOException {
    if (msg == null) {
      // do nothing if given null.
      return;
    }
    this.output.append(msg);
  }

  @Override
  public void renderIImage(IImage toRender) throws IOException {
    // for the purposes of our text based view, this one just renders
    // the width x height. We expect to render more complex things in a
    // different implementation.
    if (toRender == null) {
      // do nothing if given null.
      return;
    }
    this.output.append("Width: " + toRender.getWidth() + ", Height: " + toRender.getHeight());
  }
}
