package edu.brown.cs.amukhe12.maps.maps;

import edu.brown.cs.amukhe12.maps.graph.Edge;
import edu.brown.cs.amukhe12.maps.graph.Node;

public class Way implements Edge<MapNode, Way> {

  String _type;
  String _id;
  String _name;
  MapNode _from;
  MapNode _to;
  double _weight;

  public Way(String id, String name, String type) {
    _from = null;
    _to = null;
    _id = id;
    _type = type;
    _weight = 0;
    _name = name;
  }

  @Override
  public MapNode from() {
    return _from;
  }

  @Override
  public MapNode to() {
    return _to;
  }

  public String getName(){
    return _name;
  }

  @Override
  public void setFrom(MapNode u) {
    _from = u;
  }

  @Override
  public void setTo(MapNode v) {
    _to = v;
  }

  @Override
  public double weight() {
    return _weight;
  }

  @Override
  public void setWeight(double w) {
    _weight = w;
  }

  @Override
  public String getId() {
    return _id;
  }

  @Override
  public int compareTo(Edge o) {
    return 0;
  }
}
