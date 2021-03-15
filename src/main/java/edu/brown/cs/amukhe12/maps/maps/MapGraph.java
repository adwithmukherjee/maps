package edu.brown.cs.amukhe12.maps.maps;


import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.graph.APQEntry;
import edu.brown.cs.amukhe12.maps.graph.Graph;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class MapGraph implements EntryList {

  private Graph<MapNode, Way> _graph;
  private DBReference _db;

  public MapGraph(DBReference db) {
    _db = db;
    _graph = new Graph<>();
  }


  public void clearEdges() {
    _graph.clearEdges();
  }

  public void insertNode(MapNode u) throws Exception {
    _graph.insertNode(u);
  }

  /**
   * this is going to be called in SQLParser to populate the stuff in the map.
   *
   * @param fields
   * @throws Exception
   */

  @Override
  public void addEntry(List<String> fields) throws Exception { //for adding edges from SQL

    Way way = new Way(fields.get(0), fields.get(1), fields.get(2));
    String fromId = fields.get(3);
    String toId = fields.get(4);
    MapNode from;
    MapNode to;
    from = _graph.getNodeFromId(fromId);
    to = _graph.getNodeFromId(toId);
    way.setWeight(from.distanceTo(to));
    _graph.insertEdge(from, to, way);

  }

//  public List<Way> aStar(MapNode u, MapNode v) {
//    PriorityQueue<PQEntry<MapNode>> pq = new PriorityQueue<>();
//    HashMap<MapNode, PQEntry<MapNode>> nodeToEntries = new HashMap();
//    HashMap<PQEntry<MapNode>, PQEntry<MapNode>> prevPointer = new HashMap<>();
//    HashMap<PQEntry<MapNode>, Way> pointerEdges = new HashMap<>();
//
//    for (MapNode node : _graph.getNodes().values()) {
//      PQEntry<MapNode> entry;
//      if (node == u) {
//        entry = new PQEntry(0, node);
//      } else {
//        entry = new PQEntry(Double.MAX_VALUE, node);
//      }
//      pq.add(entry);
//      nodeToEntries.put(node, entry);
//    }
//
//    while (!pq.isEmpty()) {
//      PQEntry<MapNode> source = pq.poll();
//      Collection<Way> outEdges = source.getValue().getOutEdges();
//      for (Way edge : outEdges) {
//        PQEntry<MapNode> endNode = nodeToEntries.get(edge.to());
//        Double sourceDist = source.getKey();
//        Double edgeWeight = edge.weight();
//        Double endDist = endNode.getKey() + endNode.getValue().distanceTo(u);
//        Double distFromDestination = endNode.getValue().distanceTo(u);
//        if (sourceDist + edgeWeight + distFromDestination < endDist) {
//          MapNode endNodeValue = endNode.getValue();
//          pq.remove(endNode);
//          PQEntry<MapNode> newEntry =
//              new PQEntry(sourceDist + edgeWeight, endNodeValue);
//          pq.add(newEntry);
//          nodeToEntries.put(endNodeValue, newEntry);
//          prevPointer.put(newEntry, source);
//          pointerEdges.put(newEntry, edge);
//        }
//      }
//    }
//    //BACKTRACKING
//
//
//
//    List<Way> shortestPath = new ArrayList<>();
//    PQEntry<MapNode> end = nodeToEntries.get(v);
//    while (prevPointer.get(end) != null) {
//      if (pointerEdges.get(end) != null) {
//        shortestPath.add(pointerEdges.get(end));
//      }
//      if (prevPointer.get(end) != null) {
//        end = prevPointer.get(end);
//      }
//    }
//    Collections.reverse(shortestPath);
//
//    return shortestPath;
//  }

  public List<Way> aStar(MapNode u, MapNode v) throws Exception {
    PriorityQueue<APQEntry> pq = new PriorityQueue<>();
    HashMap<MapNode, APQEntry> nodeToEntries = new HashMap();
    HashMap<APQEntry, APQEntry> prevPointer = new HashMap<>();
    HashMap<APQEntry, Way> pointerEdges = new HashMap<>();
    //HashSet<APQEntry> unvisited = new HashSet<>(); //concurrent with pq

    for (MapNode node : _graph.getNodes().values()) {
      APQEntry entry;
      if (node == u) {
        entry = new APQEntry(0, node, v);
        pq.add(entry);
        nodeToEntries.put(node, entry);
      }
//      } else {
//        entry = new APQEntry(Double.MAX_VALUE, node, v);
//      }

    }
    //ASSUMPTIONS ABOUT GET OUT EDGES:
    //  each edge assigned a weight that can be called
    //  assume each edges' to and from is set

    while (!pq.isEmpty()) {
      APQEntry currentNode = pq.poll();
      currentNode.visit();

      this.addOutEdges(currentNode.getValue());
      //insertEdge

      Collection<Way> outEdges = currentNode.getValue().getOutEdges();
      for(Way e: outEdges){
        e.setFrom(currentNode.getValue());

        APQEntry endNode;
        if( nodeToEntries.containsKey(e.to())){
          endNode = nodeToEntries.get(e.to());
        } else {
          endNode = new APQEntry(Double.POSITIVE_INFINITY, e.to(), v);
          nodeToEntries.put(e.to(), endNode);
        }
        //APQEntry endNode = nodeToEntries.get(e.to());
        //MapNode endNode =
        double newEstimate = currentNode.getKey() + e.weight();

        if(newEstimate < endNode.getKey()){

          APQEntry newEntry = new APQEntry(newEstimate, endNode.getValue(), v);

          if(!newEntry.visited()) {
            pq.remove(endNode);
            pq.add(newEntry);
            nodeToEntries.put(endNode.getValue(), newEntry);
            prevPointer.put(newEntry, currentNode);
            pointerEdges.put(newEntry, e);
          }
        }
      }

      if(currentNode.getValue().getId().equals(v.getId())){
        break;
      }
    }

    List<Way> shortestPath = new ArrayList<>();
    APQEntry end = nodeToEntries.get(v);
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


  public MapNode getNodeFromId(String id) {
    return _graph.getNodeFromId(id);
  }

  public List<Way> routeFromNodeIds(String id1, String id2) throws Exception {

    MapNode u = _graph.getNodeFromId(id1);
    MapNode v = _graph.getNodeFromId(id2);
    List<Way> route = aStar(u, v);//_graph.djikstra(u,v);// //could also do _graph.djikstra

    return route;
  }

  public Graph<MapNode, Way> getGraph() {
    return _graph;
  }

  public void addOutEdges(MapNode u) throws Exception {

    List<List<String>> fields = new SQLParser(_db.getFilename(), null).parseAndReturnList(SQLQueries.getOutEdges(u.getId()));
    for (List<String> wayField: fields){
      Way newWay = new Way(wayField.get(0), wayField.get(1), wayField.get(2));
      MapNode v = _graph.getNodes().get(wayField.get(3));
      newWay.setWeight(u.distanceTo(v));
      _graph.insertEdge(u, v, newWay);
    }

  }

  @Override
  public void clear() {
  }

  @Override
  public int size() {
    return _graph.getNodes().size();
  }

  @Override
  public boolean isEmpty() {
    return _graph.getNodes().isEmpty();
  }

  @Override
  public String toString() {
    return _graph.toString();
  }
}
