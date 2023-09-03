package com.whitewoodcity.flame;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Audio {
  private Map<String, Object> loadedFiles = new HashMap<>();

  public void remove(String fileName) {
    loadedFiles.remove(fileName);
  }

  public void clearCache() {
    loadedFiles.clear();
  }

  public List<Object> loadAll(List<String> fileNames) {
    var list = new ArrayList<Object>();
    for(var fileName : fileNames){
      list.add(load(fileName));
    }
    return list;
  }

  public Object load(String fileName) {
    if (!loadedFiles.containsKey(fileName)) {
      loadedFiles.put(fileName,fetchToMemory(fileName));
    }
    return loadedFiles.get(fileName);
  }

  private Object fetchToMemory(String name) {
    if(name.endsWith("mp3")){
      return new Media(getClass().getResource("/audio/"+name).toExternalForm());
    }else{
      return new AudioClip(getClass().getResource("/audio/"+name).toExternalForm());
    }
  }

  public Object get(String fileName){
    if (!loadedFiles.containsKey(fileName)||loadedFiles.get(fileName)==null) {
      System.out.println("file: audio/"+fileName+" doesn't exist reload");
      loadedFiles.put(fileName,fetchToMemory(fileName));
    }
    return loadedFiles.get(fileName);
  }

  public void put(String key, Object audio){
    loadedFiles.put(key, audio);
  }

  public int size(){
    return loadedFiles.size();
  }
}
