package edu.brown.cs.amukhe12.maps.Events.maps;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.MapGraph;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.maps.Way;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteAction implements IEvent {

  private String _id;
  private DBReference _db;

  public RouteAction(DBReference db) {
    _id = "route";
    _db = db;
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (!_db.isInitialized()) {
      throw new Exception("ERROR: no map db loaded");
    }
    if (args.size() == 4) {

      List<String> command = new ArrayList<>();
      command.add("route");
      for(String arg: args){
        command.add(arg);
      }
      List<String> retrieved = _db.retrieve(command);
      if(retrieved != null){
        for(String el: retrieved){
          System.out.println(el);
        }
      } else {
        MapGraph _map = _db.getGraph();
        try {

          double lat1 = Double.parseDouble(args.get(0));
          double long1 = Double.parseDouble(args.get(1));
          double lat2 = Double.parseDouble(args.get(2));
          double long2 = Double.parseDouble(args.get(3));

          double minLong = Math.min(long1, long2);
          double maxLong = Math.max(long1, long2);
          double minLat = Math.min(lat1, lat2);
          double maxLat = Math.max(lat1, lat2);

          MapNode node1 = _db.getTree().nearest(1, lat1, long1).get(0).getValue();
          MapNode node2 = _db.getTree().nearest(1, lat2, long2).get(0).getValue();


          List<Way> route =
              _map.routeFromNodeIds(node1.getId(), node2.getId());
          List<String> results = new ArrayList<>();

          for (Way way : route) {
            String val = "" + way.from().getId() + " -> " + way.to().getId() + " : " + way.getId();
            System.out
                .println(val);
            results.add(val);
          }
          if (!node1.equals(node2) && route.isEmpty()) {
            System.out.println("" + node1.getId() + " -/- " + node2.getId());
          }
          _map.clearEdges();
          _db.cache(command, results);

        } catch (Exception e) {
          try {
            String str11 = args.get(0).replaceAll("\"", "");
            String str12 = args.get(1).replaceAll("\"", "");
            String str21 = args.get(2).replaceAll("\"", "");
            String str22 = args.get(3).replaceAll("\"", "");

            List<String> node1Ids = new SQLParser(_db.getFilename(), null)
                .parseAndReturnList(SQLQueries.streetIntersect(str11, str12)).get(0);

            List<String> node2Ids = new SQLParser(_db.getFilename(), null)
                .parseAndReturnList(SQLQueries.streetIntersect(str21, str22)).get(0);

            String node1Id = "";
            String node2Id = "";
            for (int i = 0; i < 2; i++) {
              for (int j = 0; j < 2; j++) {
                if (node1Ids.get(i).equals(node1Ids.get(2 + j))) {
                  node1Id = node1Ids.get(i);
                }
                if (node2Ids.get(i).equals(node2Ids.get(2 + j))) {
                  node2Id = node2Ids.get(i);
                }
              }
            }

            if (node1Id.equals("") || node2Id.equals("")) {
              throw new Exception("no intersection found");
            }

            MapNode node1 = _map.getNodeFromId(node1Id);
            MapNode node2 = _map.getNodeFromId(node2Id);

            double lat1 = node1.getCoords().get(0);
            double long1 = node1.getCoords().get(1);
            double lat2 = node2.getCoords().get(0);
            double long2 = node2.getCoords().get(1);
            double minLong = Math.min(long1, long2);
            double maxLong = Math.max(long1, long2);
            double minLat = Math.min(lat1, lat2);
            double maxLat = Math.max(lat1, lat2);

            double deltaLat = Math.abs(maxLat - minLat) / 2;
            double deltaLong = Math.abs(maxLong - minLong) / 2;

//            new SQLParser(_db.getFilename(), _map)
//                .parse(
//                    SQLQueries.routeEdges(maxLat + deltaLat, minLong - deltaLong, minLat - deltaLat,
//                        maxLong + deltaLong));

            List<Way> route =
                _map.routeFromNodeIds(node1.getId(), node2.getId());
            List<String> results = new ArrayList<>();

            for (Way way : route) {
              String val = "" + way.from().getId() + " -> " + way.to().getId() + " : " + way.getId();
              System.out.println(val);
              results.add(val);

            }
            if (!node1.equals(node2) && route.isEmpty()) {
              System.out.println("" + node1.getId() + " -/- " + node2.getId());
            }
            _map.clearEdges();
            _db.cache(command, results);
          } catch (Exception e2) {
            e2.printStackTrace();
            throw new Exception("incorrect arg format");

          }
        }
      }
    } else {
      throw new Exception("incorrect number of arguments");
    }

  }

  @Override
  public String id() {
    return _id;
  }
}
