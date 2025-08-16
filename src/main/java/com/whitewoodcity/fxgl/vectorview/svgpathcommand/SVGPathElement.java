package com.whitewoodcity.fxgl.vectorview.svgpathcommand;

import javafx.beans.property.SimpleDoubleProperty;

public sealed interface SVGPathElement permits CurveTo, LineTo, MoveTo, QuadraticTo, SmoothTo, TransitTo {
  String command();
  SimpleDoubleProperty x();
  SimpleDoubleProperty y();
  default double getX(){return this.x().get();}
  default double getY(){return this.y().get();}

  default SVGPathElement copyCoordinate(){
    return new MoveTo(getX(), getY());
  }

  SVGPathElement clone();
  default void apply(SVGPathElement reference, Apply apply){
    apply(reference, apply, apply);
  }
  void apply(SVGPathElement reference, Apply applyX, Apply applyY);

  @FunctionalInterface
  public interface Apply{
    double apply(SimpleDoubleProperty originalValue);
  };
}
