package edu.brown.cs.amukhe12.maps.REPL;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which runs all of the events that the user has inputted.
 */
public class EventInvoker {
  private final List<EventArgTuple> events = new ArrayList<>();

  /**
   * A Constructor for the EventInvoker class.
   */
  public EventInvoker() {
  }

  /**
   * takes an event, and attaches its dependencies to it by putting them both in an eventArgTuple
   * and adding them to the list of eventArgTuples.
   *
   * @param event - the event whose dependencies are going to be attached
   * @param args  - the dependencies for the inputted event
   */
  public void addEventArgs(IEvent event, List<String> args) {
    events.add(new EventArgTuple(event, args));
  }

  /**
   * triggers every event that the user has inputted by calling their execute methods, then
   * clears the list of events.
   */
  public void triggerEvents() throws Exception {
    for (EventArgTuple e : events) {
      List<String> args = e.getArgs();
      args.remove(0);
      e.getEvent().execute(args);
    }
    events.clear();
  }
}
