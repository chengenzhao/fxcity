package com.whitewoodcity.fxgl.material;

import com.almasb.fxgl.core.Copyable;
import com.almasb.fxgl.core.View;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Material extends Canvas implements View, Copyable<Material> {

  public Material(double width, double height) {
    super(width, height);
    // Redraw canvas when size changes.
    widthProperty().addListener(e -> draw());
    heightProperty().addListener(e -> draw());

    draw();
  }

  public void draw() {

  }

  @Override
  public boolean isResizable() {
    return true;
  }

  protected void fillSVGPath(String webColor, String svgPath) {
    var gc = getGraphicsContext2D();
    gc.setFill(Color.web(webColor));
    gc.beginPath();
    gc.appendSVGPath(svgPath);
    gc.fill();
  }

  @Override
  public Material copy() {
    return new Material(getWidth(), getHeight());
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public void dispose() {

  }

  @Override
  public void onUpdate(double tpf) {

  }
}
