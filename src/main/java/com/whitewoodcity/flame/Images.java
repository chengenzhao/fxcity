package com.whitewoodcity.flame;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Images {
  private Map<String, Image> loadedFiles = new HashMap<>();

  public void remove(String fileName) {
    loadedFiles.remove(fileName);
  }

  public void clearCache() {
    loadedFiles.clear();
  }

  public List<Image> loadAll(List<String> fileNames) {
    var list = new ArrayList<Image>();
    for(var fileName : fileNames){
      list.add(load(fileName));
    }
    return list;
  }

  public Image load(String fileName) {
    if (!loadedFiles.containsKey(fileName)) {
//      synchronized (this){
        loadedFiles.put(fileName,fetchToMemory(fileName));
//      }
    }
    return loadedFiles.get(fileName);
  }

  private Image fetchToMemory(String name) {
    return new Image("images/"+name);
  }

  public Image get(String fileName){
    if (!loadedFiles.containsKey(fileName)||loadedFiles.get(fileName)==null) {
      System.out.println("file: image/"+fileName+" doesn't exist reload");
      loadedFiles.put(fileName,fetchToMemory(fileName));
    }
    return loadedFiles.get(fileName);
  }

  public void put(String key, Image image){
    loadedFiles.put(key, image);
  }

  public int size(){
    return loadedFiles.size();
  }

  public boolean isEmpty(){
    return loadedFiles.isEmpty();
  }
}
