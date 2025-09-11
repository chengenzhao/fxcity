package com.whitewoodcity.fxgl.vectorview;

import module java.base;
import module javafx.controls;
import module com.fasterxml.jackson.databind;

public class JVG extends Group {

  public JVG(String jsonString) {

    var reference = new SimpleObjectProperty<JVGLayer>();

    fromJson(obj -> {
      var l = new JVGLayer();
      this.getChildren().add(l);
      if(obj.has(JVGLayer.JsonKeys.CLIP.key())){
        l.setClip(reference.get().daemon());
      }else{
        reference.set(l);
      }
      return l;
    },jsonString);
  }

  public void trim(){
    var p = getXY();
    getChildren().stream()
      .filter(JVGLayer.class::isInstance)
      .map(JVGLayer.class::cast)
      .forEach(e-> e.trim(p).update());
  }

  public void move(Point2D p){
    move(p.getX(), p.getY());
  }

  public void move(double x, double y){
    getChildren().stream()
      .filter(JVGLayer.class::isInstance)
      .map(JVGLayer.class::cast)
      .forEach(e -> e.move(x, y).update());
  }

  public Point2D getXY(){
    boolean firstLayer = true;
    double x = 0, y = 0;
    for(var node:getChildren()){
      if(node instanceof JVGLayer layer){
        var p = layer.getMinXY();
        if(firstLayer){
          x = p.getX();
          y = p.getY();
          firstLayer = false;
        }else{
          x = Math.min(x, p.getX());
          y = Math.min(y, p.getY());
        }
      }
    }
    return new Point2D(x,y);
  }

  public void set(double x, double y){
    var p = getXY();
    var dx = x - p.getX();
    var dy = y - p.getY();
    move(dx, dy);
  }

  public Dimension2D getDimension(){
    double w = 0;
    double h = 0;
    var point = getXY();
    var x = point.getX();
    var y = point.getY();
    for(var node:getChildren()){
      if(node instanceof JVGLayer layer) {
        var p = layer.getMaxXY();
        w = Math.max(w, p.getX() - x);
        h = Math.max(h, p.getY() - y);
      }
    }
    return new Dimension2D(w,h);
  }

  public ArrayNode toJson(){
    return toJson(this.getChildren());
  }

  public static ArrayNode toJson(ObservableList<Node> children){
    var mapper = new ObjectMapper();
    var arrayNode = mapper.createArrayNode();
    children.stream()
      .filter(JVGLayer.class::isInstance)
      .map(JVGLayer.class::cast)
      .forEach(layer -> arrayNode.add(layer.toJson()));
    return arrayNode;
  }

  public String toJsonString(){
    return toJson().toString();
  }

  public static void fromJson(JsonPreset preset, String jsonArray) {
    var mapper = new ObjectMapper();
    try{
      fromJson(preset, (ArrayNode) mapper.readTree(jsonArray));
    }catch (Exception e){
      throw new RuntimeException(e);
    }
  }
  public static void fromJson(JsonPreset preset, ArrayNode arrayNode){
    arrayNode.forEach(n -> {
      var obj = (ObjectNode)n;
      var layer = preset.create(obj);
      layer.fromJson(obj);
    });
  }

  @FunctionalInterface
  public interface JsonPreset{
    JVGLayer create(ObjectNode objectNode);
  }

  public JVG copy(){
    return new JVG(this.toJsonString());
  }
}