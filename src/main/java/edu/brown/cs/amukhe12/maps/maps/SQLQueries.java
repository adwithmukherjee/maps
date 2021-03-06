package edu.brown.cs.amukhe12.maps.maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQLQueries.
 */
public final class SQLQueries {

  private SQLQueries() {
  }
  public static final String SELECT_ALL_EDGES = ""
      + "SELECT w.id, w.name, w.type, w.start, w.end\n"
      + "FROM way as w \n"
      + "WHERE (type != \"\");";

  public static final String SELECT_ALL_TRAVERSABLE_NODES = ""
      + "with subquery(n) as(\n"
      + "\n"
      + "SELECT w.start as n\n"
      + "FROM way as w\n"
      + "WHERE w.type != \"\"\n"
      + "UNION\n"
      + "SELECT w2.end as n\n"
      + "FROM way as w2\n"
      + "WHERE w2.type != \"\"\n"
      + "ORDER BY n ASC\n"
      + ")\n"
      + "\n"
      + "SELECT m.id, m.latitude, m.longitude\n"
      + "FROM subquery as s\n"
      + "LEFT JOIN node as m\n"
      + "ON s.n = m.id";

  /**
   * ways.
   * @param lat1 lat1
   * @param long1 long1
   * @param lat2 lat2
   * @param long2 lat2
   * @return SQL for ways in the given bounding box
   */
  public static String ways(Double lat1, Double long1, Double lat2, Double long2) {
    return ""
        + "SELECT w.id \n"
        + "FROM way as w\n"
        + "LEFT JOIN node as n\n"
        + "ON w.start = n.id\n"
        + "LEFT JOIN node as m\n"
        + "ON w.end = m.id\n"
        + "WHERE ((n.latitude <= " + lat1 + " AND n.latitude >=" + lat2 + ") AND (n.longitude >= "
        + long1 + " AND n.longitude <= " + long2 + "))\n"
        + "OR ((m.latitude <= " + lat1 + " AND m.latitude >=" + lat2 + ") AND (m.longitude >= "
        + long1 + " AND m.longitude <=" + long2 + "))";
  }

  /**
   * ways.
   * @param lat1 lat1
   * @param long1 long1
   * @param lat2 lat2
   * @param long2 lat2
   * @return  SQL for ways in the given bounding box
   */
  public static String waysSelectAll(Double lat1, Double long1, Double lat2, Double long2) {
    return ""
        + "SELECT w.id, n.latitude, n.longitude, m.latitude, m.longitude, w.name, w.type \n"
        + "FROM way as w\n"
        + "LEFT JOIN node as n\n"
        + "ON w.start = n.id\n"
        + "LEFT JOIN node as m\n"
        + "ON w.end = m.id\n"
        + "WHERE ((n.latitude <= " + lat1 + " AND n.latitude >=" + lat2 + ") AND (n.longitude >= "
        + long1 + " AND n.longitude <= " + long2 + "))\n"
        + "OR ((m.latitude <= " + lat1 + " AND m.latitude >=" + lat2 + ") AND (m.longitude >= "
        + long1 + " AND m.longitude <=" + long2 + "))";
  }

//
//  public static String routeEdges(Double maxLat, Double minLong, Double minLat, Double maxLong) {
//    return ""
//        + "SELECT w.id, w.name, w.type, w.start, w.end \n"
//        + "FROM way as w\n"
//        + "LEFT JOIN node as n\n"
//        + "ON w.start = n.id\n"
//        + "LEFT JOIN node as m\n"
//        + "ON w.end = m.id\n"
//        + "WHERE ((w.type != \"\")"
//        + "AND (((n.latitude <= " + maxLat + " AND n.latitude >=" + minLat
//        + ") AND (n.longitude >= " + minLong + " AND n.longitude <= " + maxLong + "))\n"
//      + "OR ((m.latitude <= " + maxLat + " AND m.latitude >=" + minLat + ") AND (m.longitude >= "
//        + minLong + " AND m.longitude <=" + maxLong + "))))";
//  }

  /**
   * streetIntersect.
   * @param street1 street
   * @param street2 crossStreet
   * @return  SQL for intersection of streets, null if there is none.
   */
  public static String streetIntersect(String street1, String street2) {
    return ""
        + "WITH ways1 AS (\n"
        + "    SELECT *\n"
        + "    FROM way\n"
        + "    WHERE name = \"" + street1 + "\"\n"
        + "),\n"
        + "ways2 AS (\n"
        + "    SELECT *\n"
        + "    FROM way\n"
        + "    WHERE name = \"" + street2 + "\"\n"
        + ")\n"
        + "\n"
        + "SELECT w.start, w.end, v.start, v.end\n"
        + "FROM ways1 as w\n"
        + "INNER JOIN ways2 as v\n"
        + "ON (w.start = v.start OR w.start = v.end OR w.end = v.start OR w.end = v.end)\n"
        + "LIMIT 1;";
  }

  /**
   * @param currentId node
   * @return  SQL for out edges of node
   */
  public static String getOutEdges(String currentId) {
    return "SELECT w.id, w.name, w.type, w.end\n"
        + "FROM way as w\n"
        + "WHERE w.type != '' AND w.type != 'unclassified' AND w.start = '" + currentId + "' ;";

  }

  /**
   * to drop a checkin table.
   * @param conn connection
   * @throws SQLException throws SQL Exception
   */
  public static void dropCheckinsTable(Connection conn) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("DROP TABLE IF EXISTS checkins");
    prep.executeUpdate();
    prep.close();

  }

  /**
   * to create checkin table.
   * @param conn connection
   * @throws SQLException throwns SQL Exception
   */
  public static void createCheckinsTable(Connection conn) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
        "CREATE TABLE checkins (id INTEGER NOT NULL, username VARCHAR(100), "
            + "timestamp DOUBLE, latitude DOUBLE, longitude DOUBLE);");
    prep.executeUpdate();
    prep.close();
  }


  /**
   * insertCheckin to checkin table.
   * @param conn connection
   * @param id id
   * @param username username
   * @param timestamp timestamp
   * @param latitude lat
   * @param longitude lon
   * @throws SQLException throws exception
   */
  public static void insertCheckin(Connection conn, int id, String username,
                                   double timestamp, double latitude, double longitude)
      throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO checkins VALUES(?,?,?,?,?);");
    prep.setInt(1, id);
    prep.setString(2, username);
    prep.setDouble(3, timestamp);
    prep.setDouble(4, latitude);
    prep.setDouble(5, latitude);
    prep.executeUpdate();
  }

  /**
   * get users checkins.
   * @param conn connection
   * @param id user id
   * @return users checkins
   * @throws SQLException throws SQL Exception
   */
  public static PreparedStatement userCheckin(Connection conn, int id) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM checkins WHERE id = ?");
    prep.setInt(1, id);
    return prep;
  }
}
