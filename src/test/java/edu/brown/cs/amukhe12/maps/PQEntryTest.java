package edu.brown.cs.amukhe12.maps;

import edu.brown.cs.amukhe12.maps.graph.DPQEntry;
import edu.brown.cs.amukhe12.maps.maps.MapNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PQEntryTest {
   DPQEntry<MapNode> _pqEntry;

  @Before
  public void setUp() throws Exception {
    _pqEntry = new DPQEntry<>(1.0, new MapNode("0", 1.0, 1.0));
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
      new DPQEntry<>(0.0, new MapNode("1", 0.0, 0.0))) > 0);

    assertTrue(_pqEntry.compareTo(
      new DPQEntry<>(2.0, new MapNode("1", 0.0, 0.0))) < 0);
    tearDown();
  }
}
