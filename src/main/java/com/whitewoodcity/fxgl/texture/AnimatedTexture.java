package com.whitewoodcity.fxgl.texture;

import com.whitewoodcity.fxgl.animation.AnimatedValue;
import com.whitewoodcity.fxgl.animation.Animation;
import com.whitewoodcity.fxgl.animation.AnimationBuilder;
import com.whitewoodcity.fxgl.core.util.EmptyRunnable;
import javafx.animation.Interpolator;
import javafx.beans.property.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimatedTexture extends Texture {

  private final IntegerProperty currentFrame = new SimpleIntegerProperty(0);
  private final DoubleProperty time = new SimpleDoubleProperty(0);

  private Animation<Integer> animation;
  private List<Animation<Integer>> animationList = new ArrayList<>();

  private Runnable onCycleFinished = new EmptyRunnable();

  private AnimationChannel animationChannel;
  private Interpolator interpolator = Interpolator.LINEAR;

  public AnimatedTexture(AnimationChannel channel) {
    super(channel.getImage());
    animationChannel = channel;

    updateImage();
    updateAnimation();
  }

  public void updateAnimatedTexture(AnimationChannel channel){
    animationChannel = channel;
    currentFrame.set(0);
    updateImage();
    updateAnimation();
  }

  public void setInterpolator(Interpolator interpolator) {
    this.interpolator = interpolator;
    animation.setInterpolator(interpolator);
  }

  public AnimationChannel getAnimationChannel() {
    return animationChannel;
  }

  private void setAnimationChannel(AnimationChannel animationChannel) {
    this.animationChannel = animationChannel;
    updateAnimation();
  }

  private void updateImage() {
    var frameData = animationChannel.getFrameData(currentFrame.get());

    setImage(animationChannel.getImage());
    setFitWidth(frameData.width());
    setFitHeight(frameData.height());
    setViewport(frameData.getViewport());

    setLayoutX(frameData.offsetX());
    setLayoutY(frameData.offsetY());
  }

  private void updateAnimation() {
    animation = new AnimationBuilder()
      .onCycleFinished(() -> {
        if (animation.getCycleCount() > 1) {
          currentFrame.set(0);
          updateImage();
        }

        onCycleFinished.run();
      })
      .duration(Duration.seconds(animationChannel.getFrameDuration() * animationChannel.getSequence().size()))
      .interpolator(interpolator)
      .animate(new PreciseAnimatedIntValue(0, animationChannel.getSequence().size() - 1))
      .onProgress(frameNum -> {
        currentFrame.set(Math.min(frameNum, animationChannel.getSequence().size() - 1));
        updateImage();
      })
      .build();
  }

  public void jumpTo(int startFrame) {
//    currentFrame.set(startFrame);
    animation.jumpTo(currentFrame.get() * animationChannel.getFrameDuration());
  }

  public void jumpTo(Duration duration){
    animation.jumpTo(duration.toSeconds());
//    currentFrame.set(((int)(duration.toSeconds() / animationChannel.getFrameDuration())));
  }

  public int getCurrentFrame() {
    return currentFrame.get();
  }

  public AnimatedTexture play() {
    animation.play();
    return this;
  }

  public AnimatedTexture play(AnimationChannel channel) {
    setAnimationChannel(channel);

    animation.play();
    return this;
  }

  public AnimatedTexture playTo(int pauseFrame){
    animation.setReverse(currentFrame.get() > pauseFrame);
    animation.playTo(pauseFrame * animationChannel.getFrameDuration() );
    return this;
  }

  public AnimatedTexture playTo(int pauseFrame, Runnable runnable){
    animation.setReverse(currentFrame.get() > pauseFrame);
    animation.playTo(pauseFrame * animationChannel.getFrameDuration(), runnable);
    return this;
  }

  public AnimatedTexture playFrom(int startFrame) {
    jumpTo(startFrame);
    animation.play();
    return this;
  }

  public AnimatedTexture playFromStart(){
    animation.stop();
    animation.setCycleCount(1);
    animation.start();

    return this;
  }

  public AnimatedTexture playReverse() {
    animation.stop();
    animation.setCycleCount(1);
    animation.startReverse();

    return this;
  }

  public AnimatedTexture loopReverse() {
    animation.stop();
    animation.setCycleCount(Integer.MAX_VALUE);
    animation.startReverse();

    return this;
  }

  public AnimatedTexture loop() {
    animation.stop();
    animation.setCycleCount(Integer.MAX_VALUE);
    animation.start();

    return this;
  }

  public AnimatedTexture loop(AnimationChannel channel) {
    setAnimationChannel(channel);

    animation.stop();
    animation.setCycleCount(Integer.MAX_VALUE);
    animation.start();

    return this;
  }

  public AnimatedTexture loopNoOverride(final AnimationChannel channel) {
    if (animationChannel == channel && animation.isAnimating())
      return this;

    loop(channel);
    return this;
  }

  public void stop() {
    animation.stop();

    currentFrame.set(0);

    updateImage();
  }

  public boolean isAnimating(){
    return animation.isAnimating();
  }

  public boolean isPaused(){
    return animation.isPaused();
  }

  public void pause(){
    animation.pause();
  }

  public void resume(){
    animation.resume();
  }

  public ReadOnlyIntegerProperty currentFrameProperty() {
    return currentFrame;
  }

  public double getTime() {
    return time.get();
  }

  public ReadOnlyDoubleProperty timeProperty() {
    return time;
  }
  
  public void setOnCycleFinished(Runnable onCycleFinished) {
    this.onCycleFinished = onCycleFinished;
  }

  @Override
  public void onUpdate(double tpf) {
    animation.onUpdate(tpf);
    time.set(animation.getTime());
  }
}

class PreciseAnimatedIntValue extends AnimatedValue<Integer> {

  public PreciseAnimatedIntValue(Integer start, Integer end) {
    super(start, end);
  }

  @Override
  public Integer animate(Integer _1, Integer _2, double progress, Interpolator interpolator) {
    var p = interpolator.interpolate(0.0, 1.0, progress);

    var segments = super.to - super.from + 1;

    var unitProgress = 1.0 / segments;

    var howManyProgresses = (int) (p / unitProgress);

    return super.from + howManyProgresses > super.to ? super.to : super.from + howManyProgresses;
  }

  public static void main(String[] args) {
    var p = new PreciseAnimatedIntValue(10, 13);
    System.out.println(p.animate(0, 1, 0, Interpolator.LINEAR));
    System.out.println(p.animate(0, 1, .25, Interpolator.LINEAR));
    System.out.println(p.animate(0, 1, .5, Interpolator.LINEAR));
    System.out.println(p.animate(0, 1, .75, Interpolator.LINEAR));
    System.out.println(p.animate(0, 1, 1, Interpolator.LINEAR));
  }
}
