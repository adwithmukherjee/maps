package edu.brown.cs.amukhe12.maps.csvparser;

import edu.brown.cs.amukhe12.maps.EntryList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CSVParser.
 */
public class CSVParser {

  private EntryList eList;

  /**
   * Constructor.
   * @param file filename
   * @param list entryList
   * @throws Exception throws Exception
   */
  public CSVParser(String file, EntryList list) throws Exception {
    eList = list;
    this.parseCSV(file);
  }

  /**
   * @param file path to csv to be parsed. parseCSV then adds as many entries
   *             to the associated EntryList as rows in the CSV
   * @throws Exception throws Exception
   */
  public void parseCSV(String file) throws Exception {
    Scanner in = null;
    try {
      in = new Scanner(new File(file));
      eList.clear();
    } catch (FileNotFoundException e) {
      throw new Exception("ERROR: File does not exist.");
    }
    int i = 0;
    while (in.hasNextLine()) {
      List<String> fields = new ArrayList<String>();
      // Regex to make file consistent.
      String nextLine = in.nextLine() + " ";
      Scanner lineReader = new Scanner(nextLine);
      lineReader.useDelimiter(",");
      while (lineReader.hasNext()) {
        String next = lineReader.next();
        if (next.equals("") || next.equals(" ")) {
          fields.add(null);
        } else {
          fields.add(next);
        }
      }
      String test = "";
      for (int j = 0; j < fields.size(); j++) {
        test = test + fields.get(j);
      }
      if (i != 0) {
        this.addEntry(fields);
      }
      i++;
      lineReader.close();
    }
    in.close();



  }

  /**
   *
   * @param fields adds the parsed fields to the associated EntryList
   * @throws Exception if there is a mismatch with the expected format of the input fields.
   */
  public void addEntry(List<String> fields) throws Exception {
    eList.addEntry(fields);
  }

}
