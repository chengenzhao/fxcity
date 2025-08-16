package com.whitewoodcity.fxgl.vectorview;

import com.whitewoodcity.fxgl.vectorview.svgpathcommand.CurveTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.QuadraticTo;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SVGPathElement;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SmoothTo;
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
    double minX = 0;
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
    double minY = 0;
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
    for(SVGPathElement element : svgPathElements){
      switch (element){
        case CurveTo curveTo -> {
          curveTo.x().set(curveTo.getX() - x);
          curveTo.y().set(curveTo.getY() - y);
          curveTo.x1().set(curveTo.getX1() - x);
          curveTo.y1().set(curveTo.getY1() - y);
          curveTo.x2().set(curveTo.getX2() - x);
          curveTo.y2().set(curveTo.getY2() - y);
        }
        case QuadraticTo quadraticTo -> {
          quadraticTo.x().set(quadraticTo.getX() - x);
          quadraticTo.y().set(quadraticTo.getY() - y);
          quadraticTo.x1().set(quadraticTo.getX1() - x);
          quadraticTo.y1().set(quadraticTo.getY1() - y);
        }
        case SmoothTo smoothTo -> {
          smoothTo.x().set(smoothTo.getX() - x);
          smoothTo.y().set(smoothTo.getY() - y);
          smoothTo.x2().set(smoothTo.getX2() - x);
          smoothTo.y2().set(smoothTo.getY2() - y);
        }
        default -> {
          element.x().set(element.getX() - x);
          element.y().set(element.getY() - y);
        }
      }
    }
  }
}
