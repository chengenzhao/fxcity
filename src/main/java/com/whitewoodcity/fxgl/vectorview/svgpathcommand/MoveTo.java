package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record MoveTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "M";
  }

  public MoveTo(double x, double y) {
    this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public MoveTo clone() {
    return new MoveTo(getX(), getY());
  }

  @Override
  public void apply(SVGPathElement reference, Apply applyX, Apply applyY) {
    if(reference instanceof MoveTo moveTo){
      x.set(applyX.apply(moveTo.x()));
      y.set(applyY.apply(moveTo.y()));
    }else throw new RuntimeException("type error");
  }
}
