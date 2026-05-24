package com.whitewoodcity.javafx.jvg;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class JVGRectangle extends Rectangle implements JVGLayer {
  @Override
  public Shape daemon() {
    var rect = new Rectangle();
    rect.xProperty().bind(xProperty());
    rect.yProperty().bind(yProperty());
    rect.widthProperty().bind(widthProperty());
    rect.heightProperty().bind(heightProperty());
    rect.strokeProperty().bind(strokeProperty());
    rect.strokeWidthProperty().bind(strokeWidthProperty());
    rect.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    rect.strokeLineCapProperty().bind(strokeLineCapProperty());
    rect.effectProperty().bind(effectProperty());
    return rect;
  }

  @Override
  public Point2D getMinXY() {
    return new Point2D(getX(), getY());
  }

  @Override
  public Point2D getMaxXY() {
    return new Point2D(getX() + getWidth(), getY() + getHeight());
  }

  @Override
  public JVGLayer trim(double x, double y) {
    setX(getX() - x);
    setY(getY() - y);
    return this;
  }

  @Override
  public JVGLayer move(double x, double y) {
    setX(getX() + x);
    setY(getY() + y);
    return this;
  }

  @Override
  public JVGLayer zoom(double factor) {
    setX(getX() * factor);
    setY(getY() * factor);
    setWidth(getWidth() * factor);
    setHeight(getHeight() * factor);
    JVGLayer.super.zoom(factor);
    return this;
  }

  @Override
  public ObjectNode toJson() {
    var objNode = JVGLayer.super.toJson();

    objNode.put(JsonKeys.SHAPE.key(), Rectangle.class.getSimpleName());
    objNode.put(JsonKeys.X.key(), getX());
    objNode.put(JsonKeys.Y.key(), getY());
    objNode.put(JsonKeys.WIDTH.key(), getWidth());
    objNode.put(JsonKeys.HEIGHT.key(), getHeight());

    return objNode;
  }

  @Override
  public void fromJson(ObjectNode objectNode) {
    JVGLayer.super.fromJson(objectNode);

    setX(objectNode.get(JsonKeys.X.key()).asDouble());
    setY(objectNode.get(JsonKeys.Y.key()).asDouble());
    setWidth(objectNode.get(JsonKeys.WIDTH.key()).asDouble());
    setHeight(objectNode.get(JsonKeys.HEIGHT.key()).asDouble());
  }
}
