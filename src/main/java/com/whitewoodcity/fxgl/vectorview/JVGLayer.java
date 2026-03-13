package com.whitewoodcity.fxgl.vectorview;

import module com.fasterxml.jackson.databind;
import module java.base;
import module javafx.controls;
import com.whitewoodcity.fxgl.vectorview.svgpathcommand.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class JVGLayer extends SVGPath {

  static final String _GAUSSIAN_BLUR = "gaussianBlur";
  static final String _LINEAR_GRADIENT = "linearGradient";
  static final String _RADIACAL_GRADIENT = "radialGradient";
  static final String _NO_CYCLE = "noCycle";
  static final String _REFLECT = "reflect";
  static final String _REPEAT = "repeat";

  public enum JsonKeys {

    STROKE_WIDTH("strokeWidth"),
    FILL("fill"),
    STROKE("stroke"),
    CONTENT("content"),
    CLIP("clip"),
    BLEND_MODE("blendMode"),
    EFFECT("effect"),
    TYPE("type"),
    GAUSSIAN_BLUR(_GAUSSIAN_BLUR),
    RADIUS("radius"),
    FOCUS_ANGLE("focusAngle"),
    FOCUS_DISTANCE("focusDistance"),
    CENTER_X("centerX"),
    CENTER_Y("centerY"),
    START_X("startX"),
    START_Y("startY"),
    END_X("endX"),
    END_Y("endY"),
    PROPORTIONAL("proportional"),
    CYCLE_METHOD("cycleMethod"),
    STOPS("stops"),
    OFFSET("offset"),
    COLOR("color"),
    NO_CYCLE(_NO_CYCLE),
    REFLECT(_REFLECT),
    REPEAT(_REPEAT),
    GRADIENT_TYPE("gradientType");

    private final String key;

    JsonKeys(final String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }

  private final List<SVGPathElement> svgPathElements = new ArrayList<>();

  public void addSVGPathElement(SVGPathElement element) {
    svgPathElements.add(element);
  }

  public void removeSVGPathElement(SVGPathElement element) {
    svgPathElements.remove(element);
  }

  public List<SVGPathElement> getSvgPathElements() {
    return svgPathElements;
  }

  public void update() {
    update(getContent().endsWith("Z") ? "Z" : "");
  }

  public void update(String suffix) {
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

  public SVGPath daemon() {
    var svgPath = new SVGPath();
    svgPath.contentProperty().bind(contentProperty());
    svgPath.strokeProperty().bind(strokeProperty());
    svgPath.strokeWidthProperty().bind(strokeWidthProperty());
    svgPath.strokeLineJoinProperty().bind(strokeLineJoinProperty());
    svgPath.strokeLineCapProperty().bind(strokeLineCapProperty());
    svgPath.effectProperty().bind(effectProperty());
    return svgPath;
  }

  public double getMinX() {
    return getMinXY().getX();
  }

  public double getMinY() {
    return getMinXY().getY();
  }

  public Point2D getMinXY() {
    if (svgPathElements.isEmpty()) return new Point2D(0, 0);
    var e = svgPathElements.getFirst();
    var xp = new SimpleDoubleProperty(e.getX());
    var yp = new SimpleDoubleProperty(e.getY());
    for (SVGPathElement element : svgPathElements) {
      element.traverse(x -> xp.set(Math.min(xp.get(), x.get())), y -> yp.set(Math.min(yp.get(), y.get())));
    }
    return new Point2D(xp.get(), yp.get());
  }

  public Point2D getMaxXY() {
    var xp = new SimpleDoubleProperty(0);
    var yp = new SimpleDoubleProperty(0);
    for (SVGPathElement element : svgPathElements) {
      element.traverse(x -> xp.set(Math.max(xp.get(), x.get())), y -> yp.set(Math.max(yp.get(), y.get())));
    }
    return new Point2D(xp.get(), yp.get());
  }

  public Dimension2D getDimension() {
    if (svgPathElements.isEmpty()) return new Dimension2D(0, 0);
    var e = svgPathElements.getFirst();

    var minX = new SimpleDoubleProperty(e.getX());
    var minY = new SimpleDoubleProperty(e.getY());
    var maxX = new SimpleDoubleProperty(e.getX());
    var maxY = new SimpleDoubleProperty(e.getY());

    for (SVGPathElement element : svgPathElements) {
      element.traverse(x -> {
        minX.set(Math.min(minX.get(), x.get()));
        maxX.set(Math.max(maxX.get(), x.get()));
      }, y -> {
        minY.set(Math.min(minY.get(), y.get()));
        maxY.set(Math.max(maxY.get(), y.get()));
      });
    }

    return new Dimension2D(maxX.get() - minX.get(), maxY.get() - minY.get());
  }

  public void trim() {
    trim(getMinXY());
  }

  public JVGLayer trim(Point2D p) {
    return trim(p.getX(), p.getY());
  }

  public JVGLayer trim(double x, double y) {
    return move(-x, -y);
  }

  public JVGLayer move(Point2D p) {
    return move(p.getX(), p.getY());
  }

  public JVGLayer move(double x, double y) {
    map(p -> p.get() + x, p -> p.get() + y);

    switch (this.getFill()) {
      case LinearGradient gradient -> {
        var sx = gradient.getStartX();
        var sy = gradient.getStartY();
        var ex = gradient.getEndX();
        var ey = gradient.getEndY();

        this.setFill(new LinearGradient(sx + x, sy + y, ex + x, ey + y, gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
      }
      case RadialGradient gradient -> {
        var cx = gradient.getCenterX();
        var cy = gradient.getCenterY();

        this.setFill(new RadialGradient(gradient.getFocusAngle(), gradient.getFocusDistance(), cx + x, cy + y, gradient.getRadius(), gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
      }
      default -> {
      }
    }

    return this;
  }

  public JVGLayer zoom(double factor) {
    map(x -> x.get() * factor, y -> y.get() * factor);
    setStrokeWidth(getStrokeWidth() * factor);
    switch (getEffect()) {
      case GaussianBlur gaussianBlur -> gaussianBlur.setRadius(gaussianBlur.getRadius() * factor);
      case null, default -> {
      }
    }

    switch (this.getFill()) {
      case LinearGradient gradient -> {
        var sx = gradient.getStartX();
        var sy = gradient.getStartY();
        var ex = gradient.getEndX();
        var ey = gradient.getEndY();

        this.setFill(new LinearGradient(sx * factor, sy * factor, ex * factor, ey * factor, gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
      }
      case RadialGradient gradient -> {
        var cx = gradient.getCenterX();
        var cy = gradient.getCenterY();

        this.setFill(new RadialGradient(gradient.getFocusAngle(), gradient.getFocusDistance() * factor, cx * factor, cy * factor, gradient.getRadius() * factor, gradient.isProportional(), gradient.getCycleMethod(), gradient.getStops()));
      }
      default -> {
      }
    }

    return this;
  }

  public JVGLayer map(SVGPathElement.Apply applyX, SVGPathElement.Apply applyY) {
    for (SVGPathElement element : svgPathElements) {
      element.apply(element, applyX, applyY);
    }
    return this;
  }

  public ObjectNode toJson() {
    var mapper = new ObjectMapper();
    var objectNode = mapper.createObjectNode();

    objectNode.put(JsonKeys.STROKE_WIDTH.key, getStrokeWidth());
    switch (getFill()) {
      case Color color -> objectNode.put(JsonKeys.FILL.key, toWebHexWithAlpha(color));
      case LinearGradient gradient -> {
        var g = mapper.createObjectNode();
        g.put(JsonKeys.GRADIENT_TYPE.key, _LINEAR_GRADIENT);
        g.put(JsonKeys.START_X.key, gradient.getStartX());
        g.put(JsonKeys.START_Y.key, gradient.getStartY());
        g.put(JsonKeys.END_X.key, gradient.getEndX());
        g.put(JsonKeys.END_Y.key, gradient.getEndY());
        g.put(JsonKeys.PROPORTIONAL.key, gradient.isProportional());
        g.put(JsonKeys.CYCLE_METHOD.key, switch (gradient.getCycleMethod()) {
          case NO_CYCLE -> _NO_CYCLE;
          case REFLECT -> _REFLECT;
          case REPEAT -> _REPEAT;
        });
        var stops = mapper.createArrayNode();
        for (var stop : gradient.getStops()) {
          var s = mapper.createObjectNode();
          s.put(JsonKeys.OFFSET.key, stop.getOffset());
          s.put(JsonKeys.COLOR.key, toWebHexWithAlpha(stop.getColor()));
          stops.add(s);
        }
        g.set(JsonKeys.STOPS.key, stops);
        objectNode.set(JsonKeys.FILL.key, g);
      }
      case RadialGradient gradient -> {
        var g = mapper.createObjectNode();
        g.put(JsonKeys.GRADIENT_TYPE.key, _RADIACAL_GRADIENT);
        g.put(JsonKeys.FOCUS_ANGLE.key, gradient.getFocusAngle());
        g.put(JsonKeys.FOCUS_DISTANCE.key, gradient.getFocusDistance());
        g.put(JsonKeys.RADIUS.key, gradient.getRadius());
        g.put(JsonKeys.CENTER_X.key, gradient.getCenterX());
        g.put(JsonKeys.CENTER_Y.key, gradient.getCenterY());
        g.put(JsonKeys.PROPORTIONAL.key, gradient.isProportional());
        g.put(JsonKeys.CYCLE_METHOD.key, switch (gradient.getCycleMethod()) {
          case NO_CYCLE -> _NO_CYCLE;
          case REFLECT -> _REFLECT;
          case REPEAT -> _REPEAT;
        });
        var stops = mapper.createArrayNode();
        for (var stop : gradient.getStops()) {
          var s = mapper.createObjectNode();
          s.put(JsonKeys.OFFSET.key, stop.getOffset());
          s.put(JsonKeys.COLOR.key, toWebHexWithAlpha(stop.getColor()));
          stops.add(s);
        }
        g.set(JsonKeys.STOPS.key, stops);
        objectNode.set(JsonKeys.FILL.key, g);
      }
      default -> throw new RuntimeException("unsupported fill type: " + getFill());
    }

    objectNode.put(JsonKeys.STROKE.key, toWebHexWithAlpha((Color) getStroke()));
    objectNode.put(JsonKeys.CONTENT.key, getContent());
    if (getClip() != null)
      objectNode.put(JsonKeys.CLIP.key, true);
    switch (getEffect()) {
      case GaussianBlur blur -> {
        var effect = mapper.createObjectNode();
        effect.put(JsonKeys.TYPE.key, JsonKeys.GAUSSIAN_BLUR.key);
        effect.put(JsonKeys.RADIUS.key, blur.getRadius());

        objectNode.set(JsonKeys.EFFECT.key, effect);
      }
      case null, default -> {
      }
    }
    if (getBlendMode() != null)
      objectNode.put(JsonKeys.BLEND_MODE.key, getBlendMode().toString());

    return objectNode;
  }

  public String toJsonString() {
    return toJson().toString();
  }

  public void fromJson(String jsonString) {
    var mapper = new ObjectMapper();
    try {
      fromJson((ObjectNode) mapper.readTree(jsonString));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void fromJson(ObjectNode objectNode) {
    setStrokeWidth(objectNode.get(JsonKeys.STROKE_WIDTH.key).asDouble());
    setStroke(Color.web(objectNode.get(JsonKeys.STROKE.key).asText()));

    switch (objectNode.get(JsonKeys.FILL.key)) {
      case TextNode text -> setFill(Color.web(text.asText()));
      case ObjectNode obj -> {
        var stops = new ArrayList<Stop>();
        var ss = (ArrayNode) obj.get(JsonKeys.STOPS.key);
        for (var s : ss) {
          var stop = new Stop(s.get(JsonKeys.OFFSET.key).asDouble(), Color.web(s.get(JsonKeys.COLOR.key).asText()));
          stops.add(stop);
        }
        CycleMethod cycleMethod = switch (obj.get(JsonKeys.CYCLE_METHOD.key).asText()) {
          case _NO_CYCLE -> CycleMethod.NO_CYCLE;
          case _REFLECT -> CycleMethod.REFLECT;
          case _REPEAT -> CycleMethod.REPEAT;
          default ->
            throw new RuntimeException("unsupported cycleMethod type: " + obj.get(JsonKeys.CYCLE_METHOD.key).asText());
        };
        boolean proportional = obj.get(JsonKeys.PROPORTIONAL.key).asBoolean();
        switch (obj.get(JsonKeys.GRADIENT_TYPE.key).asText()) {
          case _LINEAR_GRADIENT -> {
            double startX = obj.get(JsonKeys.START_X.key).asDouble();
            double startY = obj.get(JsonKeys.START_Y.key).asDouble();
            double endX = obj.get(JsonKeys.END_X.key).asDouble();
            double endY = obj.get(JsonKeys.END_Y.key).asDouble();
            setFill(new LinearGradient(startX, startY, endX, endY, proportional, cycleMethod, stops));
          }
          case _RADIACAL_GRADIENT -> {
            double centerX = obj.get(JsonKeys.CENTER_X.key).asDouble();
            double centerY = obj.get(JsonKeys.CENTER_Y.key).asDouble();
            double radius = obj.get(JsonKeys.RADIUS.key).asDouble();
            double focusAngle = obj.get(JsonKeys.FOCUS_ANGLE.key).asDouble();
            double focusDistance = obj.get(JsonKeys.FOCUS_DISTANCE.key).asDouble();
            setFill(new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, cycleMethod, stops));
          }
          default ->
            throw new RuntimeException("unsupported gradient type: " + obj.get(JsonKeys.GRADIENT_TYPE.key).asText());
        }
      }
      default -> throw new RuntimeException("unsupported fill type: " + objectNode.get(JsonKeys.FILL.key));
    }

    var content = objectNode.get(JsonKeys.CONTENT.key).asText();

    var si = content.split(" ");
    String element = null;
    var cachePoints = new ArrayList<Point2D>();
    for (var s : si) {
      switch (s) {
        case "M", "L", "C", "Q", "S", "T" -> {
          makeSVGPathElement(element, cachePoints);
          cachePoints.clear();
          element = s;
        }
        case "", "Z" -> {
        }
        default -> {
          var pp = s.split(",");
          cachePoints.add(new Point2D(Double.parseDouble(pp[0]), Double.parseDouble(pp[1])));
        }
      }
    }
    makeSVGPathElement(element, cachePoints);
    update(content.endsWith("Z") ? "Z" : "");

    if (objectNode.has(JsonKeys.EFFECT.key)) {
      var obj = objectNode.get(JsonKeys.EFFECT.key);
      if (obj instanceof ObjectNode effect) {
        switch (effect.get(JsonKeys.TYPE.key).asText()) {
          case _GAUSSIAN_BLUR -> {
            var blur = new GaussianBlur(effect.get(JsonKeys.RADIUS.key).asDouble());
            setEffect(blur);
          }
          default -> {
          }
        }
      }
    }

    if (objectNode.has(JsonKeys.BLEND_MODE.key)) {
      var blendModeString = objectNode.get(JsonKeys.BLEND_MODE.key).asText();

      for (var blendMode : BlendMode.values()) {
        if (blendMode.toString().equals(blendModeString)) {
          setBlendMode(blendMode);
          break;
        }
      }
    }
  }

  public static String toWebHexWithAlpha(Color color) {
    return String.format("#%02X%02X%02X%02X",
      (int) (color.getRed() * 255),
      (int) (color.getGreen() * 255),
      (int) (color.getBlue() * 255),
      (int) (color.getOpacity() * 255));
  }

  private void makeSVGPathElement(String command, List<Point2D> cachedPoints) {
    if (command == null) return;
    var element = switch (command) {
      case "M" -> new MoveTo(cachedPoints.getFirst().getX(), cachedPoints.getFirst().getY());
      case "L" -> new LineTo(cachedPoints.getFirst().getX(), cachedPoints.getFirst().getY());
      case "T" -> new TransitTo(cachedPoints.getFirst().getX(), cachedPoints.getFirst().getY());
      case "C" -> {
        var p0 = cachedPoints.getFirst();
        var p1 = cachedPoints.get(1);
        var p2 = cachedPoints.getLast();
        yield new CurveTo(p0.getX(), p0.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY());
      }
      case "Q" -> {
        var p0 = cachedPoints.getFirst();
        var p1 = cachedPoints.getLast();
        yield new QuadraticTo(p0.getX(), p0.getY(), p1.getX(), p1.getY());
      }
      case "S" -> {
        var p0 = cachedPoints.getFirst();
        var p1 = cachedPoints.getLast();
        yield new SmoothTo(p0.getX(), p0.getY(), p1.getX(), p1.getY());
      }
      default -> throw new RuntimeException("unrecognized command" + command);
    };
    getSvgPathElements().add(element);
  }
}
