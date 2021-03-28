package edu.brown.cs.amukhe12.maps.kdtree;


import java.util.Comparator;


/**
 * NodeComparator.
 * @param <V> Type.
 */
public class NodeComparator<V> implements Comparator<KDNode<V>> {

  private int dimToCompare;

  /**
   * Constructor.
   * @param dimension dim
   */
  public NodeComparator(int dimension) {
    dimToCompare = dimension;
  }

  /**
   * @param dimension dim
   */
  public void setDimensionToCompare(int dimension) {
    dimToCompare = dimension;
  }

  @Override
  public int compare(KDNode<V> o1, KDNode<V> o2) {
    if ((double) o1.getCoords().get(dimToCompare - 1)
        < o2.getCoords().get(dimToCompare - 1)) {
      return -1;
    } else if ((double) o1.getCoords().get(dimToCompare - 1)
        > o2.getCoords().get(dimToCompare - 1)) {
      return 1;
    } else {
      return 0;
    }
  }
}
