package edu.brown.cs.amukhe12.maps;

import java.util.List;

/**
 * Entrylist.
 */
public interface EntryList {

  /**
   * addEntry.
   * @param fields fields of entry
   * @throws Exception throws Exception
   */
  void addEntry(List<String> fields) throws Exception;

  /**
   * clears entry.
   */
  void clear();

  /**
   * size.
   * @return int, size
   */
  int size();

  /**
   * isEmpty.
   * @return true if empty false otherwise
   */
  boolean isEmpty();

}
