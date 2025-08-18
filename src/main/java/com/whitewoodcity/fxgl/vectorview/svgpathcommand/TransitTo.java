package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

//T(transit to) = smooth quadratic Bézier curveto (create a smooth quadratic Bézier curve)
public record TransitTo(SimpleDoubleProperty x, SimpleDoubleProperty y) implements SVGPathElement {
  @Override
  public String command() {
    return "T";
  }

  public TransitTo(double x, double y) {
    this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
  }

  @Override
  public TransitTo clone() {
    return new TransitTo(getX(), getY());
  }

}
