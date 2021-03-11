package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.graph.Graph;
import edu.brown.cs.amukhe12.maps.graph.PQEntry;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import edu.brown.cs.amukhe12.maps.maps.Way;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PQEntryTest {
   PQEntry<MapNode> _pqEntry;

  @Before
  public void setUp() throws Exception {
    _pqEntry = new PQEntry<>(1.0, new MapNode("0", 1.0, 1.0));
  }

  @After
  public void tearDown() {
    _pqEntry.setKey(0.0);
  }

  @Test
  public void testGetKey() throws Exception {
    setUp();
    assertEquals(1.0, _pqEntry.getKey(), 0.0);
    tearDown();
  }

  @Test
  public void testCompareTo() throws Exception {
    setUp();
    assertTrue(_pqEntry.compareTo(
      new PQEntry<>(0.0, new MapNode("1", 0.0, 0.0))) > 0);

    assertTrue(_pqEntry.compareTo(
      new PQEntry<>(2.0, new MapNode("1", 0.0, 0.0))) < 0);
    tearDown();
  }
}
