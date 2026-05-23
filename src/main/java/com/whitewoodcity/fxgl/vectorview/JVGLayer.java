package com.whitewoodcity.fxgl.vectorview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.*;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

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

  default JVGLayer trim(Point2D p){
    return trim(p.getX(), p.getY());
  };

  JVGLayer trim(double x, double y);

  default JVGLayer move(Point2D p){
    return move(p.getX(), p.getY());
  }

  JVGLayer move(double x, double y);

  default void update() {
  }

  default JVGLayer zoom(double factor){
    if(this instanceof Shape shape){
      shape.setStrokeWidth(shape.getStrokeWidth() * factor);
      switch (shape.getEffect()) {
        case GaussianBlur gaussianBlur -> gaussianBlur.setRadius(gaussianBlur.getRadius() * factor);
        case null, default -> {
        }
      }

      switch (shape.getFill()) {
        case LinearGradient gradient -> {
          var sx = gradient.getStartX();
          var sy = gradient.getStartY();
          var ex = gradient.getEndX();
          var ey = gradient.getEndY();

          shape.setFill(new LinearGradient(sx * factor, sy * factor, ex * factor, ey * factor, gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
        }
        case RadialGradient gradient -> {
          var cx = gradient.getCenterX();
          var cy = gradient.getCenterY();

          shape.setFill(new RadialGradient(gradient.getFocusAngle(), gradient.getFocusDistance() * factor, cx * factor, cy * factor, gradient.getRadius() * factor, gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
        }
        default -> {
        }
      }
    }
    return this;
  };

  default ObjectNode toJson(){
    var mapper = new ObjectMapper();
    var objectNode = mapper.createObjectNode();

    if(this instanceof Shape shape) {
      objectNode.put(JsonKeys.STROKE_WIDTH.key(), shape.getStrokeWidth());
      switch (shape.getFill()) {
        case Color color -> objectNode.put(JsonKeys.FILL.key(), JVGLayer.toWebHexWithAlpha(color));
        case LinearGradient gradient -> {
          var g = mapper.createObjectNode();
          g.put(JsonKeys.GRADIENT_TYPE.key(), _LINEAR_GRADIENT);
          g.put(JsonKeys.START_X.key(), gradient.getStartX());
          g.put(JsonKeys.START_Y.key(), gradient.getStartY());
          g.put(JsonKeys.END_X.key(), gradient.getEndX());
          g.put(JsonKeys.END_Y.key(), gradient.getEndY());
          g.put(JsonKeys.PROPORTIONAL.key(), gradient.isProportional());
          g.put(JsonKeys.CYCLE_METHOD.key(), switch (gradient.getCycleMethod()) {
            case NO_CYCLE -> _NO_CYCLE;
            case REFLECT -> _REFLECT;
            case REPEAT -> _REPEAT;
          });
          var stops = mapper.createArrayNode();
          for (var stop : gradient.getStops()) {
            var s = mapper.createObjectNode();
            s.put(JsonKeys.OFFSET.key(), stop.getOffset());
            s.put(JsonKeys.COLOR.key(), JVGLayer.toWebHexWithAlpha(stop.getColor()));
            stops.add(s);
          }
          g.set(JsonKeys.STOPS.key(), stops);
          objectNode.set(JsonKeys.FILL.key(), g);
        }
        case RadialGradient gradient -> {
          var g = mapper.createObjectNode();
          g.put(JsonKeys.GRADIENT_TYPE.key(), _RADIAL_GRADIENT);
          g.put(JsonKeys.FOCUS_ANGLE.key(), gradient.getFocusAngle());
          g.put(JsonKeys.FOCUS_DISTANCE.key(), gradient.getFocusDistance());
          g.put(JsonKeys.RADIUS.key(), gradient.getRadius());
          g.put(JsonKeys.CENTER_X.key(), gradient.getCenterX());
          g.put(JsonKeys.CENTER_Y.key(), gradient.getCenterY());
          g.put(JsonKeys.PROPORTIONAL.key(), gradient.isProportional());
          g.put(JsonKeys.CYCLE_METHOD.key(), switch (gradient.getCycleMethod()) {
            case NO_CYCLE -> _NO_CYCLE;
            case REFLECT -> _REFLECT;
            case REPEAT -> _REPEAT;
          });
          var stops = mapper.createArrayNode();
          for (var stop : gradient.getStops()) {
            var s = mapper.createObjectNode();
            s.put(JsonKeys.OFFSET.key(), stop.getOffset());
            s.put(JsonKeys.COLOR.key(), JVGLayer.toWebHexWithAlpha(stop.getColor()));
            stops.add(s);
          }
          g.set(JsonKeys.STOPS.key(), stops);
          objectNode.set(JsonKeys.FILL.key(), g);
        }
        default -> throw new RuntimeException("unsupported fill type: " + shape.getFill());
      }

      objectNode.put(JsonKeys.STROKE.key(), JVGLayer.toWebHexWithAlpha((Color) shape.getStroke()));
      if (shape.getClip() != null)
        objectNode.put(JsonKeys.CLIP.key(), true);
      switch (shape.getEffect()) {
        case GaussianBlur blur -> {
          var effect = mapper.createObjectNode();
          effect.put(JsonKeys.TYPE.key(), JsonKeys.GAUSSIAN_BLUR.key());
          effect.put(JsonKeys.RADIUS.key(), blur.getRadius());

          objectNode.set(JsonKeys.EFFECT.key(), effect);
        }
        case null, default -> {
        }
      }
      if (shape.getBlendMode() != null)
        objectNode.put(JsonKeys.BLEND_MODE.key(), shape.getBlendMode().toString());
    }
    return objectNode;
  };

  default String toJsonString() {
    return toJson().toString();
  }

  default void fromJson(String jsonString) {
    var mapper = new ObjectMapper();
    try {
      fromJson((ObjectNode) mapper.readTree(jsonString));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  default void fromJson(ObjectNode objectNode){
    if(this instanceof Shape shape) {
      shape.setStrokeWidth(objectNode.get(JsonKeys.STROKE_WIDTH.key()).asDouble());
      shape.setStroke(Color.web(objectNode.get(JsonKeys.STROKE.key()).asText()));

      switch (objectNode.get(JsonKeys.FILL.key())) {
        case TextNode text -> shape.setFill(Color.web(text.asText()));
        case ObjectNode obj -> {
          var stops = new ArrayList<Stop>();
          var ss = (ArrayNode) obj.get(JsonKeys.STOPS.key());
          for (var s : ss) {
            var stop = new Stop(s.get(JsonKeys.OFFSET.key()).asDouble(), Color.web(s.get(JsonKeys.COLOR.key()).asText()));
            stops.add(stop);
          }
          CycleMethod cycleMethod = switch (obj.get(JsonKeys.CYCLE_METHOD.key()).asText()) {
            case _NO_CYCLE -> CycleMethod.NO_CYCLE;
            case _REFLECT -> CycleMethod.REFLECT;
            case _REPEAT -> CycleMethod.REPEAT;
            default ->
              throw new RuntimeException("unsupported cycleMethod type: " + obj.get(JsonKeys.CYCLE_METHOD.key()).asText());
          };
          boolean proportional = obj.get(JsonKeys.PROPORTIONAL.key()).asBoolean();
          switch (obj.get(JsonKeys.GRADIENT_TYPE.key()).asText()) {
            case _LINEAR_GRADIENT -> {
              double startX = obj.get(JsonKeys.START_X.key()).asDouble();
              double startY = obj.get(JsonKeys.START_Y.key()).asDouble();
              double endX = obj.get(JsonKeys.END_X.key()).asDouble();
              double endY = obj.get(JsonKeys.END_Y.key()).asDouble();
              shape.setFill(new LinearGradient(startX, startY, endX, endY, proportional, cycleMethod, stops));
            }
            case _RADIAL_GRADIENT -> {
              double centerX = obj.get(JsonKeys.CENTER_X.key()).asDouble();
              double centerY = obj.get(JsonKeys.CENTER_Y.key()).asDouble();
              double radius = obj.get(JsonKeys.RADIUS.key()).asDouble();
              double focusAngle = obj.get(JsonKeys.FOCUS_ANGLE.key()).asDouble();
              double focusDistance = obj.get(JsonKeys.FOCUS_DISTANCE.key()).asDouble();
              shape.setFill(new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, cycleMethod, stops));
            }
            default ->
              throw new RuntimeException("unsupported gradient type: " + obj.get(JsonKeys.GRADIENT_TYPE.key()).asText());
          }
        }
        default -> throw new RuntimeException("unsupported fill type: " + objectNode.get(JsonKeys.FILL.key()));
      }

      if (objectNode.has(JsonKeys.EFFECT.key())) {
        var obj = objectNode.get(JsonKeys.EFFECT.key());
        if (obj instanceof ObjectNode effect) {
          switch (effect.get(JsonKeys.TYPE.key()).asText()) {
            case _GAUSSIAN_BLUR -> {
              var blur = new GaussianBlur(effect.get(JsonKeys.RADIUS.key()).asDouble());
              shape.setEffect(blur);
            }
            default -> {
            }
          }
        }
      }

      if (objectNode.has(JsonKeys.BLEND_MODE.key())) {
        var blendModeString = objectNode.get(JsonKeys.BLEND_MODE.key()).asText();

        for (var blendMode : BlendMode.values()) {
          if (blendMode.toString().equals(blendModeString)) {
            shape.setBlendMode(blendMode);
            break;
          }
        }
      }
    }
  };

  static String toWebHexWithAlpha(Color color) {
    return String.format("#%02X%02X%02X%02X",
      (int) (color.getRed() * 255),
      (int) (color.getGreen() * 255),
      (int) (color.getBlue() * 255),
      (int) (color.getOpacity() * 255));
  }
}
