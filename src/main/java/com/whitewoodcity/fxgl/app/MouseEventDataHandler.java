package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.input.MouseEventData;

@FunctionalInterface
interface MouseEventDataHandler {
  void handle(MouseEventData data);
}