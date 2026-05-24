package com.whitewoodcity.javafx.jvg;

import static com.whitewoodcity.javafx.jvg.JVGLayer.*;

public enum JsonKeys {
  STROKE_WIDTH("strokeWidth"),
  FILL("fill"),
  STROKE("stroke"),
  CONTENT("content"),
  CLIP("clip"),
  BLEND_MODE("blendMode"),
  EFFECT("effect"),
  TYPE("type"),
  GAUSSIAN_BLUR(_GAUSSIAN_BLUR),
  RADIUS("radius"),
  FOCUS_ANGLE("focusAngle"),
  FOCUS_DISTANCE("focusDistance"),
  CENTER_X("centerX"),
  CENTER_Y("centerY"),
  START_X("startX"),
  START_Y("startY"),
  END_X("endX"),
  END_Y("endY"),
  X("x"),
  Y("y"),
  WIDTH("width"),
  HEIGHT("height"),
  PROPORTIONAL("proportional"),
  CYCLE_METHOD("cycleMethod"),
  STOPS("stops"),
  OFFSET("offset"),
  COLOR("color"),
  NO_CYCLE(_NO_CYCLE),
  REFLECT(_REFLECT),
  REPEAT(_REPEAT),
  GRADIENT_TYPE("gradientType"),
  SHAPE("shape");

  private final String key;

  JsonKeys(final String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
