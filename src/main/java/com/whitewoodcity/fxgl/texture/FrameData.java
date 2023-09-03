package com.whitewoodcity.fxgl.texture;

import javafx.geometry.Rectangle2D;

public record FrameData(int x, int y, int width, int height, int offsetX, int offsetY) {
  public FrameData(int x, int y, int width, int height) {
    this(x, y, width, height, 0, 0);
  }

  public Rectangle2D getViewport() {
    return new Rectangle2D(x, y, width, height);
  }
}