package edu.brown.cs.amukhe12.maps.Events.maps;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.SQLQueries;
import edu.brown.cs.amukhe12.maps.sqlparser.SQLParser;


import java.util.List;

/**
 * MapAction.
 */
public class MapAction implements IEvent {

  private DBReference db;
  private String id;

  /**
   * Constructor.
   * @param database db
   */
  public MapAction(DBReference database) {
    db = database;
    id = "map";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (args.size() != 1) {
      throw new Exception("ERROR: incorrect number of arguments");
    }

    try {
      db.setFilename(args.get(0));
      db.initializeTreeAndGraph();
      new SQLParser(args.get(0), db.getTree()).parse(SQLQueries.SELECT_ALL_TRAVERSABLE_NODES);
    } catch (Exception e) {
      throw new Exception("invalid db path");
    }

    System.out.println("map set to " + args.get(0));
  }

  @Override
  public String id() {
    return id;
  }
}
