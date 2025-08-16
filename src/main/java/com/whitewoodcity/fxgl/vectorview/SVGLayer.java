package com.whitewoodcity.fxgl.vectorview;

import com.whitewoodcity.fxgl.vectorview.svgpathcommand.CurveTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.QuadraticTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SVGPathElement;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SmoothTo;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.SVGPath;

import java.util.List;
import java.util.ArrayList;

public class SVGLayer extends SVGPath {
  private List<SVGPathElement> svgPathElements = new ArrayList<>();

  public void addSVGPathElement(SVGPathElement element){
    svgPathElements.add(element);
  }

  public void removeSVGPathElement(SVGPathElement element){
    svgPathElements.remove(element);
  }

  public List<SVGPathElement> getSvgPathElements() {
    return svgPathElements;
  }

  public void draw(String suffix){
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

  public SVGPath daemon(){
    var svgPath = new SVGPath();
    svgPath.contentProperty().bind(contentProperty());
    svgPath.strokeProperty().bind(strokeProperty());
    svgPath.strokeWidthProperty().bind(strokeWidthProperty());
    svgPath.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    svgPath.strokeLineCapProperty().bind(strokeLineCapProperty());
    svgPath.effectProperty().bind(effectProperty());
    return svgPath;
  }

  public double getMinX(){
    if(svgPathElements.size() < 1) return 0;
    double minX = svgPathElements.getFirst().getX();
    for(SVGPathElement element : svgPathElements){
      switch (element){
        case CurveTo curveTo -> {
          minX = Math.min(minX, curveTo.getX1());
          minX = Math.min(minX, curveTo.getX2());
          minX = Math.min(minX, curveTo.getX());
        }
        case QuadraticTo quadraticTo -> {
          minX = Math.min(minX, quadraticTo.getX1());
          minX = Math.min(minX, quadraticTo.getX());
        }
        case SmoothTo smoothTo -> {
          minX = Math.min(minX, smoothTo.getX2());
          minX = Math.min(minX, smoothTo.getX());
        }
        default -> minX = Math.min(minX, element.getX());
      }
    }
    return minX;
  }

  public double getMinY(){
    if(svgPathElements.size() < 1) return 0;
    double minY = svgPathElements.getFirst().getY();
    for(SVGPathElement element : svgPathElements){
      switch (element){
        case CurveTo curveTo -> {
          minY = Math.min(minY, curveTo.getY1());
          minY = Math.min(minY, curveTo.getY2());
          minY = Math.min(minY, curveTo.getY());
        }
        case QuadraticTo quadraticTo -> {
          minY = Math.min(minY, quadraticTo.getY1());
          minY = Math.min(minY, quadraticTo.getY());
        }
        case SmoothTo smoothTo -> {
          minY = Math.min(minY, smoothTo.getY2());
          minY = Math.min(minY, smoothTo.getY());
        }
        default -> minY = Math.min(minY, element.getY());
      }
    }
    return minY;
  }

  public void trim(){
    trim(getMinX(), getMinY());
  }

  public void trim(double x, double y){
    move(-x, -y);
  }

  public void move(double x, double y){
    map(p -> p.get() + x, p -> p.get() + y);
  }

  public void zoom(double factor){
    map(x -> x.get() * factor, y -> y.get() * factor);
    setStrokeWidth(getStrokeWidth() * factor);
    switch (getEffect()){
      case null -> {}
      case GaussianBlur gaussianBlur -> gaussianBlur.setRadius(gaussianBlur.getRadius() * factor);
      default -> {}
    }
  }

  public void map(SVGPathElement.Apply applyX, SVGPathElement.Apply applyY){
    for(SVGPathElement element : svgPathElements){
      element.apply(element, applyX, applyY);
    }
  }
}
