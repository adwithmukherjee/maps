package edu.brown.cs.amukhe12.maps.REPL;

import java.util.List;

/**
 * a class representing an event and its arguments.
 */
public class EventArgTuple {
  private final IEvent event;
  private final List<String> args;

  /**
   * constructor for an eventArgTuple.
   *
   * @param event - the event
   * @param args  - an array of Strings of the arguments of the event
   */
  public EventArgTuple(IEvent event, List<String> args) {
    this.event = event;
    this.args = args;
  }

  /**
   * gets the event.
   *
   * @return IEvent - the event of the eventArgTuple
   */
  public IEvent getEvent() {
    return event;
  }

  /**
   * gets the arguments of the event.
   *
   * @return String[] - an array of the arguments associated with the event
   */
  public List<String> getArgs() {
    return args;
  }
}
