package com.whitewoodcity.fxgl.texture;

import javafx.geometry.Rectangle2D;

public record FrameData(int frame, int x, int y, int width, int height, int offsetX, int offsetY) {
  public FrameData(int frame, int x, int y, int width, int height) {
    this(frame, x, y, width, height, 0, 0);
  }

  public Rectangle2D getViewport() {
    return new Rectangle2D(x, y, width, height);
  }
}