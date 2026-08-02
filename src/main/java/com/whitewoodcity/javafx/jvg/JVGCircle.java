package com.whitewoodcity.javafx.jvg;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public final class JVGCircle extends Circle implements JVGLayer {
  @Override
  public Shape daemon() {
    var circle = new Circle();
    circle.centerXProperty().bind(centerXProperty());
    circle.centerYProperty().bind(centerYProperty());
    circle.radiusProperty().bind(radiusProperty());
    circle.strokeProperty().bind(strokeProperty());
    circle.strokeWidthProperty().bind(strokeWidthProperty());
    circle.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    circle.strokeLineCapProperty().bind(strokeLineCapProperty());
    circle.effectProperty().bind(effectProperty());
    return circle;
  }

  @Override
  public Point2D getMinXY() {
    return new Point2D(getCenterX() - getRadius(), getCenterY() - getRadius());
  }

  @Override
  public Point2D getMaxXY() {
    return new Point2D(getCenterX() + getRadius(), getCenterY() + getRadius());
  }

  @Override
  public JVGCircle trim(double x, double y) {
    return move(-x,-y);
  }

  @Override
  public JVGCircle move(double x, double y) {
    setCenterX(getCenterX() + x);
    setCenterY(getCenterY() + y);
    JVGLayer.super.move(x,y);
    return this;
  }

  @Override
  public JVGCircle zoom(double factor) {
    setRadius(getRadius() * factor);
    setCenterX(getCenterX() * factor);
    setCenterY(getCenterY() * factor);
    JVGLayer.super.zoom(factor);
    return this;
  }

  @Override
  public ObjectNode toJson() {
    var objNode = JVGLayer.super.toJson();

    objNode.put(JsonKeys.SHAPE.key(), Circle.class.getSimpleName());
    objNode.put(JsonKeys.CENTER_X.key(), getCenterX());
    objNode.put(JsonKeys.CENTER_Y.key(), getCenterY());
    objNode.put(JsonKeys.RADIUS.key(), getRadius());

    return objNode;
  }

  @Override
  public void fromJson(ObjectNode objectNode) {
    JVGLayer.super.fromJson(objectNode);

    setCenterX(objectNode.get(JsonKeys.CENTER_X.key()).asDouble());
    setCenterY(objectNode.get(JsonKeys.CENTER_Y.key()).asDouble());
    setRadius(objectNode.get(JsonKeys.RADIUS.key()).asDouble());
  }
}
