package edu.brown.cs.amukhe12.maps.csvparser;

import java.util.List;

public interface CSVEntry {

  void setFields(List<String> fields) throws Exception;

  String toString();

  List getFields();

}
