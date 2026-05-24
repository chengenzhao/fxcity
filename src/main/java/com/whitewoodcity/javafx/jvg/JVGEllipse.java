package com.whitewoodcity.javafx.jvg;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class JVGEllipse extends Ellipse implements JVGLayer {
  @Override
  public Shape daemon() {
    var ellipse = new Ellipse();
    ellipse.centerXProperty().bind(centerXProperty());
    ellipse.centerYProperty().bind(centerYProperty());
    ellipse.radiusXProperty().bind(radiusXProperty());
    ellipse.radiusYProperty().bind(radiusYProperty());
    ellipse.strokeProperty().bind(strokeProperty());
    ellipse.strokeWidthProperty().bind(strokeWidthProperty());
    ellipse.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    ellipse.strokeLineCapProperty().bind(strokeLineCapProperty());
    ellipse.effectProperty().bind(effectProperty());
    return ellipse;
  }

  @Override
  public Point2D getMinXY() {
    return new Point2D(getCenterX() - getRadiusX(), getCenterY() - getRadiusY());
  }

  @Override
  public Point2D getMaxXY() {
    return new Point2D(getCenterX() + getRadiusX(), getCenterY() + getRadiusY());
  }

  @Override
  public JVGLayer trim(double x, double y) {
    setCenterX(getCenterX() - x);
    setCenterY(getCenterY() - y);
    return this;
  }

  @Override
  public JVGLayer move(double x, double y) {
    setCenterX(getCenterX() + x);
    setCenterY(getCenterY() + y);
    return this;
  }


  @Override
  public JVGLayer zoom(double factor) {
    setRadiusX(getRadiusX() * factor);
    setRadiusY(getRadiusY() * factor);
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
    objNode.put(JsonKeys.RADIUS_X.key(), getRadiusX());
    objNode.put(JsonKeys.RADIUS_Y.key(), getRadiusY());

    return objNode;
  }

  @Override
  public void fromJson(ObjectNode objectNode) {
    JVGLayer.super.fromJson(objectNode);

    setCenterX(objectNode.get(JsonKeys.CENTER_X.key()).asDouble());
    setCenterY(objectNode.get(JsonKeys.CENTER_Y.key()).asDouble());
    setRadiusX(objectNode.get(JsonKeys.RADIUS_X.key()).asDouble());
    setRadiusY(objectNode.get(JsonKeys.RADIUS_Y.key()).asDouble());
  }
}
