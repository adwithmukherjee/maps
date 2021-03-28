package edu.brown.cs.amukhe12.maps.kdtree;

import java.util.List;

/**
 * KDNode.
 * @param <V> Type
 */
public class KDNode<V> {

  private int dimension;
  private KDNode<V> left;
  private KDNode<V> right;
  private KDNode<V> parent;
  private boolean isRoot;
  private List<Double> coords;
  private V value;


  /**
   * Constructor.
   * @param coordinates coords
   * @param val val
   */
  public KDNode(List<Double> coordinates, V val) {
    coords = coordinates;
    value = val;
    left = null;
    right = null;
  }

  /**
   * Sets the node's left child.
   *
   * @param node node
   */
  public void setLeft(KDNode<V> node) {
    left = node;
  }


  /**
   * @param node set right node
   */
  public void setRight(KDNode<V> node) {
    right = node;
  }

  /**
   * @param node set parent node
   */
  public void setParent(KDNode<V> node) {
    parent = node;
  }

  /**
   * @param dimension set dimension
   */
  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  /**
   * @return dimension
   */
  public int getDimension() {
    return dimension;
  }

  /**
   * @return left-tree node
   */
  public KDNode<V> getLeft() {
    return left;
  }

  /**
   * @return right-tree node
   */
  public KDNode<V> getRight() {
    return right;
  }

  /**
   * @return parent node
   */
  public KDNode<V> getParent() {
    return parent;
  }

  /**
   * @return coords
   */
  public List<Double> getCoords() {
    return coords;
  }

  /**
   * @return value
   */
  public V getValue() {
    return value;
  }

  /**
   * @return true, if has a right tree
   */
  public boolean hasRight() {
    return right != null;
  }

  /**
   * @return true, if has a left tree
   */
  public boolean hasLeft() {
    return left != null;
  }

  /**
   * distanceTo.
   * @param otherNode other Node.
   * @return distance to other node.
   */
  public double distanceTo(KDNode<V> otherNode) {
    double sum = 0;
    for (int i = 0; i < coords.size(); i++) {
      sum = sum + Math.pow(this.getCoords().get(i) - (double) otherNode.getCoords().get(i), 2);
    }

    return Math.sqrt(sum);
  }


}
