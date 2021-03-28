package edu.brown.cs.amukhe12.maps.maps;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.kdtree.KDTree;

import java.util.Arrays;
import java.util.List;

/**
 * MapTree.
 */
public class MapTree implements EntryList {

  private KDTree<MapNode> mapNodes;
  private MapGraph mapGraph;

  /**
   * @param graph graph
   */
  public MapTree(MapGraph graph) {
    mapGraph = graph;
    mapNodes = new KDTree(2);
  }

  @Override
  public void addEntry(List<String> fields) throws Exception {
    String nodeId = fields.get(0);
    MapNode node = new MapNode(nodeId, Double.parseDouble(fields.get(1)),
        Double.parseDouble(fields.get(2)));
    mapNodes.insert(new KDNode<>(node.getCoords(), node));
    mapGraph.insertNode(node);
  }

  /**
   * nearest.
   * @param number number of nearest to return
   * @param latitude lat
   * @param longitude lon
   * @return nearest number of nodes to lat and lon
   */
  public List<KDNode<MapNode>> nearest(int number, double latitude, double longitude) {
    List<KDNode<MapNode>> nearest =
        mapNodes.nearestNeighbors(number, Arrays.asList(latitude, longitude));
    return nearest;
  }


  @Override
  public void clear() {
    mapNodes.clear();
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
