package com.whitewoodcity.fxgl.animation;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class RotationAnimationBuilder extends AM {
  public RotationAnimationBuilder(AnimationBuilder animationBuilder) {
    super(animationBuilder);
  }

  private boolean is3DAnimation = false;
  private Point3D startRotation = Point3D.ZERO;
  private Point3D endRotation = Point3D.ZERO;
  private Point3D rotationOrigin = null;

  public RotationAnimationBuilder from(double startAngle) {
    is3DAnimation = false;
    startRotation = new Point3D(0.0, 0.0, startAngle);
    return this;
  }

  public RotationAnimationBuilder to(double endAngle) {
    is3DAnimation = false;
    endRotation = new Point3D(0.0, 0.0, endAngle);
    return this;
  }

  public RotationAnimationBuilder from(Point3D start) {
    is3DAnimation = true;
    startRotation = start;
    return this;
  }

  public RotationAnimationBuilder to(Point3D end) {
    is3DAnimation = true;
    endRotation = end;
    return this;
  }

  public RotationAnimationBuilder origin(Point2D rotationOrigin) {
    this.rotationOrigin = new Point3D(rotationOrigin.getX(), rotationOrigin.getY(), 0.0);
    return this;
  }

  public RotationAnimationBuilder origin(Point3D rotationOrigin) {
    this.rotationOrigin = rotationOrigin;
    return this;
  }

  @Override
  public Animation<?> build() {
    // force 3D rotations to be generated to avoid NPE when accessing rotationX/Y properties
    if (is3DAnimation) {
      objects.forEach(it -> it.setRotationOrigin(Point3D.ZERO));
    }

    if (rotationOrigin != null) {
      objects.forEach(it -> it.setRotationOrigin(rotationOrigin));
    }

    return buildAnimation(new AnimatedValue<>(startRotation, endRotation),
      value -> objects.forEach(it -> {
          if (is3DAnimation) {
            it.rotationXProperty().setValue(value.getX());
            it.rotationYProperty().setValue(value.getY());
          }
          it.rotationZProperty().setValue(value.getZ());
        }
      )
    );
  }
}
