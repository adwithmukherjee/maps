package edu.brown.cs.amukhe12.maps;

import java.util.Arrays;

import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.kdtree.NodeComparator;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComparatorTest {


  KDNode<Integer> node1;
  KDNode<Integer> node2;

  @Before
  public void setUp() {
    node1 = new KDNode(Arrays.asList(1.0,2.0,3.0), 1);
    node2 = new KDNode(Arrays.asList(1.0,1.0,4.0), 1);

  }

  @After
  public void tearDown(){
    node1 = null;
    node2 = null;
  }

  @Test
  public void testComparison(){
    setUp();
    assertTrue(new NodeComparator<Integer>(1).compare(node1, node2) == 0);
    assertTrue(new NodeComparator<Integer>(2).compare(node1, node2) > 0);
    assertTrue(new NodeComparator<Integer>(3).compare(node1, node2) < 0);
  }


}
