package edu.brown.cs.amukhe12.maps.kdtree;

import edu.brown.cs.amukhe12.maps.kdtree.KDNode;

import java.util.Comparator;


public class NodeComparator<V> implements Comparator<KDNode<V>> {

  private int dimToCompare;

  public NodeComparator(int dimension) {
    dimToCompare = dimension;
  }

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
