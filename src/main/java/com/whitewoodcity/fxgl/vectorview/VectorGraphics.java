package com.whitewoodcity.fxgl.vectorview;

import module java.base;
import module javafx.controls;
import module com.fasterxml.jackson.databind;

public class VectorGraphics extends Group {
  public VectorGraphics(String jsonString) {
    fromJson(_ -> {
      var l = new SVGLayer();
      this.getChildren().add(l);
      return l;
    },jsonString);
  }

  public void trim(){
    var p = getXY();
    getChildren().stream()
      .filter(SVGLayer.class::isInstance)
      .map(SVGLayer.class::cast)
      .forEach(e-> e.trim(p));
  }

  public void move(Point2D p){
    move(p.getX(), p.getY());
  }

  public void move(double x, double y){
    getChildren().stream()
      .filter(SVGLayer.class::isInstance)
      .map(SVGLayer.class::cast)
      .forEach(e -> e.move(x, y));
  }

  public Point2D getXY(){
    double x = 0;
    double y = 0;
    for(var node:getChildren()){
      if(node instanceof SVGLayer layer){
        var p = layer.getMinXY();
        x = Math.min(x, p.getX());
        y = Math.min(y, p.getY());
      }
    }
    return new Point2D(x,y);
  }

  public Dimension2D getDimension(){
    double w = 0;
    double h = 0;
    for(var node:getChildren()){
      if(node instanceof SVGLayer layer) {
        var d = layer.getDimension();
        w = Math.max(w, d.getWidth());
        h = Math.max(h, d.getHeight());
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
      .filter(SVGLayer.class::isInstance)
      .map(SVGLayer.class::cast)
      .forEach(layer -> arrayNode.add(layer.toJson()));
    return arrayNode;
  }

  public String toJsonString(){
    return toJson().toString();
  }

  public static void fromJson(JsonPreset preset, String jsonArray){
    var mapper = new ObjectMapper();
    try {
      SVGLayer parent = null;
      var array = mapper.readTree(jsonArray);
      for(int i=0;i<array.size();i++){
        var obj = (ObjectNode)array.get(i);
        var layer = preset.createBy(obj);
        if(obj.has(SVGLayer.JsonKeys.CLIP.key())){
          assert parent != null;
          assert obj.get(SVGLayer.JsonKeys.CLIP.key()).asBoolean();
          layer.setClip(parent.daemon());
        }else{
          parent = layer;
        }
        layer.fromJson(obj);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FunctionalInterface
  public interface JsonPreset{
    SVGLayer createBy(ObjectNode objectNode);
  }
}