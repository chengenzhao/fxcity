package com.whitewoodcity.fxgl.vectorview;

import module java.base;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public class VectorGraphics extends Group {
  private List<SVGLayer> layers = new ArrayList<>();

  public VectorGraphics() {
    this.getChildren().addAll(layers);
  }

  public void trim(){
    var p = getXY();
    for(var layer:layers)
      layer.trim(p.getX(),p.getY());
  }

  public void move(double x, double y){
    layers.forEach(e -> e.move(x, y));
  }

  public Point2D getXY(){
    double x = 0;
    double y = 0;
    for(var layer:layers){
      var p = layer.getMinXY();
      x = Math.min(x, p.getX());
      y = Math.min(y, p.getY());
    }
    return new Point2D(x,y);
  }

  public Dimension2D getDimension(){
    double w = 0;
    double h = 0;
    for(var layer:layers){
      var d = layer.getDimension();
      w = Math.max(w, d.getWidth());
      h = Math.max(h, d.getHeight());
    }
    return new Dimension2D(w,h);
  }
}
