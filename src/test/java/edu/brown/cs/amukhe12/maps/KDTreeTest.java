package edu.brown.cs.amukhe12.maps;


import java.util.Arrays;

import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.kdtree.KDTree;
import edu.brown.cs.amukhe12.maps.kdtree.NodeComparator;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;


import static org.junit.Assert.*;

public class KDTreeTest {


  KDTree<Integer> _tree;

  @Before
  public void setUp() {

    _tree = new KDTree<>(3);

    for(int i = 0; i<10; i++){
      for(int j =0; j<10; j++){
        for(int k =0; k<10; k++){
          KDNode<Integer>
              node = new KDNode<Integer>(Arrays.asList((double)i,(double)j,(double)k), 100*i+10*j+k);
          _tree.insert(node);
        }
      }
    }
  }

  @After
  public void tearDown() {
    _tree.clear();
    assertTrue(_tree.isEmpty());
  }

  @Test
  public void testSize(){
    setUp();
    assertTrue(_tree.size() == 1000);
    tearDown();
  }

  @Test
  public void testTreeProperty(){
    setUp();

    for(KDNode<Integer> node: _tree.getNodes()){
      if(node.hasLeft()){
        assertTrue(new NodeComparator<Integer>(node.getDimension()).compare(node.getLeft(), node) > 0);
      }
      if(node.hasRight()){
        assertTrue(new NodeComparator<Integer>(node.getDimension()).compare(node.getRight(), node) <= 0);
      }
    }
  }


}
