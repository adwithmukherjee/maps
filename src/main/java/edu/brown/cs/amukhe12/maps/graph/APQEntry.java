package edu.brown.cs.amukhe12.maps.graph;
import edu.brown.cs.amukhe12.maps.maps.MapNode;

public class APQEntry implements Comparable<APQEntry> {

    private Double _key;
    private MapNode _node;
    private MapNode _target;
    private boolean _visited;

    public APQEntry(double key, MapNode node, MapNode target){
      _key = key;
      _node = node;
      _target = target;
      _visited = false;
    }

    @Override
    public int compareTo(APQEntry o) {
      Double cost1 = this.getKey() + this.getValue().distanceTo(_target);
      Double cost2 = o.getKey() + o.getValue().distanceTo(_target);
      return cost1.compareTo(cost2);
    }

    public void setKey(double key){
      _key = key;
    }
    public Double getKey(){
      return _key;
    }
    public MapNode getValue(){
      return _node;
    }
    public boolean visited() {
      return _visited;
    }
    public void visit() { _visited = true; }

  }
