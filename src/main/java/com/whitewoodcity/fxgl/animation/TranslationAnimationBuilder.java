package com.whitewoodcity.fxgl.animation;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

public class TranslationAnimationBuilder extends AM {
  public TranslationAnimationBuilder(AnimationBuilder animationBuilder) {
    super(animationBuilder);
  }

  private Shape path = null;
  private Point3D fromPoint = Point3D.ZERO;
  private Point3D toPoint = Point3D.ZERO;
  private boolean isFromSet = false;

  public TranslationAnimationBuilder alongPath(Shape path) {
    this.path = path;
    return this;
  }

  public TranslationAnimationBuilder from(Point2D start) {
    return from(new Point3D(start.getX(), start.getY(), 0.0));
  }

  public TranslationAnimationBuilder to(Point2D end) {
    return to(new Point3D(end.getX(), end.getY(), 0.0));
  }

  public TranslationAnimationBuilder from(Point3D start) {
    fromPoint = start;
    isFromSet = true;
    return this;
  }

  public TranslationAnimationBuilder to(Point3D end) {
    toPoint = end;
    return this;
  }

  @Override
  public Animation<?> build() {

    if (path != null) {
      var curve = path;
      makeAnim(switch (curve){
        case QuadCurve quadCurve -> new AnimatedQuadBezierPoint3D(quadCurve);
        case CubicCurve cubicCurve -> new AnimatedCubicBezierPoint3D(cubicCurve);
        default -> new AnimatedPath(curve);
      });
    }

    if (!isFromSet && objects.size() == 1) {
      from(new Point3D(
        objects.get(0).xProperty().getValue(),
        objects.get(0).yProperty().getValue(),
        objects.get(0).zProperty().getValue()
      ));
    }

    return makeAnim(new AnimatedPoint3D(fromPoint, toPoint));
  }

  private Animation<Point3D> makeAnim(AnimatedValue<Point3D> animValue) {
    return buildAnimation(animValue,
      value -> objects.forEach(it -> {
        it.xProperty().setValue(value.getX());
        it.yProperty().setValue(value.getY());
        it.zProperty().setValue(value.getZ());
      })
    );
  }
}