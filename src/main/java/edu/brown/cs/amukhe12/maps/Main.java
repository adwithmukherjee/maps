package edu.brown.cs.amukhe12.maps;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import org.json.JSONException;
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

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private DBReference db = new DBReference();

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;


  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
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
    DBReference _db = new DBReference();
    Spark.post("/ways", new WaysHandler());
    Spark.post("/route",new RouteHandler());
    Spark.post("/nearest",new NearestHandler());




  }

  ///////////////////////////
  //////MAPS HANDLERS////////
  ///////////////////////////
  private class RouteHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
     JSONObject body = new JSONObject((request.body()));
     String street = body.getString("street");
     String cross = body.getString("cross");
     String node2Id = body.getString("node2Id");
     // NOTE: need to catch if null
     List<String> intersection = new SQLParser(db.getFilename(),null).parseAndReturnList(SQLQueries.streetIntersect(street,cross)).get(0);
     System.out.println(intersection);
      String node1Id = "";
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          if (intersection.get(i).equals(intersection.get(2 + j))) {
            node1Id = intersection.get(i);
            break;
          }
        }
      }
//      if (node1Id.equals("") ) {
//        throw new Exception("no intersection found");
//      }
      db.getGraph().clear();
      List<Way> route = db.getGraph().routeFromNodeIds(node1Id,node2Id);
      Double[][] results = new Double[route.size()][4];
      int i = 0;
      for (Way way : route) {
        Double[] val = new Double[] {way.from().getCoords().get(0), way.from().getCoords().get(1),
            way.to().getCoords().get(0), way.to().getCoords().get(1)};
        results[i] = val;
        i++;
      };
      Map variables = ImmutableMap.of("route",results);
      return GSON.toJson(variables);
    }
  }

  private class WaysHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      JSONObject body = new JSONObject(req.body());
      double nwLat = body.getDouble("nwLat");
      double nwLong = body.getDouble("nwLong");
      double seLat = body.getDouble("seLat");
      double seLong = body.getDouble("seLong");
      //TODO: use the global autocorrect instance to get the suggestions

      List<List<String>> wayInfo = (new SQLParser(db.getFilename(), null))
          .parseAndReturnList(SQLQueries.waysSelectAll(nwLat, nwLong, seLat, seLong));

      double[][] ways = new double[wayInfo.size()][4];
      int i = 0;
      for (List<String> way : wayInfo) {
        ways[i] = new double[] {Double.parseDouble(way.get(1)), Double.parseDouble(way.get(2)),
            Double.parseDouble(way.get(3)), Double.parseDouble(way.get(4))};
        i++;
      }
      //TODO: create an immutable map using the suggestions
      Map variables = ImmutableMap.of("ways", ways);

      //TODO: return a Json of the suggestions (HINT: use the GSON.Json())

      return GSON.toJson(variables);
    }
  }

  private class NearestHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      JSONObject body = new JSONObject(req.body());
      double nodeLat = body.getDouble("nodeLat");
      double nodeLong = body.getDouble("nodeLong");

      NearestMapAction nearestMapAction = new NearestMapAction(db);
      List<KDNode<MapNode>> nearest = db.getTree()
          .nearest(1, nodeLat, nodeLong);
      Double[] results = new Double[] {nearest.get(0).getValue().getCoords().get(0),nearest.get(0).getValue().getCoords().get(1)};
      Map variables = ImmutableMap.of("nearest", results);
      return GSON.toJson(variables);
    }
  }


  ///////////////////////////
  //////STARS HANDLERS///////
  ///////////////////////////
  private String generateStars() {


    String circles = "";
    for (int i = 0; i < 800; i++) {
      circles = circles + "<circle fill=\"#FFFFFF\" cx=\"" + (int) (800 * Math.random() - 100) +
          "\" cy=\"" + (int) (1000 * Math.random()) +
          "\" r=\"0.398\" style = \"animation: stars-blink " + (0.5 + 4 * Math.random()) +
          "s linear infinite; \"/>";
    }
    return "<div class = \"stars\">\n" +
        "     <div class=\"starWrap starProject\">\n" +
        "       <svg focusable=\"false\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" x=\"0px\" y=\"0px\" viewBox=\"0 0 599.456 593.71\" enable-background=\"new 0 0 599.456 593.71\" xml:space=\"preserve\" id=\"stars\">\n" +
        "           " + circles + "" +
        "       </svg>" +
        "     </div>" +
        "   </div>";
  }

  private String generateCell(String starId, String starName, double x, double y, double z) {
    return "<li>\n" +
        "          <div class = \"list-item\">\n" +
        "            <div class = \"list-item-content\">\n" +
        "              <i class=\"fas fa-star\" style=\"margin-right: 10px; color:hsl(" +
        (int) (180 + 40 * Math.random()) + "," + (25 + 70 * Math.random()) + "%," +
        (15 + 70 * Math.random()) + "%)\">  </i>\n" +
        "                 " + starName + "\n" +
        "              <div class = \"list-item-id\">\n" +
        "                " + starId + "\n" +
        "              </div>\n" +
        "            </div>\n" +
        "            <div style=\"flex: 1; margin: auto\">\n" +
        "              <div class = \"list-item-coords\">\n" +
        "                (" + x + ", " + y + ", " + z + ")\n" +
        "              </div>\n" +
        "            </div>\n" +
        "          </div>\n" +
        "        </li>";
  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class FrontHandler implements TemplateViewRoute {

    String _fireflies;

    FrontHandler(String fireflies) {
      _fireflies = fireflies;
    }

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars!", "message", "poop", "suggestions", "Load in some data!", "cells", "", "stars",
          _fireflies);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private class DataHandler implements TemplateViewRoute {

    StarList _stars;
    String _fireflies;

    DataHandler(StarList stars, String fireflies) {
      _stars = stars;
      _fireflies = fireflies;
    }

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {

      QueryParamsMap qm = request.queryMap();
      String filename = qm.value("text");
      String suggestionString = "";

      try {
        _stars.clear();
        new CSVParser("data/stars/" + filename, _stars);
        suggestionString = filename + " successfully loaded!";
      } catch (Exception e) {
        suggestionString = "ERROR: " + e.getMessage();
      }


      Map<String, String> variables = ImmutableMap
          .of("title", "Stars!", "message", "Type in a Word!", "suggestions", suggestionString,
              "cells", "", "stars", _fireflies);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private class NeighborHandler implements TemplateViewRoute {

    String _fireflies;
    StarList _stars;

    NeighborHandler(StarList stars, String fireflies) {
      _stars = stars;
      _fireflies = fireflies;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String textFromTextField = qm.value("text");
      String naive = qm.value("naive");
      List<String> args = Arrays.asList(textFromTextField.split(" "));

      String suggestionString;
      String cells = "";

      try {
        if (_stars.isEmpty()) {
          suggestionString =
              "No stars loaded. Please load stars through the Select Star Data button";
        } else {
          if (args.size() == 4) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = _stars.neighborsSearchCoords(Integer.parseInt(args.get(0)),
                  Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)),
                  Double.parseDouble(args.get(3)));
            } else {
              neighbors = _stars.naiveNeighborsSearchCoords(Integer.parseInt(args.get(0)),
                  Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)),
                  Double.parseDouble(args.get(3)));
            }
            for (Star star : neighbors) {
              cells = cells +
                  generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " nearest neighbors found!";
          } else if (args.size() == 2) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = _stars
                  .neighborsSearchName(Integer.parseInt(args.get(0)),
                      args.get(1).replaceAll("\"", ""));
            } else {
              neighbors = _stars
                  .naiveNeighborsName(Integer.parseInt(args.get(0)),
                      args.get(1).replaceAll("\"", ""));
            }
            for (Star star : neighbors) {

              cells = cells +
                  generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
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
              "cells", cells, "stars", _fireflies);

      return new ModelAndView(variables, "query.ftl");
    }
  }

  private class RadiusHandler implements TemplateViewRoute {

    StarList _stars;
    String _fireflies;

    RadiusHandler(StarList stars, String fireflies) {
      _stars = stars;
      _fireflies = fireflies;
    }

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
      QueryParamsMap qm = request.queryMap();
      String textFromTextField = qm.value("text");
      String naive = qm.value("naive");
      List<String> args = Arrays.asList(textFromTextField.split(" "));
      String suggestionString = "";
      String cells = "";

      try {
        if (_stars.isEmpty()) {
          throw new Exception("no stars loaded");
        } else {
          if (args.size() == 4) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors = _stars
                  .radiusCoords(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)),
                      Double.parseDouble(args.get(2)), Double.parseDouble(args.get(3)));
            } else {
              neighbors = _stars
                  .naiveRadiusCoords(Double.parseDouble(args.get(0)),
                      Double.parseDouble(args.get(1)),
                      Double.parseDouble(args.get(2)), Double.parseDouble(args.get(3)));
            }
            for (Star star : neighbors) {
              cells = cells +
                  generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
                      star.getZ());
            }
            suggestionString = neighbors.size() + " stars found!";
          } else if (args.size() == 2) {
            List<Star> neighbors;
            if (naive == null) {
              neighbors =
                  _stars
                      .radiusName(Double.parseDouble(args.get(0)),
                          args.get(1).replaceAll("\"", ""));
            } else {
              neighbors = _stars
                  .naiveRadiusName(Double.parseDouble(args.get(0)),
                      args.get(1).replaceAll("\"", ""));
            }
            for (Star star : neighbors) {
              cells = cells +
                  generateCell(star.getStarId(), star.getProperName(), star.getX(), star.getY(),
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
              "cells", cells, "stars", _fireflies);

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
