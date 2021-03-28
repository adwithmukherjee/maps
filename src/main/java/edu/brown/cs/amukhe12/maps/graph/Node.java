package edu.brown.cs.amukhe12.maps.graph;

import java.util.Collection;

public interface Node<N extends Node<N, E>, E extends Edge<N, E>> {
  /**
   * getOutEdges.
   * @return a Collection of all edges leading from this Node to another Node.
   */
  Collection<E> getOutEdges();

  /**
   * getInEdges.
   * @return a Collection of all edges leading to this Node to another Node.
   */
  Collection<E> getInEdges();

  /**
   * adds an edge leading from this Node to another Node.
   * @param e - the edge to add
   */
  void addOutEdge(E e);

  /**
   * adds an edge leading to this Node to another Node.
   * @param e - the edge to add
   */
  void addInEdge(E e);

  /**
   * getId.
   * @return the id of this Node.
   */
  String getId();

  /**
   * clears all of the edges associated with this Node.
   */
  void clearEdges();
}
