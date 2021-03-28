package edu.brown.cs.amukhe12.maps.Events.stars;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.stars.Star;
import edu.brown.cs.amukhe12.maps.stars.StarList;

import java.util.List;

/**
 * REPLAction that implements naive neighbors search methods on a provided StarList.
 */
public class NaiveNeighborsAction implements IEvent {

  private StarList stars;
  private String id;

  public NaiveNeighborsAction(StarList starList) {
    stars = starList;
    id = "naive_neighbors";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (stars.isEmpty()) {
      throw new Exception("ERROR: no stars loaded");
    }
    if (args.size() == 4) {
      List<Star> neighbors = stars.naiveNeighborsSearchCoords(Integer.parseInt(args.get(0)),
          Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)),
          Double.parseDouble(args.get(3)));
      for (Star star : neighbors) {
        System.out.println(star.getFields().get(0));
      }
    } else if (args.size() == 2) {
      List<Star> neighbors = stars
          .naiveNeighborsName(Integer.parseInt(args.get(0)), args.get(1).replaceAll("\"", ""));
      for (Star star : neighbors) {
        System.out.println(star.getFields().get(0));
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
