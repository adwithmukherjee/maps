package edu.brown.cs.amukhe12.maps.maps;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * DBReference.
 */
public class DBReference {

  private String filename;
  private boolean isInitialized;
  private MapTree tree;
  private MapGraph graph;
  private LoadingCache<List<String>, List<String>> queryCache;


  /**
   * Constructor.
   */
  public DBReference() {
    filename = "";
    isInitialized = false;
    String spec = "maximumSize=10000,expireAfterWrite=10m";
    queryCache = CacheBuilder.from(spec)
        .build(
            new CacheLoader<List<String>, List<String>>() {
              @Override
              public List<String> load(List<String> strings) throws Exception {
                return null;
              }
            });
  }

  /**
   * @param fName filename.
   * @throws Exception throws exception.
   */
  public void setFilename(String fName) throws Exception {
    filename = fName;
    isInitialized = true;


  }

  /**
   * @return filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * initializes tree and graph.
   */
  public void initializeTreeAndGraph() {
    graph = new MapGraph(this);
    tree = new MapTree(graph);
  }

  /**
   * @return tree
   */
  public MapTree getTree() {
    return tree;
  }

  /**
   * @return graph
   */
  public MapGraph getGraph() {
    return graph;
  }

  /**
   * @return true if initialized
   */
  public boolean isInitialized() {
    return isInitialized;
  }


  /**
   * cache.
   * @param args args.
   * @param results res.
   * @throws ExecutionException throws execution exception.
   */
  public void cache(List<String> args, List<String> results) throws ExecutionException {
    queryCache.put(args, results);
  }

  /**
   * @param k key
   * @return value of key k
   */
  public List<String> retrieve(List<String> k) {
    return queryCache.getIfPresent(k);
  }


}
