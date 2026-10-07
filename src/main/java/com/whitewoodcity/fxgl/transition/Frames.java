package com.whitewoodcity.fxgl.transition;

import com.whitewoodcity.javafx.imageviews.ImageViews;
import com.whitewoodcity.javafx.jvg.JVG;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
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
    this(imageView, images, Duration.seconds(images.length * secondsPerFrame));
  }

  public Frames(ImageView imageView, Image[] images, Duration duration) {
    this(imageView, images, 0, images.length, duration);
  }

  public Frames(ImageViews imageViews, int start, int end, Duration duration) {
    this(imageViews, imageViews.getImages().toArray(new Image[0]), start, end, duration);
  }

  /**
   * Constructor of frame transitions
   * @param imageView display node
   * @param images images to display during the transition
   * @param start start frame, inclusive
   * @param end end frame, exclusive
   * @param duration the duration of whole process
   */
  public Frames(ImageView imageView, Image[] images, int start, int end, Duration duration) {
    this.imageView = imageView;
    this.images = images;

    transition = new Transition() {
      {
        setCycleDuration(duration);
      }

      @Override
      protected void interpolate(double frac) {
        frac = frac % 1;
        currentFrame = (int) (frac * (end - start)) + start;
        if (imageView != null) imageView.setImage(getCurrentImage());
      }
    };
  }

  public static Image[] toImages(List<JVG> jvgs) {
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

  public void play(EventHandler<ActionEvent> handler) {
    play(1, handler);
  }

  public void play(int cycleCount, EventHandler<ActionEvent> handler) {
    transition.setCycleCount(cycleCount);
    transition.setOnFinished(handler);
    transition.playFromStart();
  }

  public void loop() {
    transition.setAutoReverse(false);
    transition.setCycleCount(Animation.INDEFINITE);
    transition.playFromStart();
  }

  public void pingPongLoop() {
    transition.setAutoReverse(true);
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
