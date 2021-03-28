package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.kdtree.KDNode;
import edu.brown.cs.amukhe12.maps.kdtree.KDTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Stores a collection of Stars, both as a List, and in a KDTree. Support various
 * radius/neighbor queries for both.
 */
public class StarList implements EntryList {

  private List<Star> stars;

  private KDTree<Star> starTree;

  public StarList() {
    stars = new ArrayList<>();
    starTree = new KDTree<>(3);
  }

  public List<Star> getList() {
    return stars;
  }

  public void clear() {
    stars.clear();
    starTree = new KDTree<>(3);
  }


  public int size() {
    return stars.size();
  }


  public boolean isEmpty() {
    return stars.isEmpty();
  }

  /**
   * Creates a new star and adds it to the KDTree and List with the provided fields.
   * Throws an exception if there is a type mismatch.
   *
   * @param fields
   * @throws Exception
   */
  public void addEntry(List<String> fields) throws Exception {
    Star entry = new Star();
    entry.setFields(fields);
    stars.add(entry);

    KDNode<Star> node =
        new KDNode<Star>(Arrays.asList(entry.getX(), entry.getY(), entry.getZ()), entry);
    starTree.insert(node);
  }

  /**
   * Returns the instance's associated KDTree.
   *
   * @return KDTree instance variable
   */
  public KDTree<Star> getTree() {
    return starTree;
  }

  /**
   * Uses a KD-Tree to calculate and return a List of Stars less than radius away from starName.
   *
   * @param radius
   * @param starName
   * @return a List of Stars less than radius away from starName.
   * @throws Exception
   */
  public List<Star> radiusName(double radius, String starName) throws Exception {

    if (radius < 0) {
      throw new Exception("ERROR: enter non-negative radius");
    }
    double x1 = 0;
    double y1 = 0;
    double z1 = 0;
    double[] nameFound = getCoordsFromName(starName);
    if (nameFound == null) {
      throw new Exception("no such star with name " + starName);
    } else {
      x1 = nameFound[0];
      y1 = nameFound[1];
      z1 = nameFound[2];
    }
    List<Star> neighbors = this.radiusCoords(radius, x1, y1, z1);
    return neighbors.subList(1, neighbors.size());

  }

  /**
   * Uses a KDTree to calculate and return a List of Stars less than radius away from (x1,y1,z1).
   *
   * @param radius
   * @param x1
   * @param y1
   * @param z1
   * @return  a List of Stars less than radius away from (x1,y1,z1).
   * @throws Exception
   */
  public List<Star> radiusCoords(double radius, double x1, double y1, double z1) throws Exception {

    if (radius < 0) {
      throw new Exception("enter non-negative radius");
    }
    List<Double> coords = Arrays.asList(x1, y1, z1);
    List<KDNode<Star>> nodes = starTree.radius(radius, coords);
    List<Star> neighbors = new ArrayList<>();
    for (KDNode<Star> node : nodes) {
      neighbors.add(node.getValue());
    }
    return neighbors;

  }

  /**
   * Uses a KDTree to find the num closest neighbors to the point (x1,y1,z1).
   *
   * @param num
   * @param x1
   * @param y1
   * @param z1
   * @return  the num closest neighbors to the point (x1,y1,z1).
   * @throws Exception
   */
  public List<Star> neighborsSearchCoords(int num, double x1, double y1, double z1)
      throws Exception {

    if (num < 0) {
      throw new Exception("enter non-negative number of neighbors");
    }
    List<Double> coords = Arrays.asList(x1, y1, z1);
    List<KDNode<Star>> nodes = starTree.nearestNeighbors(num, coords);
    List<Star> neighbors = new ArrayList<>();
    for (KDNode<Star> node : nodes) {
      neighbors.add(node.getValue());
    }
    return neighbors;
  }

  /**
   * Uses a KDTree to find the num closest neighbors to the star with proper name starName.
   *
   * @param num
   * @param starName
   * @return the num closest neighbors to the star with proper name starName.
   * @throws Exception
   */
  public List<Star> neighborsSearchName(int num, String starName) throws Exception {
    if (num < 0) {
      throw new Exception("enter non-negative number of neighbors");
    }
    double x1 = 0;
    double y1 = 0;
    double z1 = 0;
    double[] nameFound = getCoordsFromName(starName);
    if (nameFound == null) {
      throw new Exception("no such star with name " + starName);
    } else {
      x1 = nameFound[0];
      y1 = nameFound[1];
      z1 = nameFound[2];
    }

    this.sortRelativeTo(x1, y1, z1);

    if (num + 1 > stars.size()) {
      num = stars.size() - 1;
    }

    List<Star> neighbors = this.neighborsSearchCoords(num + 1, x1, y1, z1);

    return neighbors.subList(1, neighbors.size());
  }

  /**
   * Uses a naive List search to return a List of the num closes Stars to the coords (x1,y1,z1).
   *
   * @param num
   * @param x1
   * @param y1
   * @param z1
   * @return a list of num Stars closest to the provided coordinate in 3-space.
   * @throws Exception
   */
  public List<Star> naiveNeighborsSearchCoords(int num, double x1, double y1, double z1)
      throws Exception {

    if (num < 0) {
      throw new Exception("enter non-negative number of neighbors");
    }

    if (num > stars.size()) {
      num = stars.size();
    }

    this.sortRelativeTo(x1, y1, z1);

    StarComparator comparator = new StarComparator(x1, y1, z1);
    List<Integer> starsOfSameDist = new ArrayList<>();

    Star lastStar = stars.get(num - 1);

    for (int i = 0; i < stars.size(); i++) {
      Star star = stars.get(i);
      if (comparator.compare(lastStar, star) == 0) {
        starsOfSameDist.add(i);
      }
    }
    for (int i : starsOfSameDist) {
      int randomIndex = new Random()
          .nextInt(starsOfSameDist.size());
      Collections.swap(stars, i, starsOfSameDist.get(randomIndex));
    }
    return stars.subList(0, num);
  }

  /**
   * uses a naive search approach to find and return a List of the num closest stars to the Star.
   * with proper name starName.
   *
   * @param num
   * @param starName
   * @return a list of num stars closest to the provided star.
   * @throws Exception
   */
  public List<Star> naiveNeighborsName(int num, String starName) throws Exception {
    if (num < 0) {
      throw new Exception("enter non-negative number of neighbors");
    }

    double x1 = 0;
    double y1 = 0;
    double z1 = 0;
    double[] nameFound = getCoordsFromName(starName);
    if (nameFound == null) {
      throw new Exception("no such star with name " + starName);
    } else {
      x1 = nameFound[0];
      y1 = nameFound[1];
      z1 = nameFound[2];

    }
    this.sortRelativeTo(x1, y1, z1);

    if (num + 1 > stars.size()) {
      num = stars.size() - 1;
    }

    List<Star> neighbors = this.naiveNeighborsSearchCoords(num + 1, x1, y1, z1);

    return neighbors.subList(1, neighbors.size());
  }

  /**
   * Uses a naive search approach to return a List of all Stars less than radius away from (x,y,z).
   *
   * @param radius
   * @param x
   * @param y
   * @param z
   * @return a List of all Stars less than radius away from (x,y,z).
   * @throws Exception
   */
  public List<Star> naiveRadiusCoords(double radius, double x, double y, double z)
      throws Exception {

    if (radius < 0) {
      throw new Exception("enter non-negative radius");
    }
    this.sortRelativeTo(x, y, z);
    List<Star> neighbors = new ArrayList<>();
    int i = 0;
    Star star = stars.get(0);
    while (this.distanceBetween(x, y, z, star.getX(), star.getY(), star.getZ()) <= radius
        && i < stars.size()) {
      neighbors.add(star);
      i++;
      if (i < stars.size()) {
        star = stars.get(i);
      }
    }
    return neighbors;


  }

  /**
   * Searches a KDTree and returns a List of all Stars less than radius away from (x,y,z).
   *
   * @param radius
   * @param starName
   * @return a list of Stars within the radius away from the provided Star.
   * @throws Exception
   */
  public List<Star> naiveRadiusName(double radius, String starName) throws Exception {
    if (radius < 0) {
      throw new Exception("enter non-negative radius");
    }
    double x1 = 0;
    double y1 = 0;
    double z1 = 0;
    double[] nameFound = getCoordsFromName(starName);
    if (nameFound == null) {
      throw new Exception("no such star with name " + starName);
    } else {
      x1 = nameFound[0];
      y1 = nameFound[1];
      z1 = nameFound[2];

    }
    List<Star> neighbors = this.naiveRadiusCoords(radius, x1, y1, z1);
    return neighbors.subList(1, neighbors.size());


  }

  /**
   * provides the coordinates of the Star with proper name starName.
   *
   * @param starName
   * @return true if star is found, false if otherwise.
   */
  private double[] getCoordsFromName(String starName) {
    boolean nameFound = false;
    double x1 = 0;
    double y1 = 0;
    double z1 = 0;
    for (Star star : stars) {
      if (star.getProperName() != null && star.getProperName().equals(starName)) {
        x1 = star.getX();
        y1 = star.getY();
        z1 = star.getZ();
        nameFound = true;
      }
    }
    if (nameFound) {
      return new double[] {x1, y1, z1};
    } else {
      return null;
    }
  }

  /**
   * uses distance formula to find distance between two points in 3-space.
   *
   * @param x1
   * @param y1
   * @param z1
   * @param x2
   * @param y2
   * @param z2
   * @return distance between two provided Star coords
   */
  public double distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
  }

  /**
   * in-place sorts the StarList from smallest to largest distance from origin.
   *
   * @param xOrigin
   * @param yOrigin
   * @param zOrigin
   */
  private void sortRelativeTo(double xOrigin, double yOrigin, double zOrigin) {
    Collections.sort(stars, new StarComparator(xOrigin, yOrigin, zOrigin));
  }

}





