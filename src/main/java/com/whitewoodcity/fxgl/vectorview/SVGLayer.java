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
}
