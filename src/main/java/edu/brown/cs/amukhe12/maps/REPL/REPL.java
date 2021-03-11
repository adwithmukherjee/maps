package edu.brown.cs.amukhe12.maps.REPL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * a class representing a REPL where a user can input commands.
 */
public class REPL {
  /**
   * Constructor for a REPL.
   *
   * @param startingEvents - A list of eventKeys which have been created ahead of time
   */
  public REPL(List<EventKey> startingEvents) {
    HashMap<String, IEvent> eventHashMap = new HashMap<>();
    for (EventKey e : startingEvents) {
      eventHashMap.put(e.getK(), e.getV());
    }

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String ln;
      while (true) {
        EventInvoker executeEvents = new EventInvoker();
        ln = reader.readLine();
        if (ln == null) {
          System.exit(0);
        } else {
          String[] lnSplit = ln.split(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
          if (eventHashMap.containsKey(lnSplit[0])) {
            executeEvents.addEventArgs(eventHashMap.get(lnSplit[0]),
              new ArrayList<>(Arrays.asList(lnSplit)));
          } else {
            System.err.println("ERROR: not a registered event");
          }
        }
        try {
          executeEvents.triggerEvents();
        } catch (Exception e) {
          System.out.println("ERROR: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("ERROR: could not read input");
    }
  }
}
