package edu.brown.cs.amukhe12.maps.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * KDTree.
 *
 * @param <V>
 */

public class KDTree<V> {

  private List<KDNode<V>> _nodes;
  private int _dimensions;
  private KDNode<V> _root;
  private int _size;

  public KDTree(int dimensions) {
    _dimensions = dimensions;
    _nodes = new ArrayList<>();
    _size = 0;
  }

  public void clear() {
    _nodes.clear();
    _size = 0;
    _root = null;
  }

  public void setRoot(KDNode<V> root) {
    _root = root;
  }

  public KDNode<V> getRoot() {
    return _root;
  }

  /**
   * Helper method to add given node to list in sorted distance order.
   *
   * @param list
   * @param node
   * @param target
   */

  private void addInOrder(List<KDNode<V>> list, KDNode<V> node, KDNode<V> target) {

    int i = 0;
    while (i < list.size() && target.distanceTo(node) > target.distanceTo(list.get(i))) {
      i++;
    }
    list.add(i, node);


  }

  /**
   * Inserts given KDNode in KDTree as per tree properties.
   *
   * @param node
   */
  public void insert(KDNode<V> node) {

    if (this.isEmpty()) {
      node.setDimension(1);
      this.setRoot(node);
      this.addNode(node);
      return;
    }

    KDNode<V> start = this.getRoot();

    while (true) {

      int dimension = start.getDimension();
      int nextDimension = (dimension) % _dimensions + 1;

      if (new NodeComparator<V>(dimension).compare(node, start) > 0) {
        if (start.hasLeft()) {
          start = start.getLeft();
        } else {
          node.setDimension(nextDimension);
          start.setLeft(node);
          this.addNode(node);
          break;
        }
      } else {
        if (start.hasRight()) {
          start = start.getRight();
        } else {
          node.setDimension(nextDimension);
          start.setRight(node);
          this.addNode(node);
          break;
        }
      }


    }
  }

  /**
   * Executes a search on the KDTree and returns a list of closest KDNodes to given coords.
   *
   * @param capacity
   * @param coords
   * @return List of nearest KDNodes.
   */

  public List<KDNode<V>> nearestNeighbors(int capacity, List<Double> coords) {

    KDNode<V> target = new KDNode<V>(coords, null);
    List<KDNode<V>> neighbors = new ArrayList<>();

    if (capacity == 0) {
      return neighbors;
    }

    Stack<KDNode<V>> stack = new Stack();
    stack.push(_root);

    while (!stack.isEmpty()) {

      KDNode<V> node = stack.pop();

      if (neighbors.isEmpty()) {
        this.addInOrder(neighbors, node, target);

        if (node.hasLeft()) {
          stack.push(node.getLeft());
        }
        if (node.hasRight()) {
          stack.push(node.getRight());
        }
      } else {

        int farthest = neighbors.size() - 1;
        double maxDist = target.distanceTo(neighbors.get(farthest));

        int nodeDim = node.getDimension();

        if (!node.hasLeft() && !node.hasRight()) { //BASE CASE
          if (neighbors.size() < capacity) {
            this.addInOrder(neighbors, node, target);
            //neighbors.add(node);
          } else if (target.distanceTo(node) < maxDist) {
            neighbors.remove(farthest);
            this.addInOrder(neighbors, node, target);
          }
        } else if (neighbors.size() < capacity) {   //IF neighbors NOT FULL
          this.addInOrder(neighbors, node, target);
          if (node.hasLeft()) {
            stack.push(node.getLeft());
          }
          if (node.hasRight()) {
            stack.push(node.getRight());
          }
        } else if (target.distanceTo(node) < maxDist) {  //IF n
          neighbors.remove(farthest);
          this.addInOrder(neighbors, node, target);
          if (node.hasLeft()) {
            stack.push(node.getLeft());
          }
          if (node.hasRight()) {
            stack.push(node.getRight());
          }
        } else if (Math.abs((double) node.getCoords().get(nodeDim - 1)
            - (double) target.getCoords().get(nodeDim - 1)) < maxDist) {
          if (node.hasLeft()) {
            stack.push(node.getLeft());
          }
          if (node.hasRight()) {
            stack.push(node.getRight());
          }
        } else {
          if (new NodeComparator<V>(nodeDim).compare(target, node) > 0) {
            if (node.hasLeft()) {
              stack.push(node.getLeft());
            }
          } else {
            if (node.hasRight()) {
              stack.push(node.getRight());
            }
          }
        }
      }
    }

    return neighbors;
  }

  /**
   * Executes search on KDTree and returns List of KDNodes less than radius away from given coords.
   *
   * @param radius
   * @param coords
   * @return List of KDNodes in given radius.
   */
  public List<KDNode<V>> radius(double radius, List<Double> coords) {

    KDNode<V> target = new KDNode<V>(coords, null);
    List<KDNode<V>> neighbors = new ArrayList<>();

    Stack<KDNode<V>> stack = new Stack();
    stack.push(_root);

    while (!stack.isEmpty()) {

      KDNode<V> node = stack.pop();

      int nodeDim = node.getDimension();

      if (!node.hasLeft() && !node.hasRight()) { //BASE CASE
        if (target.distanceTo(node) <= radius) {
          this.addInOrder(neighbors, node, target);
        }
      } else if (target.distanceTo(node) <= radius) {  //IF n
        this.addInOrder(neighbors, node, target);
        if (node.hasLeft()) {
          stack.push(node.getLeft());
        }
        if (node.hasRight()) {
          stack.push(node.getRight());
        }
      } else if (Math.abs((double) node.getCoords().get(nodeDim - 1)
          - (double) target.getCoords().get(nodeDim - 1)) <= radius) {
        if (node.hasLeft()) {
          stack.push(node.getLeft());
        }
        if (node.hasRight()) {
          stack.push(node.getRight());
        }
      } else {
        if (new NodeComparator<V>(nodeDim).compare(target, node) > 0) {
          if (node.hasLeft()) {
            stack.push(node.getLeft());
          }
        } else {
          if (node.hasRight()) {
            stack.push(node.getRight());
          }
        }
      }
    }

//
    return neighbors;
  }


  public void addNode(KDNode<V> node) {
    _nodes.add(node);
    _size += 1;
  }

  public boolean isEmpty() {
    return _size == 0;
  }

  public int size() {
    return _size;
  }

  public List<KDNode<V>> getNodes() {
    return _nodes;
  }


}
