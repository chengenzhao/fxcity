package com.whitewoodcity.fxgl.app;

import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;

@FunctionalInterface
public non-sealed interface PushAndPopGameSubSceneBuilder extends GameSceneBuilder{
  @Override
  PushAndPopGameSubScene run(Object... parameters);
}
