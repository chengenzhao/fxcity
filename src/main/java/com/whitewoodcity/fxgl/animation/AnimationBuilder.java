package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.core.UpdatableRunner;
import com.almasb.fxgl.logging.Logger;
import com.whitewoodcity.fxgl.core.util.EmptyRunnable;
import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import com.almasb.fxgl.animation.Animatable;

import java.util.ArrayList;
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
    builder.getObjects().addAll(Arrays.asList(entities));
    return builder;
  }

  public TranslationAnimationBuilder translate(Node... entities) {
    var builder = new TranslationAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public TranslationAnimationBuilder translate(Collection<?> entities) {
    var builder = new TranslationAnimationBuilder(this);
    entities.forEach(this::toAnimatable);
    return builder;
  }

  public ScaleAnimationBuilder scale(Animatable... entities) {
    var builder = new ScaleAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.asList(entities));
    return builder;
  }

  public ScaleAnimationBuilder scale(Node... entities) {
    var builder = new ScaleAnimationBuilder(this);
    builder.getObjects().addAll(Arrays.stream(entities).map(this::toAnimatable).toList());
    return builder;
  }

  public ScaleAnimationBuilder scale(Collection<?> entities) {
    var builder = new ScaleAnimationBuilder(this);
    entities.forEach(this::toAnimatable);
    return builder;
  }


  public abstract class AM extends AnimationBuilder {
    protected List<Animatable> objects = new ArrayList<>();

    public List<Animatable> getObjects() {
      return objects;
    }

    private final AnimationBuilder animationBuilder;

    public AM(AnimationBuilder animationBuilder) {
      super(animationBuilder);
      this.animationBuilder = animationBuilder;
    }

    public abstract Animation<?> build();

    public void buildAndPlay() {
      if (animationBuilder.scene != null) {
        buildAndPlay(animationBuilder.scene);
      } else {
        Logger.get(this.getClass()).warning("No game scene was set to AnimationBuilder");
      }
    }

    public void buildAndPlay(UpdatableRunner scene) {
      var animation = build();
      animation.setOnFinished(() -> {
        scene.removeListener(animation);
        onFinished.run();
      });
      animation.start();
      scene.addListener(animation);
    }
  }

  public class GenericAnimationBuilder<T> extends AM {

    private Consumer<T> progressConsumer = t -> {
    };
    private final AnimatedValue<T> animValue;

    public GenericAnimationBuilder(AnimationBuilder animationBuilder , final AnimatedValue<T> animValue) {
      super(animationBuilder);
      this.animValue = animValue;
    }

    public GenericAnimationBuilder<T> onProgress(Consumer<T> progressConsumer){
      this.progressConsumer = progressConsumer;
      return this;
    }

    public Animation<T> build()  {
      return buildAnimation(animValue, progressConsumer);
    }
  }

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
        //todo use enhanced switch when upgrading to jdk 21
        if (curve instanceof QuadCurve quadCurve)
          return makeAnim(new AnimatedQuadBezierPoint3D(quadCurve));
        else if (curve instanceof CubicCurve cubicCurve)
          return makeAnim(new AnimatedCubicBezierPoint3D(cubicCurve));
        else
          return makeAnim(new AnimatedPath(curve));
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
        new Consumer<Point3D>() {
          @Override
          public void accept(Point3D value) {
            objects.forEach(it -> {
              it.scaleXProperty().setValue(value.getX());
              it.scaleYProperty().setValue(value.getY());
              it.scaleZProperty().setValue(value.getZ());
            });
          }
        });
    }
  }

  private Animatable toAnimatable(Object object) {
    //todo use enhanced switch when upgrading to 21
    if(object instanceof Node node){
      return toAnimatable(node);
    }else if(object instanceof Animatable animatable){
      return animatable;
    }else{
      throw new IllegalArgumentException("${obj.javaClass} must be Node or Animatable");
    }
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