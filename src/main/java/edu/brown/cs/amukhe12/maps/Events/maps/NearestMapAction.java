package edu.brown.cs.amukhe12.maps.Events.maps;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.maps.DBReference;
import edu.brown.cs.amukhe12.maps.maps.MapNode;


import java.util.List;

/**
 * NearestMapAction.
 */
public class NearestMapAction implements IEvent {

  private DBReference db;
  private String id;

  /**
   * Constructor.
   * @param database db
   */
  public NearestMapAction(DBReference database) {
    db = database;
    id = "nearest";
  }

  @Override
  public void execute(List<String> args) throws Exception {

    if (!db.isInitialized()) {
      throw new Exception("ERROR: no map db set");
    }
    if (args.size() == 2) {

      List<KDNode<MapNode>> nearest = db.getTree()
          .nearest(1, Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)));
      System.out.println(nearest.get(0).getValue().getId());
    } else {
      throw new Exception("ERROR: incorrect number of arguments");
    }
  }

  @Override
  public String id() {
    return id;
  }
}
