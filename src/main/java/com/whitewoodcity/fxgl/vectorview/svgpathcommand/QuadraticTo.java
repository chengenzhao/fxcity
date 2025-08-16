package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record QuadraticTo(SimpleDoubleProperty x1, SimpleDoubleProperty y1, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "Q";
  }

  public double getX1() {
    return x1.get();
  }

  public double getY1() {
    return y1.get();
  }

  public QuadraticTo(double x1, double y1, double x, double y) {
    this(new SimpleDoubleProperty(x1), new SimpleDoubleProperty(y1), new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public QuadraticTo clone() {
    return new QuadraticTo(getX1(), getY1(),getX(), getY());
  }

  @Override
  public void apply(SVGPathElement reference, Apply applyX, Apply applyY) {
    if(reference instanceof QuadraticTo quadraticTo){
      x.set(applyX.apply(quadraticTo.x()));
      y.set(applyY.apply(quadraticTo.y()));
      x1.set(applyX.apply(quadraticTo.x1()));
      y1.set(applyY.apply(quadraticTo.y1()));
    }else throw new RuntimeException("type error");
  }
}
