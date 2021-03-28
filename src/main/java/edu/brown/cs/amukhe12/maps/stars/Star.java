package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.csvparser.CSVEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Star Object that stores fields for starId, properName, and x, y, z coordinates.
 */

public class Star implements CSVEntry {
  private String starId;
  private String properName;
  private double x;
  private double y;
  private double z;

  /**
   * constructor.
   */
  public Star() { }

  @Override
  public void setFields(List<String> fields) throws Exception {
    if (fields.size() != 5) {
      throw new Exception("incorrect number of fields in csv row");
    }
    starId = fields.get(0);
    properName = fields.get(1);
    x = Double.parseDouble(fields.get(2));
    y = Double.parseDouble(fields.get(3));
    z = Double.parseDouble(fields.get(4));
  }

  @Override
  public List getFields() {
    ArrayList fields = new ArrayList();
    fields.add(starId);
    fields.add(properName);
    fields.add(x);
    fields.add(y);
    fields.add(z);
    return fields;
  }

  @Override
  public String toString() {
    return "Star{"
        + "starId='" + starId + '\''
        + ", properName='" + properName + '\''
        + ", x=" + x
        + ", y=" + y
        + ", z=" + z
        + '}';
  }

  /**
   * @return x
   */
  public double getX() {
    return x;
  }

  /**
   * @return y
   */
  public double getY() {
    return y;
  }

  /**
   * @return z
   */
  public double getZ() {
    return z;
  }

  /**
   * @return name
   */
  public String getProperName() {
    return properName;
  }

  /**
   * @return id
   */
  public String getStarId() {
    return starId;
  }
}

