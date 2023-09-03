package com.whitewoodcity.flame;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ResizableCanvas extends Canvas {

  public ResizableCanvas(){
    this(1);
  }

  public ResizableCanvas(double widthToHeightRatio) {
    widthProperty().bind(heightProperty().multiply(widthToHeightRatio));
    // Redraw canvas when size changes.
//    widthProperty().addListener(evt -> draw());
    heightProperty().addListener(evt -> draw());
  }

  abstract public void draw();

  @Override
  public boolean isResizable() {
    return true;
  }

  @Override
  public double prefWidth(double height) {
    return getWidth();
  }

  @Override
  public double prefHeight(double width) {
    return getHeight();
  }

  protected void fillSVGPath(GraphicsContext gc, String webColor, String svgPath){
    gc.setFill(Color.web(webColor));
    gc.beginPath();
    gc.appendSVGPath(svgPath);
    gc.fill();
  }
}