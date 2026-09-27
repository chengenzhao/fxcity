package com.whitewoodcity.fxgl.transition;

import com.whitewoodcity.javafx.jvg.JVG;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class Frames {
  private final Transition transition;
  private int currentFrame = 0;
  private final Image[] images;
  private final ImageView imageView;

  public Frames(ImageView imageView, List<Image> imageList, double secondsPerFrame) {
    this(imageView, imageList.toArray(new Image[0]), secondsPerFrame);
  }

  public Frames(ImageView imageView, Image[] images, double secondsPerFrame) {
    this.imageView = imageView;
    this.images = images;

    transition = new Transition() {
      {
        setCycleDuration(Duration.seconds(images.length * secondsPerFrame));
      }

      @Override
      protected void interpolate(double frac) {
        frac = frac % 1;
        currentFrame = (int) (frac * images.length);
        if (imageView != null) imageView.setImage(getCurrentImage());
      }
    };
  }

  public static Image[] toImages(List<JVG> jvgs){
    return toImages(jvgs.toArray(new JVG[0]));
  }

  public static Image[] toImages(JVG[] jvgs) {
    var images = new Image[jvgs.length];
    for (int i = 0; i < images.length; i++) {
      images[i] = jvgs[i].toImage();
    }
    return images;
  }

  public Image getCurrentImage() {
    return images[currentFrame];
  }

  public void play() {
    play(1);
  }

  public void play(int cycleCount) {
    play(cycleCount, null);
  }

  public void play(EventHandler<ActionEvent> handler){
    play(1,handler);
  }

  public void play(int cycleCount, EventHandler<ActionEvent> handler){
    transition.setCycleCount(cycleCount);
    transition.setOnFinished(handler);
    transition.playFromStart();
  }

  public void loop() {
    transition.setCycleCount(Animation.INDEFINITE);
    transition.playFromStart();
  }

  public void stop() {
    transition.stop();
  }

  public void pause() {
    transition.pause();
  }

  public void resume() {
    transition.play();
  }

  public ImageView getImageView() {
    return imageView;
  }

  public int getCurrentFrame() {
    return currentFrame;
  }
}
