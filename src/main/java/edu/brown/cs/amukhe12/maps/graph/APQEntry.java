package edu.brown.cs.amukhe12.maps.graph;

import edu.brown.cs.amukhe12.maps.maps.MapNode;

public class APQEntry implements Comparable<APQEntry> {

  private Double key;
  private MapNode node;
  private MapNode target;
  private boolean visited;

  public APQEntry(double entryKey, MapNode mapNode, MapNode targetNode) {
    key = entryKey;
    node = mapNode;
    target = targetNode;
    visited = false;
  }

  @Override
  public int compareTo(APQEntry o) {
    Double cost1 = this.getKey() + this.getValue().distanceTo(target);
    Double cost2 = o.getKey() + o.getValue().distanceTo(target);
    return cost1.compareTo(cost2);
  }

  public void setKey(double key) {
    key = key;
  }

  public Double getKey() {
    return key;
  }

  public MapNode getValue() {
    return node;
  }

  public boolean visited() {
    return visited;
  }

  public void visit() {
    visited = true;
  }

}
