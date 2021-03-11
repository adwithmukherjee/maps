package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.kdtree.KDTree;
import edu.brown.cs.amukhe12.maps.stars.PersonList;
import edu.brown.cs.amukhe12.maps.stars.Star;
import java.util.List;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class ParserTest {

  PersonList _people;

  @Before
  public void setUp() throws Exception {

    _people = new PersonList();

    try{
      CSVParser parser = new CSVParser("people.csv", _people);
    } catch(Exception e){
      assertTrue(e != null);
    }
    CSVParser parser = new CSVParser("data/people/people.csv", _people);
    KDTree<Star> tree = new KDTree<>(3);

  }

  @After
  public void tearDown() {
    _people = null;
  }


  @Test
  public void testSize() throws Exception {
    setUp();
    assertTrue(_people.size() == 1000);
    tearDown();
  }

  @Test
  public void testEmptyFields() throws Exception {
    setUp();
    assertTrue(_people.getPeople().get(15).getFields().get(0).equals("Hyman"));
    assertTrue(_people.getPeople().get(15).getFields().get(2) == null);
    tearDown();
  }


}
