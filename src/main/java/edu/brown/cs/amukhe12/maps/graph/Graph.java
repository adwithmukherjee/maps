package edu.brown.cs.amukhe12.maps.graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Graph<N extends Node, E extends Edge> {


  private HashMap<String, N> _nodes;
  private HashMap<String, E> _edges;


  public Graph() {
    _nodes = new HashMap<>();
    _edges = new HashMap<>();
  }

  /**
   * removes all of the edges from the Graph.
   */
  public void clearEdges() {
    _edges = new HashMap<>();
    for (N node : _nodes.values()) {
      node.clearEdges();
    }
  }

  /**
   * returns whether or not the Graph contains a Node of the given id.
   *
   * @param id - the id of the Node to look for
   * @return - whether the Graph contains the Node
   */
  public boolean containsNodeWithId(String id) {
    return _nodes.get(id) != null;
  }

  public boolean containsEdgeWithId(String id) {
    return _edges.get(id) != null;
  }

  public void insertNode(N u) throws Exception {
    if (_nodes.get(u.getId()) != null) {
      throw new Exception("Node already exists in Graph");
    }
    _nodes.put(u.getId(), u);
  }

  /**
   * Inserts Edge e from Node u to v.
   *
   * @param u - the "from" Node
   * @param v - the "to" Node
   * @param e - the edge
   */
  public void insertEdge(N u, N v, E e) throws Exception {
    if (!this.containsNodeWithId(u.getId()) || !this.containsNodeWithId(v.getId())) {
      throw new Exception("No such Node found when inserting Edge");
    }
    if (this.containsEdgeWithId(e.getId())) {
      throw new Exception("Edge already exists in Graph");
    }
    _edges.put(e.getId(), e);
    e.setFrom(u);
    e.setTo(v);
    u.addOutEdge(e);
    v.addInEdge(e);
  }

  /**
   * gets a Node that has the given id.
   *
   * @param _id - the id of the Node to look for
   * @return - the Node with the given id
   */
  public N getNodeFromId(String _id) {
    return _nodes.get(_id);
  }

  /**
   * Returns an ordered list of Edges representing cheapest path from Node u (source) to Node v.
   *
   * @param u - the "from" Node
   * @param v - the "to" Node
   * @return - the set of edges that connect the two Nodes
   */
  public List<E> djikstra(N u, N v) {

    PriorityQueue<PQEntry<N>> pq = new PriorityQueue<>();
    HashMap<N, PQEntry<N>> nodeToEntries = new HashMap();
    HashMap<PQEntry<N>, PQEntry<N>> prevPointer = new HashMap<>();
    HashMap<PQEntry<N>, E> pointerEdges = new HashMap<>();

    for (N node : _nodes.values()) {
      PQEntry<N> entry;

      if (node == u) {
        entry = new PQEntry<N>(0, node);
      } else {
        entry = new PQEntry<N>(Double.MAX_VALUE, node);
      }
      pq.add(entry);
      nodeToEntries.put(node, entry);

    }


    while (!pq.isEmpty()) {


      PQEntry<N> source = pq.poll();
      Collection<E> outEdges = source.getValue().getOutEdges();
      for (E edge : outEdges) {
        PQEntry<N> endNode = nodeToEntries.get(edge.to());
        Double sourceDist = source.getKey();
        Double edgeWeight = edge.weight();
        Double endDist = endNode.getKey();

        if (sourceDist + edgeWeight < endDist) {
          N endNodeValue = endNode.getValue();
          pq.remove(endNode);
          PQEntry<N> newEntry = new PQEntry<N>(sourceDist + edgeWeight, endNodeValue);
          pq.add(newEntry);
          nodeToEntries.put(endNodeValue, newEntry);

          prevPointer.put(newEntry, source);
          pointerEdges.put(newEntry, edge);
        }
      }
    }
    //BACKTRACKING
    List<E> shortestPath = new ArrayList<>();
    PQEntry<N> end = nodeToEntries.get(v);
    while (prevPointer.get(end) != null) {

      if (pointerEdges.get(end) != null) {
        shortestPath.add(pointerEdges.get(end));
      }
      if (prevPointer.get(end) != null) {
        end = prevPointer.get(end);
      }
    }
    Collections.reverse(shortestPath);

    return shortestPath;
  }

  public HashMap<String, E> getEdges() {
    return _edges;
  }

  public HashMap<String, N> getNodes() {
    return _nodes;
  }


}
