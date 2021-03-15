package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.maps.MapGraph;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapGraphTest {

  MapGraph _mapGraph;

  @Before
  public void setUp() throws Exception {
    _mapGraph = new MapGraph(null);
    _mapGraph.insertNode(new MapNode("0", 0.0, 0.0));
  }

  @After
  public void tearDown() {
    _mapGraph.clear();
    _mapGraph.clearEdges();
  }

  @Test
  public void testIsEmpty() throws Exception {
    setUp();
    assertFalse(_mapGraph.isEmpty());
    tearDown();
  }

  @Test
  public void testSize() throws Exception {
    setUp();
    assertEquals(_mapGraph.size(), 1);
    tearDown();
  }

}
