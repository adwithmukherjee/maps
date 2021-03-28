package edu.brown.cs.amukhe12.maps.csvparser;

import java.util.List;

/**
 * CSVEntry.
 */
public interface CSVEntry {

  /**
   * setFields, sets fields.
   * @param fields fields to set
   * @throws Exception throws Exception
   */
  void setFields(List<String> fields) throws Exception;

  /**
   * @return string representation
   */
  String toString();

  /**
   * @return fields
   */
  List getFields();

}
