package edu.brown.cs.amukhe12.maps.maps;

import edu.brown.cs.amukhe12.maps.graph.Edge;

/**
 * Way.
 */
public class Way implements Edge<MapNode, Way> {

  private String type;
  private String id;
  private String name;
  private MapNode from;
  private MapNode to;
  private double weight;

  /**
   * Constructor.
   * @param wayId  id
   * @param name name
   * @param wayType type
   */
  public Way(String wayId, String name, String wayType) {
    from = null;
    to = null;
    id = wayId;
    type = wayType;
    weight = 0;
    name = name;
  }

  @Override
  public MapNode from() {
    return from;
  }

  @Override
  public MapNode to() {
    return to;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  @Override
  public void setFrom(MapNode u) {
    from = u;
  }

  @Override
  public void setTo(MapNode v) {
    to = v;
  }

  @Override
  public double weight() {
    return weight;
  }

  @Override
  public void setWeight(double w) {
    weight = w;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int compareTo(Edge o) {
    return 0;
  }
}
