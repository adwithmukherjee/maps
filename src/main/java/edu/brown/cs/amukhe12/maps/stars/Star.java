package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.csvparser.CSVEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Star Object that stores fields for starId, properName, and x, y, z coordinates.
 */

public class Star implements CSVEntry {
  private String _starId;
  private String _properName;
  private double _x;
  private double _y;
  private double _z;

  public Star() { }

  @Override
  public void setFields(List<String> fields) throws Exception {
    if (fields.size() != 5) {
      throw new Exception("incorrect number of fields in csv row");
    }
    _starId = fields.get(0);
    _properName = fields.get(1);
    _x = Double.parseDouble(fields.get(2));
    _y = Double.parseDouble(fields.get(3));
    _z = Double.parseDouble(fields.get(4));
  }

  @Override
  public List getFields() {
    ArrayList fields = new ArrayList();
    fields.add(_starId);
    fields.add(_properName);
    fields.add(_x);
    fields.add(_y);
    fields.add(_z);
    return fields;
  }

  @Override
  public String toString() {
    return "Star{"
        + "starId='" + _starId + '\''
        + ", properName='" + _properName + '\''
        + ", x=" + _x
        + ", y=" + _y
        + ", z=" + _z
        + '}';
  }

  public double getX() {
    return _x;
  }

  public double getY() {
    return _y;
  }

  public double getZ() {
    return _z;
  }

  public String getProperName() {
    return _properName;
  }

  public String getStarId() {
    return _starId;
  }
}

