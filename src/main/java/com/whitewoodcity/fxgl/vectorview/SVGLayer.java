package com.whitewoodcity.fxgl.vectorview;

import com.whitewoodcity.fxgl.vectorview.svgpathcommand.SVGPathElement;
import javafx.scene.shape.SVGPath;

import java.util.List;
import java.util.ArrayList;

public class SVGLayer extends SVGPath {
  private List<SVGPathElement> elementList = new ArrayList<>();

  public void addSVGPathElement(SVGPathElement element){
    elementList.add(element);
  }

  public void removeSVGPathElement(SVGPathElement element){
    elementList.remove(element);
  }
}
