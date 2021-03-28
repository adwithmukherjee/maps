package edu.brown.cs.amukhe12.maps;

//import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import edu.brown.cs.amukhe12.maps.Events.maps.MapAction;
import edu.brown.cs.amukhe12.maps.Events.maps.NearestMapAction;
import edu.brown.cs.amukhe12.maps.Events.maps.RouteAction;
import edu.brown.cs.amukhe12.maps.Events.maps.WaysAction;
import edu.brown.cs.amukhe12.maps.Events.stars.MockAction;
import edu.brown.cs.amukhe12.maps.Events.stars.NaiveNeighborsAction;
import edu.brown.cs.amukhe12.maps.Events.stars.NaiveRadiusAction;
import edu.brown.cs.amukhe12.maps.Events.stars.NeighborsAction;
import edu.brown.cs.amukhe12.maps.Events.stars.RadiusAction;
import edu.brown.cs.amukhe12.maps.Events.stars.StarsAction;
import edu.brown.cs.amukhe12.maps.REPL.EventKey;
import edu.brown.cs.amukhe12.maps.REPL.REPL;
import edu.brown.cs.amukhe12.maps.checkin.CheckinThread;
import edu.brown.cs.amukhe12.maps.checkin.UserCheckin;
import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.maps.Way;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;
import edu.brown.cs.amukhe12.maps.stars.PersonList;
import edu.brown.cs.amukhe12.maps.stars.Star;
import edu.brown.cs.amukhe12.maps.stars.StarList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
//import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private DBReference db = new DBReference();
  private CheckinThread thread;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   * @throws  SQLException throws SQLException
   * @throws  ClassNotFoundException throws ClassNotFoundExecption
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  private String[] args;


  private Main(String[] args) {
    this.args = args;
  }

  private void run() throws SQLException, ClassNotFoundException {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    try {

      StarList stars = new StarList();
      PersonList people = new PersonList();


      List<EventKey> events = new ArrayList<EventKey>();

      events.add(new EventKey("mock", new MockAction(people)));
      events.add(new EventKey("stars", new StarsAction(stars)));
      events.add(new EventKey("naive_neighbors", new NaiveNeighborsAction(stars)));
      events.add(new EventKey("naive_radius", new NaiveRadiusAction(stars)));
      events.add(new EventKey("neighbors", new NeighborsAction(stars)));
      events.add(new EventKey("radius", new RadiusAction(stars)));
      events.add(new EventKey("map", new MapAction(db)));
      events.add(new EventKey("nearest", new NearestMapAction(db)));
      events.add(new EventKey("ways", new WaysAction(db)));
      events.add(new EventKey("route", new RouteAction(db)));

      thread = new CheckinThread();
      thread.start();

      REPL repl = new REPL(events);

    } catch (Exception e) {
      System.out.println("ERROR: Could not add events or create REPL");
    }
  }


  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


    FreeMarkerEngine freeMarker = createEngine();

//    StarList stars = new StarList();
//    String fireflies = this.generateStars();
//
//    // Setup Stars Routes
//    Spark.get("/stars", new FrontHandler(fireflies), freeMarker);
//    Spark.post("/select-data", new DataHandler(stars, fireflies), freeMarker);
//    Spark.post("/neighbors", new NeighborHandler(stars, fireflies), freeMarker);
//    Spark.post("/radius", new RadiusHandler(stars, fireflies), freeMarker);

    //Setup Maps Routes

   // DBReference _db = new DBReference();

    Spark.post("/ways", new WaysHandler());
    Spark.post("/nacroute", new NameAndCoordRouteHandler());
    Spark.post("/croute", new CoordsRouteHandler());
    Spark.post("/nearest", new NearestHandler());
    Spark.post("/intersection", new IntersectionHandler());
    Spark.post("/checkin", new CheckinHandler());
    Spark.post("/userCheckins", new UserCheckinHandler());


  }

  /**
   * UserCheckinHandler.
   */
  private class UserCheckinHandler implements Route {

    private Connection conn = null;

    UserCheckinHandler() {
      try {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + "data/maps/maps.sqlite3";
        this.conn = DriverManager.getConnection(urlToDB);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("ERROR: could not connect to maps database");
      }
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject body = new JSONObject((request.body()));
      int id = body.getInt("id");
      //System.out.println(id);

      PreparedStatement prep = SQLQueries.userCheckin(conn, id);
      ResultSet rs = prep.executeQuery();

      List<String[]> userInfoList = new ArrayList<>();
      while (rs.next()) {
        String[] fields = new String[5];
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          fields[i - 1] = (rs.getString(i));
        }
        userInfoList.add(fields);

      }
      rs.close();
      prep.close();

      String[][] userInfo = new String[userInfoList.size()][5];

      for (int i = 0; i < userInfoList.size(); i++) {
        userInfo[i] = userInfoList.get(i);
      }

      Map variables = ImmutableMap.of("user", userInfo);
      return GSON.toJson(variables);
    }
  }

  ///////CHECKIN SERVER/////


  /**
   * CheckinHandler.
   */
  private class CheckinHandler implements Route {

    private Connection conn = null;

    CheckinHandler() {

      try {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + "data/maps/maps.sqlite3";
        this.conn = DriverManager.getConnection(urlToDB);
      } catch (Exception e) {
        System.out.println("ERROR: could not connect to maps database");
      }

      try {
        SQLQueries.dropCheckinsTable(this.conn);
        SQLQueries.createCheckinsTable(this.conn);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("ERROR: could not create checking table");
      }
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

      Map<Double, UserCheckin> users = thread.getLatestCheckins();

      String[][] userInfo = new String[users.size()][5];
      int i = 0;

      for (Double timestamp : users.keySet()) {
        UserCheckin user = users.get(timestamp);
        userInfo[i] = new String[]{timestamp.toString(), "" + user.getId(),
            user.getName(), "" + user.getLat(), "" + user.getLon()};
        i++;
      }

      Map variables = ImmutableMap.of("users", userInfo);
      return GSON.toJson(variables);
    }
  }

  ///////////////////////////
  //////MAPS HANDLERS////////
  ///////////////////////////

  /**
   * NameAndCoordRouteHandler.
   */
  private class NameAndCoordRouteHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject body = new JSONObject((request.body()));
      String street = body.getString("street");
      String cross = body.getString("cross");
      String node2Id = body.getString("node2Id");
      List<String> intersection = new SQLParser(db.getFilename(), null)
          .parseAndReturnList(SQLQueries.streetIntersect(street, cross)).get(0);
      String node1Id = "";
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          if (intersection.get(i).equals(intersection.get(2 + j))) {
            node1Id = intersection.get(i);
            break;
          }
        }
      }
      db.getGraph().clear();
      List<Way> route = db.getGraph().routeFromNodeIds(node1Id, node2Id);
      Double[][] results = new Double[route.size()][4];
      int i = 0;
      for (Way way : route) {
        Double[] val = new Double[]{way.from().getCoords().get(0), way.from().getCoords().get(1),
            way.to().getCoords().get(0), way.to().getCoords().get(1)};
        results[i] = val;
        i++;
      }
      Map variables = ImmutableMap.of("route", results);
      return GSON.toJson(variables);
    }
  }

  /**
   * CoordsRouteHandler.
   */
  private class CoordsRouteHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject body = new JSONObject((request.body()));
      String node1Id = body.getString("node1Id");
      String node2Id = body.getString("node2Id");
      db.getGraph().clear();
      List<Way> route = db.getGraph().routeFromNodeIds(node1Id, node2Id);
      Double[][] results = new Double[route.size()][4];
      int i = 0;
      for (Way way : route) {
        Double[] val = new Double[]{way.from().getCoords().get(0), way.from().getCoords().get(1),
            way.to().getCoords().get(0), way.to().getCoords().get(1)};
        results[i] = val;
        i++;
      }
      Map variables = ImmutableMap.of("route", results);
      return GSON.toJson(variables);
    }
  }

  /**
   * WaysHandler.
   */
  private class WaysHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      JSONObject body = new JSONObject(req.body());
      double nwLat = body.getDouble("nwLat");
      double nwLong = body.getDouble("nwLong");
      double seLat = body.getDouble("seLat");
      double seLong = body.getDouble("seLong");

      List<List<String>> wayInfo = (new SQLParser(db.getFilename(), null))
          .parseAndReturnList(SQLQueries.waysSelectAll(nwLat, nwLong, seLat, seLong));

      double[][] ways = new double[wayInfo.size()][4];
      int i = 0;
      for (List<String> way : wayInfo) {
        ways[i] = new double[]{Double.parseDouble(way.get(1)), Double.parseDouble(way.get(2)),
            Double.parseDouble(way.get(3)), Double.parseDouble(way.get(4))};
        i++;
      }
      Map variables = ImmutableMap.of("ways", ways);
      return GSON.toJson(variables);
    }
  }

  /**
   * NearestHandler.
   */
  private class NearestHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      JSONObject body = new JSONObject(req.body());
      double nodeLat = body.getDouble("nodeLat");
      double nodeLong = body.getDouble("nodeLong");

      NearestMapAction nearestMapAction = new NearestMapAction(db);
      List<KDNode<MapNode>> nearest = db.getTree()
          .nearest(1, nodeLat, nodeLong);
      Double[] results = new Double[]{nearest.get(0).getValue().getCoords().get(0),
          nearest.get(0).getValue().getCoords().get(1)};
      Map variables = ImmutableMap.of("coords", results, "id", nearest.get(0).getValue().getId());
      return GSON.toJson(variables);
    }
  }

  /**
   * IntersectionHandler.
   */
  private class IntersectionHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      JSONObject body = new JSONObject(req.body());
      String street = body.getString("street");
      String cross = body.getString("cross");

      String str11 = street.replaceAll("\"", "");
      String str12 = cross.replaceAll("\"", "");


      List<List<String>> intersectionResults = new SQLParser(db.getFilename(), null)
          .parseAndReturnList(SQLQueries.streetIntersect(street, cross));
      if (intersectionResults == null) {
        return null;
      }
      List<String> intersection = intersectionResults.get(0);

      String node1Id = "";
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          if (intersection.get(i).equals(intersection.get(2 + j))) {
            node1Id = intersection.get(i);
            break;
          }
        }
      }

      MapNode node1 = db.getGraph().getNodeFromId(node1Id);
      double lat1 = node1.getCoords().get(0);
      double long1 = node1.getCoords().get(1);

      Double[] results = new Double[]{lat1, long1};
      Map variables = ImmutableMap.of("coords", results, "id", node1Id);
      return GSON.toJson(variables);
    }
  }


  ///////////////////////////
  //////STARS HANDLERS///////
  ///////////////////////////
  private String generateStars() {


    String circles = "";
    final int numStars = 800;
    for (int i = 0; i < numStars; i++) {
      final int onehundred = 100;
      final int onethousand = 1000;
      final double onehalf = 0.5;
      circles = circles + "<circle fill=\"#FFFFFF\" cx=\""
          + (int) (numStars * Math.random() - onehundred)
          + "\" cy=\"" + (int) (onethousand * Math.random())
          + "\" r=\"0.398\" style = \"animation: stars-blink " + (onehalf + 4 * Math.random())
          + "s linear infinite; \"/>";
    }
    return "<div class = \"stars\">\n"
        + "     <div class=\"starWrap starProject\">\n"
        + "       <svg focusable=\"false\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\""
        + " x=\"0px\" y=\"0px\" viewBox=\"0 0 599.456 593.71\" enable-background="
        + "\"new 0 0 599.456 593.71\" xml:space=\"preserve\" id=\"stars\">\n"
        + "           " + circles + ""
        + "       </svg>"
        + "     </div>"
        + "   </div>";
  }

  private String generateCell(String starId, String starName, double x, double y, double z) {
    final int oneEighty = 180;
    final int forty = 40;
    final int twentyFive = 25;
    final int seventy = 70;
    final int fifteen = 15;
    return "<li>\n"
        + "          <div class = \"list-item\">\n"
        + "            <div class = \"list-item-content\">\n"
        + "              <i class=\"fas fa-star\" style=\"margin-right: 10px; color:hsl("
        + (int) (oneEighty + forty * Math.random()) + "," + (twentyFive + seventy * Math.random())
        + "%,"
        + (fifteen + seventy * Math.random()) + "%)\">  </i>\n"
        + "                 " + starName + "\n"
        + "              <div class = \"list-item-id\">\n"
        + "                " + starId + "\n"
        + "              </div>\n"
        + "            </div>\n"
        + "            <div style=\"flex: 1; margin: auto\">\n"
        + "              <div class = \"list-item-coords\">\n"
        + "                (" + x + ", " + y + ", " + z + ")\n"
        + "              </div>\n"
        + "            </div>\n"
        + "          </div>\n"
        + "        </li>";
  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class FrontHandler implements TemplateViewRoute {

    private String fireflies;

    FrontHandler(String firefly) {
      fireflies = firefly;
    }

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars!", "message", "poop", "suggestions", "Load in some data!", "cells", "", "stars",
          fireflies);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * DataHandler.
   */
  private class DataHandler implements TemplateViewRoute {

    private StarList stars;
    private String fireflies;

    DataHandler(StarList starList, String firefly) {
      stars = starList;
      fireflies = firefly;
    }

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {

      QueryParamsMap qm = request.queryMap();
      String filename = qm.value("text");
      String suggestionString = "";

      try {
        stars.clear();
        new CSVParser("data/stars/" + filename, stars);
        suggestionString = filename + " successfully loaded!";
      } catch (Exception e) {
        suggestionString = "ERROR: " + e.getMessage();
      }


      Map<String, String> variables = ImmutableMap
          .of("title", "Stars!", "message", "Type in a Word!", "suggestions", suggestionString,
              "cells", "", "stars", fireflies);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * NeighborHandler.
   */
  private class NeighborHandler implements TemplateViewRoute {

    private String fireflies;
    private StarList stars;

    NeighborHandler(StarList starList, String firefly) {
      stars = starList;
      fireflies = firefly;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String textFromTextField = qm.value("text");
      String naive = qm.value("naive");
      List<String> arg = Arrays.asList(textFromTextField.split(" "));

      String suggestionString;
      String cells = "";

      try {
        if (stars.isEmpty()) {
          suggestionString =
              "No stars loaded. Please load stars through the Select Star Data button";
        } else {
          if (arg.size() == 4) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = stars.neighborsSearchCoords(Integer.parseInt(arg.get(0)),
                  Double.parseDouble(arg.get(1)), Double.parseDouble(arg.get(2)),
                  Double.parseDouble(arg.get(3)));
            } else {
              neighbors = stars.naiveNeighborsSearchCoords(Integer.parseInt(arg.get(0)),
                  Double.parseDouble(arg.get(1)), Double.parseDouble(arg.get(2)),
                  Double.parseDouble(arg.get(3)));
            }
            for (Star star : neighbors) {
              cells = cells
                  + generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " nearest neighbors found!";
          } else if (arg.size() == 2) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = stars
                  .neighborsSearchName(Integer.parseInt(arg.get(0)),
                      arg.get(1).replaceAll("\"", ""));
            } else {
              neighbors = stars
                  .naiveNeighborsName(Integer.parseInt(arg.get(0)),
                      arg.get(1).replaceAll("\"", ""));
            }
            for (Star star : neighbors) {

              cells = cells
                  + generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " nearest neighbors found!";
          } else {
            suggestionString = "ERROR: incorrect number of arguments";
          }
        }
      } catch (Exception e) {
        suggestionString = "ERROR: " + e.getMessage();
      }


      Map<String, String> variables = ImmutableMap
          .of("title", "Stars!", "message", "Type in a Word!", "suggestions", suggestionString,
              "cells", cells, "stars", fireflies);

      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * RadiusHandler.
   */
  private class RadiusHandler implements TemplateViewRoute {

    private StarList stars;
    private String fireflies;

    RadiusHandler(StarList starList, String firefly) {
      stars = starList;
      fireflies = firefly;
    }

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
      QueryParamsMap qm = request.queryMap();
      String textFromTextField = qm.value("text");
      String naive = qm.value("naive");
      List<String> arg = Arrays.asList(textFromTextField.split(" "));
      String suggestionString = "";
      String cells = "";

      try {
        if (stars.isEmpty()) {
          throw new Exception("no stars loaded");
        } else {
          if (arg.size() == 4) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = stars
                  .radiusCoords(Double.parseDouble(arg.get(0)), Double.parseDouble(arg.get(1)),
                      Double.parseDouble(arg.get(2)), Double.parseDouble(arg.get(3)));
            } else {
              neighbors = stars
                  .naiveRadiusCoords(Double.parseDouble(arg.get(0)),
                      Double.parseDouble(arg.get(1)),
                      Double.parseDouble(arg.get(2)), Double.parseDouble(arg.get(3)));
            }
            for (Star star : neighbors) {
              cells = cells
                  + generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " stars found!";
          } else if (arg.size() == 2) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors =
                  stars
                      .radiusName(Double.parseDouble(arg.get(0)),
                          arg.get(1).replaceAll("\"", ""));
            } else {
              neighbors = stars
                  .naiveRadiusName(Double.parseDouble(arg.get(0)),
                      arg.get(1).replaceAll("\"", ""));
            }
            for (Star star : neighbors) {
              cells = cells
                  + generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " stars found!";
          } else {
            throw new Exception("incorrect number of arguments");
          }
        }
      } catch (Exception e) {
        suggestionString = "ERROR: " + e.getMessage();
      }

      Map<String, String> variables = ImmutableMap
          .of("title", "Stars!", "message", "Type in a Word!", "suggestions", suggestionString,
              "cells", cells, "stars", fireflies);

      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
