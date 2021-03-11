package edu.brown.cs.amukhe12.maps.Events.stars;

import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.stars.StarList;

import java.util.List;

/**
 * REPLAction that loads in Star data to a given StarList via a .csv argument
 */
public class StarsAction implements IEvent {
  private StarList _stars;
  private String _id;

  public StarsAction(StarList stars) {
    _stars = stars;
    _id = "ERROR: stars";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    if (args.size() != 1) {
      throw new Exception("ERROR: incorrect number of arguments");
    }
    CSVParser starsParser = new CSVParser(args.get(0), _stars);
    //_stars.generateKDTree();
    System.out.println("Read " + _stars.size() + " stars from " + args.get(0));
  }

  @Override
  public String id() {
    return _id;
  }
}

