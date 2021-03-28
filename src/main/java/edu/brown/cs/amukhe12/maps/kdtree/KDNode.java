package edu.brown.cs.amukhe12.maps.kdtree;

import java.util.List;

public class KDNode<V> {

  private int dimension;
  private KDNode<V> left;
  private KDNode<V> right;
  private KDNode<V> parent;
  private boolean isRoot;
  private List<Double> coords;
  private V value;


  public KDNode(List<Double> coordinates, V val) {
    coords = coordinates;
    value = val;
    left = null;
    right = null;
  }

  /**
   * Sets the node's left child.
   *
   * @param node
   */
  public void setLeft(KDNode<V> node) {
    left = node;
  }


  public void setRight(KDNode<V> node) {
    right = node;
  }

  public void setParent(KDNode<V> node) {
    parent = node;
  }

  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  public int getDimension() {
    return dimension;
  }

  public KDNode<V> getLeft() {
    return left;
  }

  public KDNode<V> getRight() {
    return right;
  }

  public KDNode<V> getParent() {
    return parent;
  }

  public List<Double> getCoords() {
    return coords;
  }

  public V getValue() {
    return value;
  }

  public boolean hasRight() {
    return right != null;
  }

  public boolean hasLeft() {
    return left != null;
  }

  public double distanceTo(KDNode<V> otherNode) {
    double sum = 0;
    for (int i = 0; i < coords.size(); i++) {
      sum = sum + Math.pow(this.getCoords().get(i) - (double) otherNode.getCoords().get(i), 2);
    }

    return Math.sqrt(sum);
  }


}
