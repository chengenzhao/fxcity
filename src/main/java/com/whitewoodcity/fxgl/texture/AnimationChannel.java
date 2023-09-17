package com.whitewoodcity.fxgl.texture;

import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.IntStream;

public class AnimationChannel {

  private final Image image;
  private final Duration channelDuration;

  /**
   * Maps frame number in sprite sheet, so may not start with 0, to its data.
   */
  private final List<FrameData> frameData;

  private final List<Integer> sequence;
  private final double frameDuration;

  public Image getImage() {
    return image;
  }

  public Duration getChannelDuration() {
    return channelDuration;
  }

  public List<FrameData> getFrameData() {
    return frameData;
  }

  public List<Integer> getSequence() {
    return sequence;
  }

  public double getFrameDuration() {
    return frameDuration;
  }

  public AnimationChannel(Image image, Duration channelDuration, int numFrames) {
    this(image, numFrames, (int)(image.getWidth()) / numFrames, (int)(image.getHeight()), channelDuration, 0, numFrames - 1);
  }

  public AnimationChannel(Image image, Duration channelDuration, List<FrameData> frameData) {
    this.image = image;
    this.frameData = frameData;
    this.channelDuration = channelDuration;
    sequence = frameData.stream().map(FrameData::frame).toList();
    frameDuration = channelDuration.toSeconds() / sequence.size();
  }

  public AnimationChannel(Image image, int framesPerRow, int frameWidth, int frameHeight, Duration channelDuration, int startFrame, int endFrame) {
    this(image, channelDuration,
      IntStream.range(startFrame, endFrame + 1).mapToObj(it ->  new FrameData(it, (it % framesPerRow) * frameWidth, (it / framesPerRow) * frameHeight, frameWidth, frameHeight)).toList());
  }

  public boolean isLastFrame(int frame) {
    return frame == sequence.size() - 1;
  }

  public FrameData getFrameData(int frame) {
    for (FrameData frameDatum : frameData) {
      if (frameDatum.frame() == sequence.get(frame))
        return frameDatum;
    }
    return null;
  }

  public double getFrameWidth(int frame) {
    return getFrameData(frame).width();
  }
  public double getFrameHeight(int frame) {
    return getFrameData(frame).height();
  }

  public int frameAfter(int frame) {
    return (frame + 1) % sequence.size();
  }

}

record AnimationChannelData(int frameStart, int frameEnd, int frameWidth, int frameHeight) {
}
