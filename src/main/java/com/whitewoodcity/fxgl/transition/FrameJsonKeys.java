package com.whitewoodcity.fxgl.transition;

public enum FrameJsonKeys {
  DURATION("duration"),
  START_FRAME("startFrame"),
  END_FRAME("endFrame"),
  ;

  private final String key;

  FrameJsonKeys(final String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
