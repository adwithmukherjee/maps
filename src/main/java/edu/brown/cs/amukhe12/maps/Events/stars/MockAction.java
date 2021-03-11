package edu.brown.cs.amukhe12.maps.Events.stars;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.stars.PersonList;

import java.util.List;

/**
 * REPLAction that loads in MockPerson data to a givne PersonList via a .csv argument
 */
public class MockAction implements IEvent {
  private EntryList _people;
  private String _id;

  public MockAction(PersonList people) {
    _people = people;
    _id = "mock";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    CSVParser personParser = new CSVParser(args.get(0), _people);
    System.out.println("Read " + _people.size() + " people from " + args.get(0));
  }

  @Override
  public String id() {
    return _id;
  }
}
