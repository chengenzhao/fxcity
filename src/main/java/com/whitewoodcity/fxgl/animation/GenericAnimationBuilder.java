package com.whitewoodcity.fxgl.animation;

import java.util.function.Consumer;

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