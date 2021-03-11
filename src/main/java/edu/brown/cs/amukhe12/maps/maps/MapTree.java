package edu.brown.cs.amukhe12.maps.maps;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.kdtree.KDTree;

import java.util.Arrays;
import java.util.List;

public class MapTree implements EntryList {

  private KDTree<MapNode> _mapNodes;
  private MapGraph _mapGraph;

  public MapTree(MapGraph mapGraph){
    _mapGraph = mapGraph;
    _mapNodes = new KDTree(2);
  }

  @Override
  public void addEntry(List<String> fields) throws Exception {
    String nodeId = fields.get(0);
    MapNode node = new MapNode(nodeId, Double.parseDouble(fields.get(1)),
        Double.parseDouble(fields.get(2)));
    _mapNodes.insert(new KDNode<>(node.getCoords(), node));
    _mapGraph.insertNode(node);
  }

  public List<KDNode<MapNode>> nearest(int number, double latitude, double longitude) {
    List<KDNode<MapNode>> nearest =
        _mapNodes.nearestNeighbors(number, Arrays.asList(latitude, longitude));
    return nearest;
  }


  @Override
  public void clear() {
    _mapNodes.clear();
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
