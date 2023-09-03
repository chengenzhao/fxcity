package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.core.math.FXGLMath;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class AnimatedValue<T> {
  protected final T from, to;

  public AnimatedValue(final T from, final T to) {
    this.from = from;
    this.to = to;
  }

  public T getValue(double progress) {
    return animate(from, to, progress, Interpolator.LINEAR);
  }

  public T getValue(double progress, Interpolator interpolator) {
    return animate(from, to, progress, interpolator);
  }

  @SuppressWarnings("unchecked")
  public T animate(T val1, T val2, double progress, Interpolator interpolator) {
    return (T) interpolator.interpolate(val1, val2, progress);
  }
}

class AnimatedPoint2D extends AnimatedValue<Point2D> {

  public AnimatedPoint2D(Point2D from, Point2D to) {
    super(from, to);
  }

  public Point2D animate(Point2D val1, Point2D val2, double progress, Interpolator interpolator) {
    return interpolate(val1, val2, progress, interpolator);
  }

  private Point2D interpolate(Point2D fromValue, Point2D toValue, double progress, Interpolator interpolator) {
    final var x = interpolator.interpolate(fromValue.getX(), toValue.getX(), progress);
    final var y = interpolator.interpolate(fromValue.getY(), toValue.getY(), progress);

    return new Point2D(x, y);
  }
}

class AnimatedPoint3D extends AnimatedValue<Point3D> {

  public AnimatedPoint3D(Point3D from, Point3D to) {
    super(from, to);
  }

  public Point3D animate(Point3D val1, Point3D val2, double progress, Interpolator interpolator) {
    return interpolate(val1, val2, progress, interpolator);
  }

  private Point3D interpolate(Point3D fromValue, Point3D toValue, double progress, Interpolator interpolator) {
    final var x = interpolator.interpolate(fromValue.getX(), toValue.getX(), progress);
    final var y = interpolator.interpolate(fromValue.getY(), toValue.getY(), progress);
    final var z = interpolator.interpolate(fromValue.getZ(), toValue.getZ(), progress);

    return new Point3D(x, y, z);
  }
}

class AnimatedQuadBezierPoint2D extends AnimatedValue<Point2D> {
  private final QuadCurve path;

  public AnimatedQuadBezierPoint2D(final QuadCurve path) {
    super(Point2D.ZERO, Point2D.ZERO);
    this.path = path;
  }

  public Point2D animate(Point2D val1, Point2D val2, double progress, Interpolator interpolator) {
    return FXGLMath.bezier(
      new Point2D(path.getStartX(), path.getStartY()),
      new Point2D(path.getControlX(), path.getControlY()),
      new Point2D(path.getEndX(), path.getEndY()),
      interpolator.interpolate(0.0, 1.0, progress)
    );
  }
}

class AnimatedCubicBezierPoint2D extends AnimatedValue<Point2D> {

  private final CubicCurve path;

  public AnimatedCubicBezierPoint2D(CubicCurve path) {
    super(Point2D.ZERO, Point2D.ZERO);
    this.path = path;
  }

  public Point2D animate(Point2D val1, Point2D val2, double progress, Interpolator interpolator) {
    return FXGLMath.bezier(
      new Point2D(path.getStartX(), path.getStartY()),
      new Point2D(path.getControlX1(), path.getControlY1()),
      new Point2D(path.getControlX2(), path.getControlY2()),
      new Point2D(path.getEndX(), path.getEndY()),
      interpolator.interpolate(0.0, 1.0, progress)
    );
  }
}

class AnimatedQuadBezierPoint3D extends AnimatedValue<Point3D> {
  private final AnimatedQuadBezierPoint2D animated2D;

  public AnimatedQuadBezierPoint3D(final QuadCurve path) {
    super(Point3D.ZERO, Point3D.ZERO);
    animated2D = new AnimatedQuadBezierPoint2D(path);
  }

  public Point3D animate(Point3D val1, Point3D val2, double progress, Interpolator interpolator) {
    final var p = animated2D.animate(new Point2D(val1.getX(), val1.getY()),
      new Point2D(val2.getX(), val2.getY()), progress, interpolator);

    return new Point3D(p.getX(), p.getY(), 0.0);
  }
}

class AnimatedCubicBezierPoint3D extends AnimatedValue<Point3D> {

  private final AnimatedCubicBezierPoint2D animated2D;

  public AnimatedCubicBezierPoint3D(final CubicCurve path) {
    super(Point3D.ZERO, Point3D.ZERO);
    animated2D = new AnimatedCubicBezierPoint2D(path);
  }

  public Point3D animate(Point3D val1, Point3D val2, double progress, Interpolator interpolator) {
    final var p = animated2D.animate(new Point2D(val1.getX(), val1.getY()),
      new Point2D(val2.getX(), val2.getY()), progress, interpolator);

    return new Point3D(p.getX(), p.getY(), 0.0);
  }
}

class AnimatedPath extends AnimatedValue<Point3D> {

  /**
   * Maps reference time values [0..1] to points on path at that time.
   */
  private final Map<Integer, Point3D> points = new HashMap<>();

  public AnimatedPath(Shape path) {
    super(Point3D.ZERO, Point3D.ZERO);

    final var dummy = new Rectangle();

    final var pt = new PathTransition(Duration.seconds(1.0), path, dummy);
    pt.play();

    var t = 0.0;
    var percent = 0;

    while (t < 1.0) {
      points.put(percent++, new Point3D(dummy.getTranslateX(), dummy.getTranslateY(), 0.0));

      t += 0.01;

      pt.jumpTo(Duration.seconds(t));
    }

    pt.jumpTo(Duration.seconds(1.0));

    // hack to ensure that points[0] is not (0, 0)
    points.put(0, points.get(1));
    points.put(100, new Point3D(dummy.getTranslateX(), dummy.getTranslateY(), 0.0));
  }

  public Point3D animate(Point3D val1, Point3D val2, double progress, Interpolator interpolator) {
    final var t = interpolator.interpolate(0.0, 1.0, progress);

    final var key = (int) (t * 100);

    return points.get(key);
  }
}