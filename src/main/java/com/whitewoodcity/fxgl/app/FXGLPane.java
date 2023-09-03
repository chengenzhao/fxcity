package com.whitewoodcity.fxgl.app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FXGLPane extends Region {

  private double w, h;

  public FXGLPane(double w, double h) {
    super();
    this.w = w;
    this.h = h;
  }

  public ObservableList<Node> getAllChildren() {
    return getChildren();
  }

  private DoubleProperty renderWidthProp = new SimpleDoubleProperty(w);
  private DoubleProperty renderHeightProp = new SimpleDoubleProperty(h);

  // default is white to be consistent with FXGL's scene default in non-embedded mode
  private ObjectProperty<Paint> renderFillProp = new SimpleObjectProperty<>(Color.WHITE);

  public double getRenderWidth() {
    return renderWidthProp.getValue();
  }

  public void setRenderWidth(double value) {
    renderWidthProp.setValue(value);
  }

  public double getRenderHeight() {
    return renderHeightProp.getValue();
  }

  public void setRenderHeight(double value) {
    renderHeightProp.setValue(value);
  }

  public Paint getRenderFill() {
    return renderFillProp.getValue();
  }

  public void setRenderFill(Paint fill) {
    renderFillProp.set(fill);
  }


  DoubleProperty renderWidthProperty() {
    return renderWidthProp;
  }

  DoubleProperty renderHeightProperty() {
    return renderHeightProp;
  }

  ObjectProperty<Paint> renderFillProperty() {
    return renderFillProp;
  }
}