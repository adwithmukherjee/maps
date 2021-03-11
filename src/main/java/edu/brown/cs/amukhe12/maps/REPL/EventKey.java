package edu.brown.cs.amukhe12.maps.REPL;

/**
 * a class representing an event and its corresponding trigger name.
 */
public class EventKey {
  private final String k;
  private final IEvent v;

  /**
   * constructor for an eventKey.
   *
   * @param k - the String representing the name of the event
   * @param v - the event
   */
  public EventKey(String k, IEvent v) {
    this.k = k;
    this.v = v;
  }

  /**
   * gets the name of the event.
   *
   * @return String - the trigger name associated with the event
   */
  public String getK() {
    return k;
  }

  /**
   * gets the event of the eventKey.
   *
   * @return IEvent - the event in the eventKey
   */
  public IEvent getV() {
    return v;
  }
}
