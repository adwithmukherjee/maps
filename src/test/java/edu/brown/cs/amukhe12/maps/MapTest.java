package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.MapGraph;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import edu.brown.cs.amukhe12.maps.maps.MapTree;
import edu.brown.cs.amukhe12.maps.maps.Way;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MapTest {

  DBReference _ref;

  @Before
  public void setUp(){

    _ref = new DBReference();
    _ref.initializeTreeAndGraph();

  }

  @After
  public void cleanUp(){
    _ref = null;
  }

  //@Test
  public void insertTest() throws Exception {
    setUp();
    MapTree tree = _ref.getTree();
    MapGraph graph = _ref.getGraph();
    String[] ids = new String[]{"0","1", "2", "3", "4", "5"};
    List<String> latitudes = Arrays.asList("41.82", "41.8203", "41.8206", "41.82", "41.8203", "41.8206");
    List<String> longitudes = Arrays.asList("-71.4", "-71.4","-71.4","-71.4003","-71.4003","-71.4003");
    for(int i =0; i<latitudes.size(); i++){
      tree.addEntry(Arrays.asList(ids[i], latitudes.get(i), longitudes.get(i)));
    }

    assertTrue(graph.getGraph().containsNodeWithId("0"));
    assertTrue(graph.getGraph().getEdges().values().isEmpty());

    List<String> edgeIds = Arrays.asList("0", "1", "2", "3", "4", "5", "6");
    List<String> names = Arrays.asList("edge0","edge1","edge2","edge3","edge4","edge5","edge6");
    List<String> start = Arrays.asList("0","1","0","1","2","3","4");
    List<String> end = Arrays.asList("1","2","3","4","5","4","5");
    for(int i = 0; i<edgeIds.size(); i++){
      graph.addEntry(Arrays.asList(edgeIds.get(i), names.get(i), "residential", start.get(i), end.get(i)));
    }
    List<Way> routes = graph.routeFromNodeIds("0","5");
    assertTrue(routes.size() == 3);
    assertTrue(routes.get(0).getId().equals("0"));
    assertTrue(routes.get(1).getId().equals("1"));
    assertTrue(routes.get(2).getId().equals("4"));
    List<Way> aStar = graph.aStar(graph.getNodeFromId("0"), graph.getNodeFromId("5"));
    assertTrue(aStar.size() == 3);

    cleanUp();
  }

  @Test
  public void nearestTest() throws Exception {
    MapTree tree = _ref.getTree();

    String[] ids = new String[]{"0","1", "2", "3", "4", "5"};
    List<String> latitudes = Arrays.asList("41.82", "41.8203", "41.8206", "41.82", "41.8203", "41.8206");
    List<String> longitudes = Arrays.asList("-71.4", "-71.4","-71.4","-71.4003","-71.4003","-71.4003");
    for(int i =0; i<latitudes.size(); i++){
      tree.addEntry(Arrays.asList(ids[i], latitudes.get(i), longitudes.get(i)));
    }

    List<KDNode<MapNode>> nearest =  tree.nearest(1,41.8 ,-71.3);
    assertTrue(nearest.get(0).getValue().getId().equals("0"));
  }



}
