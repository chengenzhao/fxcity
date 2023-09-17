package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.animation.Animatable;
import com.almasb.fxgl.core.UpdatableRunner;
import com.almasb.fxgl.logging.Logger;

import java.util.ArrayList;
import java.util.List;

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