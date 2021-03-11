package edu.brown.cs.amukhe12.maps;

import java.util.List;

public interface EntryList {

  void addEntry(List<String> fields) throws Exception;

  void clear();

  int size();

  boolean isEmpty();

}
