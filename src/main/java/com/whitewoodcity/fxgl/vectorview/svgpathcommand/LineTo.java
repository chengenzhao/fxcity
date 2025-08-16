package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record LineTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "L";
  }

  public LineTo(double x, double y) {
    this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public LineTo clone() {
    return new LineTo(getX(), getY());
  }

  @Override
  public void apply(SVGPathElement reference, Apply applyX, Apply applyY) {
    if(reference instanceof LineTo lineTo){
      x.set(applyX.apply(lineTo.x()));
      y.set(applyY.apply(lineTo.y()));
    }else throw new RuntimeException("type error");
  }
}
