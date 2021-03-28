package edu.brown.cs.amukhe12.maps.checkin;

/**
 * UserCheckin.
 */
public class UserCheckin {

  private int id;
  private String name;
  private double ts;
  private double lat;
  private double lon;

  /**
   * Constructor for UserCheckin.
   * @param userId  users Id
   * @param username username
   * @param timestamp timestamp of checkin
   * @param latitude lat
   * @param longitude lon
   */
  public UserCheckin(
      int userId,
      String username,
      double timestamp,
      double latitude,
      double longitude) {
    id = userId;
    name = username;
    ts = timestamp;
    lat = latitude;
    lon = longitude;
  }

  /**
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @return timestamp
   */
  public double getTimestamp() {
    return ts;
  }

  /**
   * @return latitude
   */
  public double getLat() {
    return lat;
  }

  /**
   * @return longitude
   */
  public double getLon() {
    return lon;
  }
}
