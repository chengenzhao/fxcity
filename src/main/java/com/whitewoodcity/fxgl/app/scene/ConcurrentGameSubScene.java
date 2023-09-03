package com.whitewoodcity.fxgl.app.scene;

import com.almasb.fxgl.app.scene.GameSubScene;

public class ConcurrentGameSubScene extends GameSubScene {
  private boolean allowConcurrency = true;
  private boolean isUIScene = true;
  private boolean isGWScene = true;

  public ConcurrentGameSubScene(int width, int height) {
    super(width, height);
  }

  public ConcurrentGameSubScene(int width, int height, boolean is3D) {
    super(width, height, is3D);
  }

  /**
   *
   * @param width - width
   * @param height - height
   * @param is3D - is 3D Scene
   * @param isPureUISceneOrGameWorldScene true - UI Scene, false - Game world Scene
   */
  public ConcurrentGameSubScene(int width, int height, boolean is3D, boolean isPureUISceneOrGameWorldScene) {
    super(width, height, is3D);
    this.isUIScene = isPureUISceneOrGameWorldScene;
    this.isGWScene = !isPureUISceneOrGameWorldScene;
  }

  public ConcurrentGameSubScene(boolean allowConcurrency, int width, int height, boolean is3D) {
    this(width, height, is3D);
    this.allowConcurrency = allowConcurrency;
  }

  public ConcurrentGameSubScene(boolean allowConcurrency, int width, int height) {
    this(width, height);
    this.allowConcurrency = allowConcurrency;
  }

  public ConcurrentGameSubScene(boolean allowConcurrency, int width, int height, boolean is3D, boolean isUIOrGameWorld) {
    this(width, height, is3D, isUIOrGameWorld);
    this.allowConcurrency = allowConcurrency;
  }

  @Override
  public boolean isAllowConcurrency() {
    return allowConcurrency;
  }

  public void setAllowConcurrency(boolean allowConcurrency) {
    this.allowConcurrency = allowConcurrency;
  }

  public boolean isUIScene() {
    return isUIScene;
  }

  public boolean isGWScene() {
    return isGWScene;
  }
}
