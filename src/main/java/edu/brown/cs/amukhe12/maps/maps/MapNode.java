package edu.brown.cs.amukhe12.maps.maps;

import edu.brown.cs.amukhe12.maps.graph.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MapNode implements Node<MapNode, Way> {

  private Collection<Way> _outEdges;
  private Collection<Way> _inEdges;
  private String _id;
  private double _latitude;
  private double _longitude;

  public MapNode(String id, Double latitude, Double longitude) {
    _outEdges = new HashSet<>();
    _inEdges = new HashSet<>();
    _id = id;
    _latitude = latitude;
    _longitude = longitude;
  }


  /**
   * Haverstein distance to given MapNode.
   *
   * @param v
   * @return haverstein distance
   */
  public double distanceTo(MapNode v) {

    double euclidean = Math.sqrt(Math.pow(this.getCoords().get(0) - v.getCoords().get(0), 2)
        + Math.pow(this.getCoords().get(1) - v.getCoords().get(1), 2));
    ////HAVERSINE/////
    double r = 6371; //meters

    double dLat = Math.toRadians(this.getCoords().get(0) - v.getCoords().get(0));
    double dLong = Math.toRadians(this.getCoords().get(1) - v.getCoords().get(1));

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(this.getCoords().get(0)))
        * Math.cos(Math.toRadians(v.getCoords().get(0))) * Math.sin(dLong / 2)
        * Math.sin(dLong / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return r * c;
    //return euclidean;
  }

  public List<Double> getCoords() {
    return Arrays.asList(_latitude, _longitude);
  }

  @Override
  public void clearEdges() {
    _outEdges = new HashSet<>();
  }

  @Override
  public Collection<Way> getOutEdges() {
    return _outEdges;
  }

  @Override
  public Collection<Way> getInEdges() {
    return _inEdges;
  }

  @Override
  public void addOutEdge(Way way) {
    _outEdges.add(way);
  }

  @Override
  public void addInEdge(Way way) {
    _inEdges.add(way);
  }

  @Override
  public String getId() {
    return _id;
  }


}


