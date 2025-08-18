package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public record CurveTo(SimpleDoubleProperty x1, SimpleDoubleProperty y1, SimpleDoubleProperty x2, SimpleDoubleProperty y2, SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "C";
  }

  public double getX1() {
    return x1.get();
  }

  public double getY1() {
    return y1.get();
  }

  public double getX2() {
    return x2.get();
  }

  public double getY2() {
    return y2.get();
  }

  public CurveTo(double x1, double y1, double x2, double y2, double x, double y) {
    this(new SimpleDoubleProperty(x1), new SimpleDoubleProperty(y1), new SimpleDoubleProperty(x2), new SimpleDoubleProperty(y2), new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public CurveTo clone() {
    return new CurveTo(getX1(), getY1(), getX2(), getY2(), getX(), getY());
  }

  @Override
  public void apply(SVGPathElement reference, Apply applyX, Apply applyY) {
    if(reference instanceof CurveTo curveTo){
      x.set(applyX.apply(curveTo.x()));
      y.set(applyY.apply(curveTo.y()));
      x1.set(applyX.apply(curveTo.x1()));
      y1.set(applyY.apply(curveTo.y1()));
      x2.set(applyX.apply(curveTo.x2()));
      y2.set(applyY.apply(curveTo.y2()));
    }else throw new RuntimeException("type error");
  }

  @Override
  public void traverse(Traverse traverseX, Traverse traverseY) {
    traverseX.traverse(x());
    traverseY.traverse(y());
    traverseX.traverse(x1());
    traverseY.traverse(y1());
    traverseX.traverse(x2());
    traverseY.traverse(y2());
  }
}
