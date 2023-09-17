package com.whitewoodcity.fxgl.animation;

public class FadeAnimationBuilder extends AM {

  private double from = 0.0;
  private double to = 0.0;

  public FadeAnimationBuilder from(double start) {
    from = start;
    return this;
  }

  public FadeAnimationBuilder to(double end) {
    to = end;
    return this;
  }

  public FadeAnimationBuilder(AnimationBuilder animationBuilder) {
    super(animationBuilder);
  }

  @Override
  public Animation<?> build() {
    return buildAnimation(new AnimatedValue<>(from, to),
      value -> objects.forEach(it -> it.opacityProperty().setValue(value))
    );
  }
}
