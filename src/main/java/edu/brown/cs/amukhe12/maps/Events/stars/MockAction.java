package edu.brown.cs.amukhe12.maps.Events.stars;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.REPL.IEvent;
import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.stars.PersonList;

import java.util.List;

/**
 * REPLAction that loads in MockPerson data to a givne PersonList via a .csv argument.
 */
public class MockAction implements IEvent {
  private EntryList people;
  private String id;

  /**
   * Constructor.
   * @param personList pL
   */
  public MockAction(PersonList personList) {
    people = personList;
    id = "mock";
  }

  @Override
  public void execute(List<String> args) throws Exception {
    CSVParser personParser = new CSVParser(args.get(0), people);
    System.out.println("Read " + people.size() + " people from " + args.get(0));
  }

  @Override
  public String id() {
    return id;
  }
}
