package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.graph.Graph;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import edu.brown.cs.amukhe12.maps.maps.Way;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {

  Graph<MapNode, Way> _graph;

  @Before
  public void setUp() throws Exception {

    _graph = new Graph<>();
    List<MapNode> nodes = new ArrayList<>();

    int id = 0;

    for(int i = 0; i<2; i++){
      for(int j = 0; j<2; j++){
        MapNode node = new MapNode(String.valueOf(id), (double) i, (double) j);
        _graph.insertNode(node);
        nodes.add(node);
        id++;
      }
    }

    for (int i = 0; i < 3; i++) {
      Way way = new Way(String.valueOf(i), "Way " + String.valueOf(i), "");
      _graph.insertEdge(nodes.get(i), nodes.get(i + 1), way);
    }
  }

  @After
  public void tearDown() {
    _graph.clearEdges();
  }

  @Test
  public void testClearEdges() throws Exception {
    setUp();
    tearDown();
    assertTrue(_graph.getEdges().isEmpty());
  }

  @Test
  public void testGetEdges() throws Exception {
    setUp();
    assertEquals(_graph.getEdges().size(), 3);
    tearDown();
  }

  @Test
  public void testGetNodes() throws Exception {
    setUp();
    assertEquals(_graph.getNodes().size(), 4);
    tearDown();
  }

  @Test
  public void testContainsNodeWithId() throws Exception {
    setUp();
    assertTrue(_graph.containsNodeWithId("0"));
    tearDown();
  }

  @Test
  public void testGetNodeFromId() throws Exception {
    setUp();
    assertEquals(_graph.getNodeFromId("0"), _graph.getNodeFromId("0"));
    tearDown();
  }

//  @Test
//  public void testDijkstra() throws Exception {
//    setUp();
//    List<Way> ways = new ArrayList<>();
//    for (int i = 0; i < 4; i++) {
//      Way way = new Way(String.valueOf(i), "Way " + String.valueOf(i), "");
//      way.setFrom(new MapNode(String.valueOf(i), (double) (i % 5), (double) (5 / (i + 1))));
//      ways.add(way);
//    }
//
//    MapNode source = new MapNode("0", 0.0, 0.0);
//    MapNode destination = new MapNode("3", 1.0, 1.0);
//
//    System.out.println(_graph.djikstra(source,destination));
//    System.out.println();
//    System.out.println(ways);
//
//    assertEquals(_graph.djikstra(source, destination).size(), 3);
//    tearDown();
//  }

  @Test
  public void testGraphProperty() throws Exception {
    setUp();

    tearDown();
  }

}
