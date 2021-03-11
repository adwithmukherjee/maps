package edu.brown.cs.amukhe12.maps.Events.maps;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;


import java.util.List;

public class MapAction implements IEvent {

  private DBReference _db;
  private String _id;

  public MapAction(DBReference db) {
    _db = db;
    _id = "map";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (args.size() != 1) {
      throw new Exception("ERROR: incorrect number of arguments");
    }

    try {
      _db.setFilename(args.get(0));
      _db.initializeTreeAndGraph();
      new SQLParser(args.get(0), _db.getTree()).parse(SQLQueries.SELECT_ALL_TRAVERSABLE_NODES);
    } catch (Exception e) {
      throw new Exception("invalid db path");
    }

    System.out.println("map set to " + args.get(0));
  }

  @Override
  public String id() {
    return _id;
  }
}
