package edu.brown.cs.amukhe12.maps.graph;

/**Edge.
 * @param <N> Node type.
 * @param <E> Edge Type.
 */
public interface Edge<N extends Node<N, E>, E extends Edge<N, E>> extends Comparable<Edge> {
  /**
   * gets the Node that this edge comes from.
   * @return source node.
   */
  N from();

  /**
   * gets the Node that this edge leads to.
   * @return dest node.
   */
  N to();

  /**
   * adds a Node that this edge comes from.
   * @param u - the "from" Node to add
   */
  void setFrom(N u);

  /**
   * adds a Node that this edge leads to.
   * @param v - the "to" Node to add
   */
  void setTo(N v);

  /**
   * gets the weight of this edge.
   * @return weight
   */
  double weight();

  /**
   * sets the weight of this edge.
   * @param w - the weight to set the edge to
   */
  void setWeight(double w);

  /**
   * gets the id of this edge.
   * @return id
   */
  String getId();
}
