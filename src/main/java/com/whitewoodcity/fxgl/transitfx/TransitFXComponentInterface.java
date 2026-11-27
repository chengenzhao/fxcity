package com.whitewoodcity.fxgl.transitfx;

import io.javafx.whitewoodcity.JsonFile;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface TransitFXComponentInterface {

  static Map<String, JsonObject> loadJsonObject(List<JsonFile> jsonFiles) {
    Map<String, JsonObject> actMap = HashMap.newHashMap(32);
    var set = new HashSet<>(jsonFiles);
    for (var a : set) {
      var json = new JsonObject(a.getJsonString());
      actMap.put(a.getFileName(), json);
    }
    return actMap;
  }

  static Map<String, JsonArray> loadJsonArray(List<JsonFile> jsonFiles) {
    Map<String, JsonArray> actMap = HashMap.newHashMap(32);
    var set = new HashSet<>(jsonFiles);
    for (var a : set) {
      var json = new JsonArray(a.getJsonString());
      actMap.put(a.getFileName(), json);
    }
    return actMap;
  }

}
