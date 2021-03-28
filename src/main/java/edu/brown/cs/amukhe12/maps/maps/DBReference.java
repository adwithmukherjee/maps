package edu.brown.cs.amukhe12.maps.maps;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBReference {

  private String filename;
  private boolean isInitialized;
  private MapTree tree;
  private MapGraph graph;
  private LoadingCache<List<String>, List<String>> queryCache;


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

  public void setFilename(String fName) throws Exception {
    filename = fName;
    isInitialized = true;


  }

  public String getFilename() {
    return filename;
  }

  public void initializeTreeAndGraph() {
    graph = new MapGraph(this);
    tree = new MapTree(graph);
  }

  public MapTree getTree() {
    return tree;
  }

  public MapGraph getGraph() {
    return graph;
  }

  public boolean isInitialized() {
    return isInitialized;
  }


  public void cache(List<String> args, List<String> results) throws ExecutionException {
    queryCache.put(args, results);
  }

  public List<String> retrieve(List<String> k) {
    return queryCache.getIfPresent(k);
  }


}
