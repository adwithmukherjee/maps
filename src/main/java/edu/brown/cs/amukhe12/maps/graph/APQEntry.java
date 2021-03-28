package edu.brown.cs.amukhe12.maps.graph;

import edu.brown.cs.amukhe12.maps.maps.MapNode;

/**
 * APQEntry.
 */
public class APQEntry implements Comparable<APQEntry> {

  private Double key;
  private MapNode node;
  private MapNode target;
  private boolean visited;

  /**
   * Constructor.
   * @param entryKey eKey
   * @param mapNode node
   * @param targetNode target
   */
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

  /**
   * @param newKey key
   */
  public void setKey(double newKey) {
    key = newKey;
  }

  /**
   * @return key
   */
  public Double getKey() {
    return key;
  }

  /**
   * @return node
   */
  public MapNode getValue() {
    return node;
  }

  /**
   * @return visited
   */
  public boolean visited() {
    return visited;
  }

  /**
   * set visited to true.
   */
  public void visit() {
    visited = true;
  }

}
