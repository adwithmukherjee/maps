package edu.brown.cs.amukhe12.maps.graph;

public class DPQEntry<N extends Node> implements Comparable<DPQEntry> {

    private Double _key;
    private N _node;
    private boolean _visited;

    public DPQEntry(double key, N node){
      _key = key;
      _node = node;
    }

    @Override
    public int compareTo(DPQEntry o) {
      return this.getKey().compareTo(o.getKey());
    }
    public void setKey(double key){
      _key = key;
    }
    public Double getKey(){
      return _key;
    }
    public N getValue(){
      return _node;
    }
    public boolean visited() {
      return _visited;
    }
  }
