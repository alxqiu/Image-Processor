package view;

import model.image.IImage;

import java.io.IOException;

/**
 * Interface defining behavior for viewing IImage processing operations
 * throught information given by the controller.
 */
public interface IImageView {

  /**
   * Renders the message of the given String to the appendable object.
   * @param msg String to render, usually given from the controller.
   * @throws IOException if rendering fails.
   * @throws IllegalArgumentException if given null (CHANGE IN HW7).
   *
   */
  void renderMessage(String msg) throws IOException, IllegalArgumentException;

  /**
   * Renders a given IImage, probably given from the controller to the view.
   * @param toRender an IImage to render to the output object this view is using.
   * @throws IOException if rendering fails.
   */
  void renderIImage(IImage toRender) throws IOException;
}
