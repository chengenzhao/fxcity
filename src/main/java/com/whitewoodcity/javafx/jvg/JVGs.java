package com.whitewoodcity.javafx.jvg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class JVGs {

  private List<JVG> jvgs = new ArrayList<>();

  public JVGs(String jsonString){
    fromJson(jvgs, jsonString);
  }

  public static void fromJson(List<JVG> jvgs, String jsonArray) {
    var mapper = new ObjectMapper();
    try {
      fromJson(jvgs, (ArrayNode) mapper.readTree(jsonArray));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void fromJson(List<JVG> jvgs, ArrayNode arrayNode) {
    arrayNode.forEach(n -> {
      var node = (ArrayNode) n;
      var jvg = new JVG(node);
      jvgs.add(jvg);
    });
  }

  public List<JVG> getJVGs() {
    return jvgs;
  }
}
