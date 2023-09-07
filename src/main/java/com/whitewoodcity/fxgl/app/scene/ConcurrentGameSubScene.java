package com.whitewoodcity.fxgl.app.scene;

import com.almasb.fxgl.app.scene.GameSubScene;

public class ConcurrentGameSubScene extends GameSubScene {
  private boolean allowConcurrency = true;

  public ConcurrentGameSubScene(int width, int height) {
    super(width, height);
  }

  public ConcurrentGameSubScene(int width, int height, boolean is3D) {
    super(width, height, is3D);
  }

  public ConcurrentGameSubScene(boolean allowConcurrency, int width, int height, boolean is3D) {
    this(width, height, is3D);
    this.allowConcurrency = allowConcurrency;
  }

  public ConcurrentGameSubScene(boolean allowConcurrency, int width, int height) {
    this(width, height);
    this.allowConcurrency = allowConcurrency;
  }

  @Override
  public boolean isAllowConcurrency() {
    return allowConcurrency;
  }

  public void setAllowConcurrency(boolean allowConcurrency) {
    this.allowConcurrency = allowConcurrency;
  }
}
