package edu.brown.cs.amukhe12.maps.maps;

import java.util.List;

public class SQLQueries {
  public static final String SELECT_ALL_EDGES = "" +
      "SELECT w.id, w.name, w.type, w.start, w.end\n" +
      "FROM way as w \n" +
      "WHERE (type != \"\");";

  public static final String SELECT_ALL_TRAVERSABLE_NODES = "" +
      "with subquery(n) as(\n" +
      "\n" +
      "SELECT w.start as n\n" +
      "FROM way as w\n" +
      "WHERE w.type != \"\"\n" +
      "UNION\n" +
      "SELECT w2.end as n\n" +
      "FROM way as w2\n" +
      "WHERE w2.type != \"\"\n" +
      "ORDER BY n ASC\n" +
      ")\n" +
      "\n" +
      "SELECT m.id, m.latitude, m.longitude\n" +
      "FROM subquery as s\n" +
      "LEFT JOIN node as m\n" +
      "ON s.n = m.id";

  public static String ways(Double lat1, Double long1, Double lat2, Double long2) {
    return "" +
        "SELECT w.id \n" +
        "FROM way as w\n" +
        "LEFT JOIN node as n\n" +
        "ON w.start = n.id\n" +

        "LEFT JOIN node as m\n" +
        "ON w.end = m.id\n" +
        "WHERE ((n.latitude <= " + lat1 + " AND n.latitude >=" + lat2 + ") AND (n.longitude >= " +
        long1 + " AND n.longitude <= " + long2 + "))\n" +
        "OR ((m.latitude <= " + lat1 + " AND m.latitude >=" + lat2 + ") AND (m.longitude >= " +
        long1 + " AND m.longitude <=" + long2 + "))";
  }

  public static String waysSelectAll(Double lat1, Double long1, Double lat2, Double long2) {
    return "" +
        "SELECT w.id, n.latitude, n.longitude, m.latitude, m.longitude \n" +
        "FROM way as w\n" +
        "LEFT JOIN node as n\n" +
        "ON w.start = n.id\n" +

        "LEFT JOIN node as m\n" +
        "ON w.end = m.id\n" +
        "WHERE ((n.latitude <= " + lat1 + " AND n.latitude >=" + lat2 + ") AND (n.longitude >= " +
        long1 + " AND n.longitude <= " + long2 + "))\n" +
        "OR ((m.latitude <= " + lat1 + " AND m.latitude >=" + lat2 + ") AND (m.longitude >= " +
        long1 + " AND m.longitude <=" + long2 + "))";
  }


  public static String routeEdges(Double maxLat, Double minLong, Double minLat, Double maxLong) {
    return "" +
        "SELECT w.id, w.name, w.type, w.start, w.end \n" +
        "FROM way as w\n" +
        "LEFT JOIN node as n\n" +
        "ON w.start = n.id\n" +
        "LEFT JOIN node as m\n" +
        "ON w.end = m.id\n" +
        "WHERE ((w.type != \"\")" +
        "AND (((n.latitude <= " + maxLat + " AND n.latitude >=" + minLat +
        ") AND (n.longitude >= " + minLong + " AND n.longitude <= " + maxLong + "))\n" +
        "OR ((m.latitude <= " + maxLat + " AND m.latitude >=" + minLat + ") AND (m.longitude >= " +
        minLong + " AND m.longitude <=" + maxLong + "))))";
  }

  public static String streetIntersect(String street1, String street2) {
    return "" +
        "WITH ways1 AS (\n" +
        "    SELECT *\n" +
        "    FROM way\n" +
        "    WHERE name = \"" + street1 + "\"\n" +
        "),\n" +
        "ways2 AS (\n" +
        "    SELECT *\n" +
        "    FROM way\n" +
        "    WHERE name = \"" + street2 + "\"\n" +
        ")\n" +
        "\n" +
        "SELECT w.start, w.end, v.start, v.end\n" +
        "FROM ways1 as w\n" +
        "INNER JOIN ways2 as v\n" +
        "ON (w.start = v.start OR w.start = v.end OR w.end = v.start OR w.end = v.end)\n" +
        "LIMIT 1;";
  }

  public static String getOutEdges(String currentId) {
    return "SELECT w.id, w.name, w.type, w.end\n" +
        "FROM way as w\n" +
        "WHERE w.type != '' AND w.type != 'unclassified' AND w.start = '"+currentId+"' ;";

  }
}
