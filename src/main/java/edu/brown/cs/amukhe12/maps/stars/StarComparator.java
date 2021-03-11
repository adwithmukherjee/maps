package edu.brown.cs.amukhe12.maps.stars;

import java.util.Comparator;

/**
 * Used to compare the distances of two Stars from an origin point.
 */

public class StarComparator implements Comparator<Star> {

  private double _x1;
  private double _y1;
  private double _z1;

  public StarComparator(double xOrigin, double yOrigin, double zOrigin) {
    _x1 = xOrigin;
    _y1 = yOrigin;
    _z1 = zOrigin;
  }


  @Override
  public int compare(Star o1, Star o2) {

    double distTo1 = Math.sqrt(Math.pow(o1.getX() - _x1, 2) + Math.pow(o1.getY() - _y1, 2)
        + Math.pow(o1.getZ() - _z1, 2));
    double distTo2 = Math.sqrt(Math.pow(o2.getX() - _x1, 2) + Math.pow(o2.getY() - _y1, 2)
        + Math.pow(o2.getZ() - _z1, 2));

    if (distTo1 < distTo2) {
      return -1;
    } else if (distTo1 > distTo2) {
      return 1;
    } else {
      return 0;
    }
  }
}
