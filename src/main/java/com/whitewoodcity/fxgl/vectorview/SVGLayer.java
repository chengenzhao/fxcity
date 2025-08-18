package com.whitewoodcity.fxgl.vectorview;

import com.whitewoodcity.fxgl.vectorview.svgpathcommand.CurveTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.QuadraticTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SVGPathElement;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SmoothTo;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.SVGPath;

import java.util.List;
import java.util.ArrayList;

public class SVGLayer extends SVGPath {
  private List<SVGPathElement> svgPathElements = new ArrayList<>();

  public void addSVGPathElement(SVGPathElement element) {
    svgPathElements.add(element);
  }

  public void removeSVGPathElement(SVGPathElement element) {
    svgPathElements.remove(element);
  }

  public List<SVGPathElement> getSvgPathElements() {
    return svgPathElements;
  }

  public void draw(String suffix) {
    StringBuilder content = new StringBuilder();
    for (SVGPathElement element : svgPathElements) {
      content.append(element.command()).append(" ").append(switch (element) {
        case CurveTo curveTo ->
          curveTo.getX1() + "," + curveTo.getY1() + " " + curveTo.getX2() + "," + curveTo.getY2() + " ";
        case SmoothTo smoothTo -> smoothTo.getX2() + "," + smoothTo.getY2() + " ";
        case QuadraticTo quadraticTo -> quadraticTo.getX1() + "," + quadraticTo.getY1() + " ";
        default -> "";
      }).append(element.getX()).append(",").append(element.getY()).append(" ");
    }
    content.append(suffix);
    setContent(content.toString());
  }

  public SVGPath daemon() {
    var svgPath = new SVGPath();
    svgPath.contentProperty().bind(contentProperty());
    svgPath.strokeProperty().bind(strokeProperty());
    svgPath.strokeWidthProperty().bind(strokeWidthProperty());
    svgPath.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    svgPath.strokeLineCapProperty().bind(strokeLineCapProperty());
    svgPath.effectProperty().bind(effectProperty());
    return svgPath;
  }

  public double getMinX() {
    return getMinXY().getX();
  }

  public double getMinY() {
    return getMinXY().getY();
  }

  public Point2D getMinXY() {
    if (svgPathElements.size() < 1) return new Point2D(0, 0);
    var e = svgPathElements.getFirst();
    var xp = new SimpleDoubleProperty(e.getX());
    var yp = new SimpleDoubleProperty(e.getY());
    for (SVGPathElement element : svgPathElements) {
      element.traverse(x -> xp.set(Math.min(xp.get(), x.get())), y -> yp.set(Math.min(yp.get(), y.get())));
    }
    return new Point2D(xp.get(), yp.get());
  }

  public Dimension2D getDimension(){
    if (svgPathElements.size() < 1) return new Dimension2D(0, 0);
    var e = svgPathElements.getFirst();

    var minX = new SimpleDoubleProperty(e.getX());
    var minY = new SimpleDoubleProperty(e.getY());
    var maxX = new SimpleDoubleProperty(e.getX());
    var maxY = new SimpleDoubleProperty(e.getY());

    for (SVGPathElement element : svgPathElements) {
      element.traverse(x -> {
        minX.set(Math.min(minX.get(), x.get()));
        maxX.set(Math.max(maxX.get(), x.get()));
      }, y -> {
        minY.set(Math.min(minY.get(), y.get()));
        maxY.set(Math.max(maxY.get(), y.get()));
      });
    }

    return new Dimension2D(maxX.get() - minX.get(), maxY.get() - minY.get());
  }

  public void trim() {
    trim(getMinXY());
  }

  public void trim(Point2D p){
    trim(p.getX(), p.getY());
  }

  public void trim(double x, double y) {
    move(-x, -y);
  }

  public void move(Point2D p){
    move(p.getX(), p.getY());
  }

  public void move(double x, double y) {
    map(p -> p.get() + x, p -> p.get() + y);
  }

  public void zoom(double factor) {
    map(x -> x.get() * factor, y -> y.get() * factor);
    setStrokeWidth(getStrokeWidth() * factor);
    switch (getEffect()) {
      case null -> {
      }
      case GaussianBlur gaussianBlur -> gaussianBlur.setRadius(gaussianBlur.getRadius() * factor);
      default -> {
      }
    }
  }

  public void map(SVGPathElement.Apply applyX, SVGPathElement.Apply applyY) {
    for (SVGPathElement element : svgPathElements) {
      element.apply(element, applyX, applyY);
    }
  }
}
