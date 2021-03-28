package edu.brown.cs.amukhe12.maps.Events.maps;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * WaysAction.
 */
public class WaysAction implements IEvent {

  private String id;
  private DBReference db;

  /**
   * Constructor.
   * @param database db.
   */
  public WaysAction(DBReference database) {
    id = "ways";
    db = database;
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (!db.isInitialized()) {
      throw new Exception("ERROR: no db set");
    }

    if (args.size() == 4) {

      List<String> command = new ArrayList<>();
      command.add("ways");
      for (String arg : args) {
        command.add(arg);
      }
      List<String> retrieved = db.retrieve(command);
      if (retrieved != null) {
        for (String el : retrieved) {
          System.out.println(el);
        }
      } else {
        Double lat1 = Double.parseDouble(args.get(0));
        Double long1 = Double.parseDouble(args.get(1));
        Double lat2 = Double.parseDouble(args.get(2));
        Double long2 = Double.parseDouble(args.get(3));
        //System.out.println("" + lat1 + ", " + long1 + ", " + lat2 + ", " + long2);
        List<List<String>> ids = (new SQLParser(db.getFilename(), null))
            .parseAndReturnList(SQLQueries.ways(lat1, long1, lat2, long2));
//      List<Way> ways =
//          _map.ways(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)),
//              Double.parseDouble(args.get(2)), Double.parseDouble(args.get(3)));
//      for (Way way : ways) {
//        ids.add(way.getId());
//      }
//      Collections.sort(ids);
        List<String> results = new ArrayList<>();
        for (List<String> eachId : ids) {
          System.out.println(eachId.get(0));
          results.add(eachId.get(0));
        }
        db.cache(command, results);
      }
    } else {
      throw new Exception("ERROR: incorrect number of arguments");
    }
  }

  @Override
  public String id() {
    return id;
  }
}

