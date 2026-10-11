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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frames extends AbstractTransitions{
  private final String defaultTransitionName = "";
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

    currentTransition = buildTransition(duration, start, end);
    transitions.put(defaultTransitionName, currentTransition);
  }

  private Transition buildTransition(Duration duration, int startFrame, int endFrame){
    return new Transition() {
      {
        setCycleDuration(duration);
      }

      @Override
      protected void interpolate(double frac) {
        frac = frac % 1;
        currentFrame = (int) (frac * (endFrame - startFrame)) + startFrame;
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

  public void pingPongLoop() {
    currentTransition.setAutoReverse(true);
    currentTransition.setCycleCount(Animation.INDEFINITE);
    currentTransition.playFromStart();
  }

  public ImageView getImageView() {
    return imageView;
  }

  public int getCurrentFrame() {
    return currentFrame;
  }
}
