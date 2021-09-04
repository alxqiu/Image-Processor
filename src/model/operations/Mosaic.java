package model.operations;

import model.image.IImage;
import model.image.IPixel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Function object implementation of IOperation that generates a mosaic
 * of the given IImage it was constructed with using a given number of seeds.
 * Note, location of seeds is randomly chosen.
 *
 * <p>As of 8:07pm PT, the benchmarks for mosaic on manhattan-small.png (100,000px)
 * is 4.5 seconds for 4,000 seeds (4%), 7.2 seconds for 8,000 seeds (8%), and
 * 15 seconds for 15,000 seeds (15%). Tested on i5-3230M@2.60GHz and GT650M with 6GB of memory.
 * </p>
 */
public class Mosaic implements IOperation {
  private final int seeds;
  private final IImage root;
  private final Random rand;

  /**
   * Constructs a new Mosaic out of the given IImage and the number of seeds.
   *
   * @param root  the IImage to construct and build a manipulated IImage from.
   * @param seeds the number of seeds to create this new Mosaic object from.
   * @param rand  random object to perform seeding with
   * @throws IllegalArgumentException if given null, a seed count lower than 1,
   *                                  or higher than the number of pixels
   */
  public Mosaic(IImage root, int seeds, Random rand) throws IllegalArgumentException {
    if (root == null || rand == null) {
      throw new IllegalArgumentException("root image or rand cannot be null");
    }
    if (seeds > root.getHeight() * root.getWidth()) {
      throw new IllegalArgumentException("cannot create with more seeds than pixels");
    }
    this.root = root;
    this.seeds = seeds;
    this.rand = rand;
  }

  @Override
  public IImage apply() {
    // choose the points in the IImage that will be seeds
    // then initialize seeds with a location and empty average color

    // map a set of coordinates to their seeds, ensure no coords are
    // chosen twice
    Map<Posn, List<IPixel>> seedsToPixels = new HashMap<>();

    for (int i = 0; i < seeds; i++) {
      Posn newKey;
      do {
        // keep trying to generate a new key until we make one that doesn't
        // exist in the map
        newKey = new Posn(rand.nextInt(root.getWidth()),
                rand.nextInt(root.getHeight()));
      }
      while (seedsToPixels.containsKey(newKey));
      seedsToPixels.put(newKey, new ArrayList<>());
      seedsToPixels.get(newKey).add(root.getPixelAt(newKey.getX(), newKey.getY()));

    }

    Posn[] posnSeeds = seedsToPixels.keySet().toArray(new Posn[seeds]);

    // map all pixels to the posn of the closest seed
    for (int i = 0; i < root.getWidth(); i++) {
      for (int j = 0; j < root.getHeight(); j++) {
        // if the pixel already exists as the posn, just ignore
        if (seedsToPixels.containsKey(new Posn(i, j))) {
          continue;
        }
        Posn closest = Mosaic.findClosest(i, j, posnSeeds);
        seedsToPixels.get(closest).add(root.getPixelAt(i, j));
      }
    }

    // then compute the average color of each seed
    IPixel[] pixelsOut = new IPixel[root.getWidth() * root.getHeight()];
    int pixelsIter = 0;
    for (Posn p : posnSeeds) {
      List<IPixel> mappedPixels = seedsToPixels.get(p);

      // compute average here

      Color avgRgb = Mosaic.averageColor(mappedPixels);
      for (IPixel pix : mappedPixels) {
        pixelsOut[pixelsIter] = pix.createPixel(pix.getX(), pix.getY(),
                avgRgb.getRed(), avgRgb.getGreen(), avgRgb.getBlue());
        pixelsIter++;
      }
    }
    // then create an IImage using the mapped pixels and MosaicSeeds
    return root.createImage(root.getWidth(), root.getHeight(), pixelsOut);
  }

  /**
   * Method that computes the closest Posn to the given x y coords.
   * @param x x position of pixel
   * @param y y position of pixel
   * @param posns an array of posns to traverse.
   * @return the closest posn in the given array of Posns to the coordinate.
   * @throws IllegalArgumentException if given null.
   */
  private static Posn findClosest(int x, int y, Posn[] posns)
          throws IllegalArgumentException {
    for (Posn p : posns) {
      if (p == null) {
        throw new IllegalArgumentException("posn is null");
      }
    }

    Posn currPosn = posns[0];
    double lowestDistance = Math.sqrt(Math.pow((currPosn.getX() - x), 2)
            + Math.pow((currPosn.getY() - y), 2));
    Posn currentLowest = currPosn;

    for (int i = 1; i < posns.length; i++) {
      currPosn = posns[i];
      double currDistance = Math.sqrt(Math.pow((currPosn.getX() - x), 2)
              + Math.pow((currPosn.getY() - y), 2));
      if (currDistance < lowestDistance) {
        lowestDistance = currDistance;
        currentLowest = currPosn;
      }
    }
    return currentLowest;
  }

  /**
   * Method that computes the Color object from the average of the
   * given List of IPixels.
   *
   * @param mappedPixels list of IPixel that are in one cluster.
   * @return Color object representing the RGB values of the average
   *                of given pixels.
   */
  private static Color averageColor(List<IPixel> mappedPixels) {
    int numPixels = mappedPixels.size();
    double avgR = 0.0;
    double avgG = 0.0;
    double avgB = 0.0;
    for (IPixel p : mappedPixels) {
      avgR += p.getRed();
      avgG += p.getGreen();
      avgB += p.getBlue();
    }
    return new Color((int) avgR / numPixels,
            (int) avgG / numPixels,
            (int) avgB / numPixels);
  }

  /**
   * Class representation for a 2D position. Only used for Mosaic, so its a private
   * class. Enforces invariant that x and y are not changeable and are >= 0.
   */
  private class Posn {
    private final int x;
    private final int y;

    /**
     * Creates a new Posn object from the x and y.
     * @param x x coord.
     * @param y y coord.
     * @throws IllegalArgumentException if either is less than 0.
     */
    private Posn(int x, int y) {
      if (x < 0 || y < 0) {
        throw new IllegalArgumentException("x or y cannot be less than 0");
      }

      this.x = x;
      this.y = y;
    }

    /**
     * Observer for the posn x coord.
     * @return x coordinate that is at least 0.
     */
    private int getX() {
      return this.x;
    }

    /**
     * Observer for the posn y coord.
     * @return y coordinate that is at least 0.
     */
    private int getY() {
      return this.y;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Posn)) {
        return false;
      }
      Posn other = (Posn) obj;
      return (this.x == other.x) && (this.y == other.y)
              && (this.hashCode() == other.hashCode());
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.x, this.y);
    }
  }
}
