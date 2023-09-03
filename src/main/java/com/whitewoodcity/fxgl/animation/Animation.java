package com.whitewoodcity.fxgl.animation;

import com.almasb.fxgl.core.Updatable;
import javafx.animation.Interpolator;
import javafx.util.Duration;

public abstract class Animation<T> implements Updatable {
  AnimationBuilder config;
  final AnimatedValue<T> animatedValue;

  private boolean isAutoReverse;
  private Runnable onFinished;
  private Runnable onCycleFinished;
  private Runnable onPaused;

  private Interpolator interpolator;
  private boolean isAnimating = false;
  private boolean isReverse = false;
  private boolean isPaused = false;
  private final Duration delay;
  private boolean checkDelay;

  private double time = 0.0;
  private double endTime;
  private double pauseTime = -1;
  private int count = 0;
  private int cycleCount;

  public boolean isAnimating() {
    return isAnimating;
  }

  public boolean isReverse() {
    return isReverse;
  }

  public boolean isAutoReverse() {
    return isAutoReverse;
  }

  public void setAutoReverse(boolean autoReverse) {
    isAutoReverse = autoReverse;
  }

  public Runnable getOnFinished() {
    return onFinished;
  }

  public void setOnFinished(Runnable onFinished) {
    this.onFinished = onFinished;
  }

  public Runnable getOnCycleFinished() {
    return onCycleFinished;
  }

  public void setOnCycleFinished(Runnable onCycleFinished) {
    this.onCycleFinished = onCycleFinished;
  }

  public Interpolator getInterpolator() {
    return interpolator;
  }

  public void setInterpolator(Interpolator interpolator) {
    this.interpolator = interpolator;
  }

  public void setReverse(boolean reverse) {
    isReverse = reverse;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public double getTime() {
    return time;
  }

  public void setTime(double time) {
    this.time = time;
  }

  public int getCycleCount() {
    return cycleCount;
  }

  public void setCycleCount(int cycleCount) {
    this.cycleCount = cycleCount;
  }

  public Animation(AnimationBuilder config, AnimatedValue<T> animatedValue) {
    this.config = config;
    this.animatedValue = animatedValue;

    isAutoReverse = config.isAutoReverse;
    onFinished = config.onFinished;
    onCycleFinished = config.onCycleFinished;
    interpolator = config.interpolator;

    endTime = config.duration.toSeconds();

    delay = config.delay;
    checkDelay = delay.greaterThan(Duration.ZERO);

    cycleCount = config.times;
  }

  @Override
  public void onUpdate(double tpf) {
    if (isPaused || !isAnimating)
      return;

    if (checkDelay) {
      time += tpf;

      if (time >= delay.toSeconds()) {
        checkDelay = false;
        resetTime();
        return;
      } else {
        return;
      }
    }

    updateTime(tpf);

    if (pauseTime >= 0 &&
      ((!isReverse && time >= pauseTime) || (isReverse && time <= pauseTime))) {
      jumpTo(pauseTime);
      pause();
      pauseTime = -1;
      if (onPaused != null) {
        onPaused.run();
        onPaused = null;
      }
    }

    if ((!isReverse && time >= endTime) || (isReverse && time <= 0.0)) {
      onProgress(animatedValue.getValue(isReverse ? 0.0 : 1.0));

      onCycleFinished.run();
      count++;

      if (count >= cycleCount) {
        onFinished.run();
        stop();
      } else {
        if (isAutoReverse) {
          isReverse = !isReverse;
        }

        resetTime();
      }

      return;
    }

    onProgress(animatedValue.getValue(time / endTime, interpolator));
  }

  private void resetTime() {
    time = isReverse ? endTime : 0.0;
  }

  private void updateTime(double tpf) {
    time += isReverse ? -tpf : tpf;
  }

  public void jumpTo(double newTime) {
    time = newTime;

    onProgress(animatedValue.getValue(time / endTime, interpolator));
  }

  public void start() {
    if (!isAnimating) {
      isAnimating = true;
      isPaused = false;
      resetTime();
      onProgress(animatedValue.getValue(isReverse ? 1.0 : 0.0));
    }
  }

  public void stop() {
    if (isAnimating) {
      isAnimating = false;
      time = 0.0;
      count = 0;
      isReverse = false;
      checkDelay = delay.greaterThan(Duration.ZERO);
    }
  }

  public void startReverse() {
    if (!isAnimating) {
      isReverse = true;
      start();
    }
  }

  public void play() {
    if (!isAnimating) {
      isAnimating = true;
      onProgress(animatedValue.getValue(isReverse ? 1.0 : 0.0));
    }
  }

  public void playTo(double pauseTime) {
    if (isAnimating && !isPaused) return;
    this.pauseTime = pauseTime;
    if (!isAnimating) play();
    if (isPaused) resume();
  }

  public void playTo(double pauseTime, Runnable onPaused) {
    if (isAnimating && !isPaused) return;
    this.onPaused = onPaused;
    playTo(pauseTime);
  }

  public void pause() {
    isPaused = true;
  }

  public void resume() {
    isPaused = false;
  }

  public abstract void onProgress(T value);

}
