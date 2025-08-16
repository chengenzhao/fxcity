package com.whitewoodcity.fxgl.vectorview;

import module java.base;
import javafx.scene.Group;

public class VectorGraphics extends Group {
  private List<SVGLayer> layers = new ArrayList<>();

  public VectorGraphics() {
    this.getChildren().addAll(layers);
  }

  public void trim(){
    double minX = 0;
    double minY = 0;
    for(var layer:layers){
      minX = Math.min(minX, layer.getMinX());
      minY = Math.min(minY, layer.getMinY());
    }
    for(var layer:layers)
      layer.trim(minX, minY);
  }
}
