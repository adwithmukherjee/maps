package edu.brown.cs.amukhe12.maps.graph;

/**
 * DPQEntry.
 * @param <N> Node type
 */
public class DPQEntry<N extends Node> implements Comparable<DPQEntry> {

  private Double key;
  private N node;
//  private boolean _visited;

  /**
   * Constructor.
   * @param entryKey eKey
   * @param node node
   */
  public DPQEntry(double entryKey, N node) {
    key = entryKey;
    node = node;
  }

  @Override
  public int compareTo(DPQEntry o) {
    return this.getKey().compareTo(o.getKey());
  }

  /**
   * @param newKey newKey
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
  public N getValue() {
    return node;
  }

//  public boolean visited() {
//    return _visited;
//  }
}
