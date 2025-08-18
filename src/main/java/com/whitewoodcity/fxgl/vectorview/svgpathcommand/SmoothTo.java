package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record SmoothTo(SimpleDoubleProperty x2, SimpleDoubleProperty y2, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "S";
  }

  public double getX2() {
    return x2.get();
  }

  public double getY2() {
    return y2.get();
  }

  public SmoothTo(double x2, double y2, double x, double y) {
    this(new SimpleDoubleProperty(x2), new SimpleDoubleProperty(y2), new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public SmoothTo clone() {
    return new SmoothTo(getX2(), getY2(), getX(), getY());
  }

  @Override
  public void apply(SVGPathElement reference, Apply applyX, Apply applyY) {
    if(reference instanceof SmoothTo smoothTo){
      x.set(applyX.apply(smoothTo.x()));
      y.set(applyY.apply(smoothTo.y()));
      x2.set(applyX.apply(smoothTo.x2()));
      y2.set(applyY.apply(smoothTo.y2()));
    }else throw new RuntimeException("type error");
  }

  @Override
  public void traverse(Traverse traverseX, Traverse traverseY) {
    traverseX.traverse(x());
    traverseY.traverse(y());
    traverseX.traverse(x2());
    traverseY.traverse(y2());
  }
}
