package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.core.UpdatableRunner;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimationBuilderTest {

  private UpdatableRunner scene;
  private AnimationBuilder builder;
  private MockEntity e;
  private Node node;

  @BeforeEach
  void setUp() {
    scene = new MockRunner();
    builder = new AnimationBuilder(scene);

    e = new MockEntity();
    node = new Rectangle();
  }

  @Test
  void duration() {
    final var anim = builder.duration(Duration.seconds(2.0))
      .translate(e)
      .from(new Point2D(10.0, 10.0))
      .to(new Point2D(50.0, 50.0))
      .build();

    anim.start();

    assertEquals(10.0,e.getX(),0.001);
    assertEquals(10.0,e.getY(),0.001);

    anim.onUpdate(0.5);

    assertEquals( 20.0,e.getX(),0.001);
    assertEquals( 20.0,e.getY(),0.001);

    anim.onUpdate(0.5);

    assertEquals( 30.0,e.getX(),0.001);
    assertEquals( 30.0,e.getY(),0.001);

    anim.onUpdate(0.5);

    assertEquals(40.0,e.getX(),0.001);
    assertEquals(40.0,e.getY(),0.001);

    anim.onUpdate(0.5);

    assertEquals( 50.0,e.getX(),0.001);
    assertEquals( 50.0,e.getY(),0.001);
  }
}

class MockRunner implements UpdatableRunner {
  private final List<Updatable> listeners = new CopyOnWriteArrayList<>();

  public void addListener(Updatable updatable) {
    listeners.add(updatable);
  }

  public void removeListener(Updatable updatable) {
    listeners.remove(updatable);
  }

  void update(double tpf) {
    listeners.forEach(it -> it.onUpdate(tpf) );
  }
}

class MockEntity implements com.almasb.fxgl.animation.Animatable {
  private final SimpleDoubleProperty propX = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propY = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propZ = new SimpleDoubleProperty();

  private final SimpleDoubleProperty propRotX = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propRotY = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propRotZ = new SimpleDoubleProperty();

  private final SimpleDoubleProperty propScaleX = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propScaleY = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propScaleZ = new SimpleDoubleProperty();
  private final SimpleDoubleProperty propOpacity = new SimpleDoubleProperty();
  Point3D scaleOriginValue = Point3D.ZERO;
  Point3D rotationOriginValue = Point3D.ZERO;
  public double getX() {
    return propX.getValue();
  }
  public double getY() {
    return propY.getValue();
  }
  public double getZ(){
    return propZ.getValue();
  }
  public double getRotation(){
    return propRotZ.getValue();
  }
  public double getRotationX(){
    return propRotX.getValue();
  }
  public double getRotationY(){
    return propRotY.getValue();
  }
  public double getRotationZ(){
    return propRotZ.getValue();
  }
  public double getScaleX() {
    return propScaleX.getValue();
  }
  public double getScaleY() {
    return propScaleY.getValue();
  }
  public double getScaleZ() {
    return propScaleZ.getValue();
  }
  public double getOpacity(){
    return propOpacity.getValue();
  }

  @Override
  public DoubleProperty xProperty() {
    return propX;
  }

  @Override
  public DoubleProperty yProperty() {
    return propY;
  }

  @Override
  public DoubleProperty zProperty() {
    return propZ;
  }

  @Override
  public DoubleProperty scaleXProperty() {
    return propScaleX;
  }

  @Override
  public DoubleProperty scaleYProperty() {
    return propScaleY;
  }

  @Override
  public DoubleProperty scaleZProperty() {
    return propScaleZ;
  }

  @Override
  public DoubleProperty rotationXProperty() {
    return propRotX;
  }

  @Override
  public DoubleProperty rotationYProperty() {
    return propRotY;
  }

  @Override
  public DoubleProperty rotationZProperty() {
    return propRotZ;
  }

  @Override
  public DoubleProperty opacityProperty() {
    return propOpacity;
  }

  @Override
  public void setScaleOrigin(Point3D pivotPoint) {
    scaleOriginValue = pivotPoint;
  }

  @Override
  public void setRotationOrigin(Point3D pivotPoint) {
    rotationOriginValue = pivotPoint;
  }
}
