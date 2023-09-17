package com.whitewoodcity.fxgl.animation;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class ScaleAnimationBuilder extends AM {

  public ScaleAnimationBuilder(AnimationBuilder animationBuilder) {
    super(animationBuilder);
  }

  private Point3D startScale = new Point3D(1.0, 1.0, 1.0);
  private Point3D endScale = new Point3D(1.0, 1.0, 1.0);
  private Point3D scaleOrigin;

  public ScaleAnimationBuilder from(Point2D start){
    startScale =new Point3D(start.getX(), start.getY(), 1.0);
    return this;
  }

  public ScaleAnimationBuilder to(Point2D end) {
    endScale = new Point3D(end.getX(), end.getY(), 1.0);
    return this;
  }

  public ScaleAnimationBuilder from(Point3D start){
    startScale = start;
    return this;
  }

  public ScaleAnimationBuilder to(Point3D end) {
    endScale = end;
    return this;
  }

  public ScaleAnimationBuilder origin(Point2D scaleOrigin) {
    this.scaleOrigin = new  Point3D(scaleOrigin.getX(), scaleOrigin.getY(), 0.0);
    return this;
  }

  public ScaleAnimationBuilder origin(Point3D scaleOrigin) {
    this.scaleOrigin = scaleOrigin;
    return this;
  }

  public Animation<?> build() {
    if(scaleOrigin != null){
      objects.forEach(it -> it.setScaleOrigin(scaleOrigin));
    }

    return buildAnimation(
      new AnimatedPoint3D(startScale, endScale),
      value -> objects.forEach(it -> {
        it.scaleXProperty().setValue(value.getX());
        it.scaleYProperty().setValue(value.getY());
        it.scaleZProperty().setValue(value.getZ());
      }));
  }
}