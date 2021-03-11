package edu.brown.cs.amukhe12.maps.maps;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBReference {

  private String _filename;
  private boolean _isInitialized;
  private MapTree _tree;
  private MapGraph _graph;
  private LoadingCache<List<String>, List<String>> _queryCache;


  public DBReference() {
    _filename = "";
    _isInitialized = false;
    String spec = "maximumSize=10000,expireAfterWrite=10m";
    _queryCache = CacheBuilder.from(spec)
        .build(
            new CacheLoader<List<String>, List<String>>() {
              @Override
              public List<String> load(List<String> strings) throws Exception {
                return null;
              }
            });
  }

  public void setFilename(String filename) throws Exception {
    _filename = filename;
    _isInitialized = true;


  }

  public String getFilename() {
    return _filename;
  }

  public void initializeTreeAndGraph() {
    _graph = new MapGraph();
    _tree = new MapTree(_graph);
  }

  public MapTree getTree() {
    return _tree;
  }

  public MapGraph getGraph() {
    return _graph;
  }

  public boolean isInitialized() {
    return _isInitialized;
  }


  public void cache(List<String> args, List<String> results) throws ExecutionException {
    _queryCache.put(args, results);
  }

  public List<String> retrieve(List<String> k) {
    return _queryCache.getIfPresent(k);
  }


}
