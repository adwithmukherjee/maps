package edu.brown.cs.amukhe12.maps;

import java.util.Collections;
import java.util.List;

import edu.brown.cs.amukhe12.maps.csvparser.CSVParser;
import edu.brown.cs.amukhe12.maps.stars.Star;
import edu.brown.cs.amukhe12.maps.stars.StarList;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class MBRadiusTest {

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
  public void testRadiusByCoords() throws Exception {
    setUp();
    double i;
    double j;
    double k;
    double l;
    int m = 0;

    while (m < 10) {
      i = (Math.random() * 20);
      j = Math.random() * 50;
      k = Math.random() * 50;
      l = Math.random() * 50;

      List<Star> set1 = _stars.radiusCoords(i, j, k, l);
      List<Star> set2 = _stars.naiveRadiusCoords(i, j, k, l);

      for (Star star: set1) {
        assertTrue(_stars.distanceBetween(star.getX(), star.getY(), star.getZ(), j, k, l) <= i);
        assertTrue(set2.contains(star));
      }
      m++;
    }
    tearDown();
  }

  @Test
  public void testRadiusByName() throws Exception {
    setUp();

    double i;
    String starName;
    int m = 0;
    while (m < 10) {
      i = (Math.random() * 20) ;
      final Star randomStar =
          _stars.getList().get((int) (Math.random() * (_stars.getList().size() - 1)));
      starName = randomStar.getProperName();



      if (starName != null) {

        List<Star> set1 = _stars.radiusName(i, starName);
        List<Star> set2 = _stars.naiveRadiusName(i, starName);

        for (Star star : set1) {
          assertTrue(_stars.distanceBetween(star.getX(), star.getY(), star.getZ(), randomStar.getX(), randomStar.getY(), randomStar.getZ()) <= i);
          assertTrue(set2.contains(star));
        }
      }
      m++;
    }

    tearDown();

  }

  @Test
  public void exceptions(){
    try{
      _stars.radiusName(-1, "Sol");
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.naiveRadiusName(-1, "Sol");
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.naiveRadiusName(1, "random-name");
    } catch(Exception e){
      assertTrue(e != null);
    }
    try{
      _stars.radiusName(1, "random-name");
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.radiusCoords(-1, 0,0,0);
    } catch(Exception e){
      assertTrue(e != null);
    }

    try{
      _stars.naiveRadiusCoords(-1, 0,0,0);
    } catch(Exception e){
      assertTrue(e != null);
    }
  }


}
