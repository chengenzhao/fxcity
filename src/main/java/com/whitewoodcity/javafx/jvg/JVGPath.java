package com.whitewoodcity.javafx.jvg;

import module com.fasterxml.jackson.databind;
import module java.base;
import module javafx.controls;
import com.whitewoodcity.javafx.jvg.svgpathcommand.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Shape;


public class JVGPath extends SVGPath implements JVGLayer{

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

  public Shape daemon() {
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

  public JVGLayer trim(double x, double y) {
    return move(-x, -y);
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
    JVGLayer.super.zoom(factor);

    return this;
  }

  public JVGPath map(SVGPathElement.Apply applyX, SVGPathElement.Apply applyY) {
    for (SVGPathElement element : svgPathElements) {
      element.apply(element, applyX, applyY);
    }
    return this;
  }

  public ObjectNode toJson() {
    var objectNode = JVGLayer.super.toJson();
    objectNode.put(JsonKeys.SHAPE.key(), SVGPath.class.getSimpleName());
    objectNode.put(JsonKeys.CONTENT.key(), getContent());

    return objectNode;
  }

  public void fromJson(ObjectNode objectNode) {
    JVGLayer.super.fromJson(objectNode);

    var content = objectNode.get(JsonKeys.CONTENT.key()).asText();

    if(content.isBlank() || content.equals(" Z") || content.equals("Z")){
      throw new RuntimeException("Content of JVGPath is empty, please remove it.\nWhole json object:\n" + objectNode);
    }

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
