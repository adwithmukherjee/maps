package edu.brown.cs.amukhe12.maps.REPL;

import java.util.List;

/**
 * an interface representing a single event.
 */
public interface IEvent {
  /**
   * causes the event to trigger.
   *
   * @param args - a String list of dependencies for the event
   */
  void execute(List<String> args) throws Exception;

  String id();
}
