package edu.brown.cs.amukhe12.maps.kdtree;

import java.util.List;

public class KDNode<V> {

  private int _dimension;
  private KDNode<V> _left;
  private KDNode<V> _right;
  private KDNode<V> _parent;
  private boolean _isRoot;
  private List<Double> _coords;
  private V _value;


  public KDNode(List<Double> coords, V value) {
    _coords = coords;
    _value = value;
    _left = null;
    _right = null;
  }

  /**
   * Sets the node's left child.
   *
   * @param node
   */
  public void setLeft(KDNode<V> node) {
    _left = node;
  }


  public void setRight(KDNode<V> node) {
    _right = node;
  }

  public void setParent(KDNode<V> node) {
    _parent = node;
  }

  public void setDimension(int dimension) {
    this._dimension = dimension;
  }

  public int getDimension() {
    return _dimension;
  }

  public KDNode<V> getLeft() {
    return _left;
  }

  public KDNode<V> getRight() {
    return _right;
  }

  public KDNode<V> getParent() {
    return _parent;
  }

  public List<Double> getCoords() {
    return _coords;
  }

  public V getValue() {
    return _value;
  }

  public boolean hasRight() {
    return _right != null;
  }

  public boolean hasLeft() {
    return _left != null;
  }

  public double distanceTo(KDNode<V> otherNode) {
    double sum = 0;
    for (int i = 0; i < _coords.size(); i++) {
      sum = sum + Math.pow(this.getCoords().get(i) - (double) otherNode.getCoords().get(i), 2);
    }

    return Math.sqrt(sum);
  }


}
