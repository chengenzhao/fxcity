package com.whitewoodcity.fxgl.vectorview;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public interface JVGLayer {

  String _GAUSSIAN_BLUR = "gaussianBlur";
  String _LINEAR_GRADIENT = "linearGradient";
  String _RADIAL_GRADIENT = "radialGradient";
  String _NO_CYCLE = "noCycle";
  String _REFLECT = "reflect";
  String _REPEAT = "repeat";

  Shape daemon();

  Point2D getMinXY();

  Point2D getMaxXY();

  JVGLayer trim(Point2D p);

  JVGLayer trim(double x, double y);

  JVGLayer move(Point2D p);

  JVGLayer move(double x, double y);

  default void update() {
  }

  JVGLayer zoom(double factor);

  ObjectNode toJson();

  String toJsonString();

  void fromJson(String jsonString);

  void fromJson(ObjectNode objectNode);

  static String toWebHexWithAlpha(Color color) {
    return String.format("#%02X%02X%02X%02X",
      (int) (color.getRed() * 255),
      (int) (color.getGreen() * 255),
      (int) (color.getBlue() * 255),
      (int) (color.getOpacity() * 255));
  }
}
