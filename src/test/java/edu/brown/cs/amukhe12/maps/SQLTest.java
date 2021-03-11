package edu.brown.cs.amukhe12.maps;


import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.MapGraph;
import edu.brown.cs.amukhe12.maps.maps.MapTree;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.maps.Way;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SQLTest {

  DBReference _ref;

  @Before
  public void setUp() throws Exception {

    _ref = new DBReference();
    _ref.setFilename("data/maps/smallMaps.sqlite3");
    _ref.initializeTreeAndGraph();

  }

  @After
  public void cleanUp() {
    _ref = null;
  }

  @Test
  public void testParser() throws Exception {
    setUp();
    MapTree tree = _ref.getTree();
    MapGraph graph = _ref.getGraph();
    new SQLParser(_ref.getFilename(), tree).parse(SQLQueries.SELECT_ALL_TRAVERSABLE_NODES);

    assertTrue(graph.getGraph().containsNodeWithId("/n/0"));
    assertTrue(graph.getGraph().containsNodeWithId("/n/1"));
    assertTrue(graph.getGraph().containsNodeWithId("/n/2"));
    assertTrue(graph.getGraph().containsNodeWithId("/n/3"));
    assertTrue(graph.getGraph().containsNodeWithId("/n/3"));

    new SQLParser(_ref.getFilename(), graph).parse(SQLQueries.SELECT_ALL_EDGES);

    assertTrue(graph.getGraph().containsEdgeWithId("/w/0"));
    assertTrue(graph.getGraph().containsEdgeWithId("/w/1"));
    assertTrue(graph.getGraph().containsEdgeWithId("/w/2"));
    assertTrue(graph.getGraph().containsEdgeWithId("/w/3"));

    List<List<String>> output =
        new SQLParser(_ref.getFilename(), graph).parseAndReturnList(SQLQueries.SELECT_ALL_EDGES);


    cleanUp();
  }

  @Test
  public void testRawParser() throws Exception {

    List<List<String>> output =
        new SQLParser(_ref.getFilename(), null).parseAndReturnList(SQLQueries.SELECT_ALL_EDGES);
    assertTrue(output.get(0).get(0).equals("/w/0"));
    assertTrue(output.get(0).get(1).equals("Chihiro Ave"));

  }


}
