package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.animation.Animatable;
import com.almasb.fxgl.core.UpdatableRunner;
import com.whitewoodcity.fxgl.core.util.EmptyRunnable;
import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class AnimationBuilder {
  protected final UpdatableRunner scene;

  public AnimationBuilder() {
    this.scene = null;
  }

  public AnimationBuilder(UpdatableRunner scene) {
    this.scene = scene;
  }

  Duration duration = Duration.seconds(1.0);
  Duration delay = Duration.ZERO;
  Interpolator interpolator = Interpolator.LINEAR;
  int times = 1;
  Runnable onCycleFinished = new EmptyRunnable();
  boolean isAutoReverse = false;
  Runnable onFinished = new EmptyRunnable();

  public AnimationBuilder(AnimationBuilder copy) {
    this(copy.scene);
    duration = copy.duration;
    delay = copy.delay;
    interpolator = copy.interpolator;
    times = copy.times;
    onFinished = copy.onFinished;
    onCycleFinished = copy.onCycleFinished;
    isAutoReverse = copy.isAutoReverse;
  }

  public AnimationBuilder duration(Duration duration) {
    this.duration = duration;
    return this;
  }

  public AnimationBuilder delay(Duration delay) {
    this.delay = delay;
    return this;
  }

  public AnimationBuilder interpolator(Interpolator interpolator) {
    this.interpolator = interpolator;
    return this;
  }


  public AnimationBuilder repeat(int times) {
    this.times = times;
    return this;
  }

  public AnimationBuilder repeatInfinitely() {
    return repeat(Integer.MAX_VALUE);
  }

  public AnimationBuilder onCycleFinished(Runnable onCycleFinished) {
    this.onCycleFinished = onCycleFinished;
    return this;
  }

  public AnimationBuilder autoReverse(boolean autoReverse) {
    this.isAutoReverse = autoReverse;
    return this;
  }

  public AnimationBuilder onFinished(Runnable onFinished) {
    this.onFinished = onFinished;
    return this;
  }

  public <T> Animation<T> buildAnimation(AnimatedValue<T> animatedValue, Consumer<T> onProgress) {
    return new Animation<>(this, animatedValue) {
      @Override
      public void onProgress(T value) {
        onProgress.accept(value);
      }
    };
  }
  public <T> GenericAnimationBuilder<T> animate(AnimatedValue<T> value) {
    return new GenericAnimationBuilder<>(this, value);
  }

  public TranslationAnimationBuilder translate(Animatable... entities) {
    var builder = new TranslationAnimationBuilder(this);
    builder.getObjects().addAll(List.of(entities));
    return builder;
  }

  public TranslationAnimationBuilder translate(Node... entities) {
    var builder = new TranslationAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public TranslationAnimationBuilder translate(Collection<?> entities) {
    var builder = new TranslationAnimationBuilder(this);
    builder.getObjects().addAll(entities.stream().map(this::toAnimatable).toList());
    return builder;
  }

  public ScaleAnimationBuilder scale(Animatable... entities) {
    var builder = new ScaleAnimationBuilder(this);
    builder.getObjects().addAll(List.of(entities));
    return builder;
  }

  public ScaleAnimationBuilder scale(Node... entities) {
    var builder = new ScaleAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public ScaleAnimationBuilder scale(Collection<?> entities) {
    var builder = new ScaleAnimationBuilder(this);
    builder.getObjects().addAll(entities.stream().map(this::toAnimatable).toList());
    return builder;
  }

  public FadeAnimationBuilder fade(Animatable... entities) {
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll( List.of(entities));
    return builder;
  }

  public FadeAnimationBuilder fade(Node... entities) {
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public FadeAnimationBuilder fade(Collection<?> entities) {
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(entities.stream().map(this::toAnimatable).toList());
    return builder;
  }
  
  public FadeAnimationBuilder fadeIn(Animatable... entities){
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(List.of(entities));
    return builder.from(0.0).to(1.0);
  }

  public FadeAnimationBuilder fadeIn(Node... entities){
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder.from(0.0).to(1.0);
  }

  public FadeAnimationBuilder fadeOut(Animatable... entities){
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(List.of(entities));
    return builder.from(1.0).to(0.0);
  }

  public FadeAnimationBuilder fadeOut(Node... entities){
    var builder = new FadeAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder.from(1.0).to(0.0);
  }

  public RotationAnimationBuilder rotate(Animatable... entities) {
    var builder = new RotationAnimationBuilder(this);
    builder.getObjects().addAll(List.of(entities));
    return builder;
  }

  public RotationAnimationBuilder rotate(Node... entities){
    var builder = new RotationAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public RotationAnimationBuilder rotate(Collection<?> entities) {
    var builder = new RotationAnimationBuilder(this);
    builder.getObjects().addAll(entities.stream().map(this::toAnimatable).toList());
    return builder;
  }

  private Animatable toAnimatable(Object object) {
    return switch (object){
      case Node node -> toAnimatable(node);
      case Animatable animatable -> animatable;
      default -> throw new IllegalArgumentException("${obj.javaClass} must be Node or Animatable");
    };
  }

  private Animatable toAnimatable(Node node) {
    final var n = node;
    return new Animatable() {
      private Scale scale = null;
      private Rotate rotateX = null;
      private Rotate rotateY = null;
      private Rotate rotateZ = null;

      public DoubleProperty xProperty() {
        return n.translateXProperty();
      }

      public DoubleProperty yProperty() {
        return n.translateYProperty();
      }

      public DoubleProperty zProperty() {
        return n.translateZProperty();
      }

      public DoubleProperty scaleXProperty() {
        if (scale != null)
          return scale.xProperty();
        else
          return n.scaleXProperty();
      }

      public DoubleProperty scaleYProperty() {
        if (scale != null)
          return scale.yProperty();
        else
          return n.scaleYProperty();
      }

      public DoubleProperty scaleZProperty() {
        if (scale != null)
          return scale.zProperty();
        else
          return n.scaleZProperty();
      }

      public DoubleProperty rotationXProperty() {
        return rotateX.angleProperty();
      }

      public DoubleProperty rotationYProperty() {
        return rotateY.angleProperty();
      }

      public DoubleProperty rotationZProperty() {
        if (rotateZ != null)
          return rotateZ.angleProperty();
        else
          return n.rotateProperty();
      }

      public DoubleProperty opacityProperty() {
        return n.opacityProperty();
      }

      public void setScaleOrigin(Point3D pivotPoint) {
        // if a node already has a previous transform, reuse it
        // this means the node was animated previously
        if (n.getProperties().get("anim_scale") != null) {
          var scale = (Scale) n.getProperties().get("anim_scale");
          scale.setPivotX(pivotPoint.getX());
          scale.setPivotY(pivotPoint.getY());
          scale.setPivotZ(pivotPoint.getZ());
        } else {
          scale = new Scale(0.0, 0.0, 0.0, pivotPoint.getX(), pivotPoint.getY(), pivotPoint.getZ());
          n.getTransforms().add(scale);
          n.getProperties().put("anim_scale", scale);
        }
      }

      public void setRotationOrigin(Point3D pivotPoint) {
        // if a node already has a previous transform, reuse it
        // this means the node was animated previously
        if (n.getProperties().get("anim_rotate_x") != null) {
          var rotateX = (Rotate) n.getProperties().get("anim_rotate_x");
          rotateX.setPivotX(pivotPoint.getX());
          rotateX.setPivotY(pivotPoint.getY());
          rotateX.setPivotZ(pivotPoint.getZ());
        }
        if (n.getProperties().get("anim_rotate_y") != null) {
          var rotateY = (Rotate) n.getProperties().get("anim_rotate_y");
          rotateY.setPivotX(pivotPoint.getX());
          rotateY.setPivotY(pivotPoint.getY());
          rotateY.setPivotZ(pivotPoint.getZ());
        }
        if (n.getProperties().get("anim_rotate_z") != null) {
          var rotateZ = (Rotate) n.getProperties().get("anim_rotate_z");
          rotateZ.setPivotX(pivotPoint.getX());
          rotateZ.setPivotY(pivotPoint.getY());
          rotateZ.setPivotZ(pivotPoint.getZ());
          return;
        }

        rotateZ = new Rotate(0.0, pivotPoint.getX(), pivotPoint.getY(), pivotPoint.getZ(), Rotate.Z_AXIS);
        rotateY = new Rotate(0.0, pivotPoint.getX(), pivotPoint.getY(), pivotPoint.getZ(), Rotate.Y_AXIS);
        rotateX = new Rotate(0.0, pivotPoint.getX(), pivotPoint.getY(), pivotPoint.getZ(), Rotate.X_AXIS);

        n.getProperties().put("anim_rotate_x", rotateX);
        n.getProperties().put("anim_rotate_y", rotateY);
        n.getProperties().put("anim_rotate_z", rotateZ);

        n.getTransforms().addAll(rotateZ, rotateY, rotateX);
      }
    };
  }
}