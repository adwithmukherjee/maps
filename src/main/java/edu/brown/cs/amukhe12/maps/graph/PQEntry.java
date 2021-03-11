package edu.brown.cs.amukhe12.maps.graph;

public class PQEntry<N extends Node> implements Comparable<PQEntry> {

    private Double _key;
    private N _node;

    public PQEntry(double key, N node){
      _key = key;
      _node = node;
    }

    @Override
    public int compareTo(PQEntry o) {
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
  }
