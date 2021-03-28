package edu.brown.cs.amukhe12.maps.graph;

public class DPQEntry<N extends Node> implements Comparable<DPQEntry> {

  private Double key;
  private N node;
//  private boolean _visited;

  public DPQEntry(double entryKey, N node) {
    key = entryKey;
    node = node;
  }

  @Override
  public int compareTo(DPQEntry o) {
    return this.getKey().compareTo(o.getKey());
  }

  public void setKey(double newKey) {
    key = newKey;
  }

  public Double getKey() {
    return key;
  }

  public N getValue() {
    return node;
  }

//  public boolean visited() {
//    return _visited;
//  }
}
