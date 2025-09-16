package com.whitewoodcity.fxgl.transition;

public enum RotateJsonKeys {
  X("x"),
  Y("y"),
  TRANSLATE_X("translateX"),
  TRANSLATE_Y("translateY"),
  ROTATES("rotates"),
  PIVOT_X("pivotX"),
  PIVOT_Y("pivotY"),
  ANGLE("angle"),
  TIME("time"),
  ;

  private final String key;

  RotateJsonKeys(final String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}