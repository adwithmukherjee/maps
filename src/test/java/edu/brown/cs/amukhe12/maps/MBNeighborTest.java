package edu.brown.cs.amukhe12.maps;

import java.util.Collections;
import java.util.List;

import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.stars.Star;
import edu.brown.cs.amukhe12.maps.stars.StarComparator;
import edu.brown.cs.amukhe12.maps.stars.StarList;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class MBNeighborTest {


  StarList _stars;

  @Before
  public void setUp() throws Exception {
    _stars = new StarList();
    new CSVParser("data/stars/stardata.csv", _stars);

  }

  @After
  public void tearDown() {
    _stars = null;
  }

  @Test
  public void testNeighborsByCoords() throws Exception {
    setUp();
    int i;
    double j;
    double k;
    double l;
    int m = 0;


    while (m < 10) {
      i = (int) (Math.random() * 50);
      j = Math.random() * 50;
      k = Math.random() * 50;
      l = Math.random() * 50;

      List<Star> set1 = _stars.neighborsSearchCoords(i, j, k, l);
      List<Star> set2 = _stars.naiveNeighborsSearchCoords(i, j, k, l);

      StarComparator comp = new StarComparator(j, k, l);

      assertTrue(set1.size() <= i);
      assertTrue(set2.size() <= i);

      for (Star star : _stars.getList()) {
        assertTrue(comp.compare(set1.get(set1.size() - 1), star) <= 0 || set1.contains(star));
        assertTrue(comp.compare(set2.get(set2.size() - 1), star) <= 0 || set2.contains(star));
      }
      m++;
    }
    tearDown();

  }

  @Test
  public void testNeighborsByName() throws Exception {
    setUp();

    int i;
    String starName;
    int m = 0;


    while (m < 10) {
      i = (int) (Math.random() * 500) + 1;
      final Star randomStar =
          _stars.getList().get((int) (Math.random() * (_stars.getList().size() - 1)));
      starName = randomStar.getProperName();


      if (starName != null) {

        List<Star> set1 = _stars.neighborsSearchName(i, starName);
        List<Star> set2 = _stars.naiveNeighborsName(i, starName);

        StarComparator comp = new StarComparator(randomStar.getX(), randomStar.getY(),
            randomStar.getZ());

        Collections.sort(set1, comp);
        Collections.sort(set2, comp);

        assertTrue(set1.size() <= i);
        assertTrue(set2.size() <= i);

        for (Star star : _stars.getList()) {
          if (set1.size() > 0) {
            assertTrue(comp.compare(set1.get(set1.size() - 1), star) <= 0 || set1.contains(star) ||
                star == randomStar);
          }
          if (set2.size() > 0) {
            assertTrue(comp.compare(set2.get(set2.size() - 1), star) <= 0 || set2.contains(star) ||
                star == randomStar);
          }
        }
      }
      m++;
    }

    tearDown();

  }

  @Test
  public void exceptions() throws Exception {
    setUp();
    try{
      _stars.neighborsSearchCoords(-1, 0,0,0);
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.naiveNeighborsSearchCoords(-1, 0,0,0);
    } catch(Exception e){
      assertTrue(e != null);
    }
    try{
      _stars.neighborsSearchName(-1, "Sol");
    } catch(Exception e){
      assertTrue(e != null);
    }
    try{
      _stars.naiveNeighborsName(-1, "Sol");
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.naiveNeighborsName(1, "random-name");
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.neighborsSearchName(1, "random-name");
    } catch(Exception e){
      assertTrue(e != null);
    }
    tearDown();
  }


}
