package com.whitewoodcity.javafx.jvg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.whitewoodcity.javafx.imageviews.ImageViews;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class JVGs {

  private final List<JVG> jvgs = new ArrayList<>();

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

  public ImageViews toImageViews(){
    List<Image> imgs = jvgs.stream().map(JVG::toImage).map(Image.class::cast).toList();
    return new ImageViews(imgs);
  }

  public JVG getDefaultJVG(){
    return jvgs.getFirst();
  }

  public JVGs move(Point2D p) {
    return move(p.getX(), p.getY());
  }

  public JVGs move(double x, double y) {
    jvgs.forEach(jvg -> jvg.move(x,y));
    return this;
  }

  public JVGs set(double x, double y) {
    jvgs.forEach(jvg -> jvg.set(x,y));
    return this;
  }

  public JVGs trim(){
    jvgs.forEach(JVG::trim);
    return this;
  }

  public JVGs zoom(double factor){
    jvgs.forEach(jvg -> jvg.zoom(factor));
    return this;
  }

  public ArrayNode toJson(){
    var mapper = new ObjectMapper();
    var arrayNode = mapper.createArrayNode();
    for(var jvg:jvgs) {
      arrayNode.add(jvg.toJson());
    }
    return arrayNode;
  }

  public String toJsonString(){
    return toJson().toString();
  }
}
