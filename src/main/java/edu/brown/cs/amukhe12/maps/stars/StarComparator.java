package edu.brown.cs.amukhe12.maps.stars;

import java.util.Comparator;

/**
 * Used to compare the distances of two Stars from an origin point.
 */

public class StarComparator implements Comparator<Star> {

  private double x1;
  private double y1;
  private double z1;

  /**
   * constructor.
   * @param xOrigin x
   * @param yOrigin y
   * @param zOrigin z
   */
  public StarComparator(double xOrigin, double yOrigin, double zOrigin) {
    x1 = xOrigin;
    y1 = yOrigin;
    z1 = zOrigin;
  }


  @Override
  public int compare(Star o1, Star o2) {

    double distTo1 = Math.sqrt(Math.pow(o1.getX() - x1, 2) + Math.pow(o1.getY() - y1, 2)
        + Math.pow(o1.getZ() - z1, 2));
    double distTo2 = Math.sqrt(Math.pow(o2.getX() - x1, 2) + Math.pow(o2.getY() - y1, 2)
        + Math.pow(o2.getZ() - z1, 2));

    if (distTo1 < distTo2) {
      return -1;
    } else if (distTo1 > distTo2) {
      return 1;
    } else {
      return 0;
    }
  }
}
