package com.whitewoodcity.fxgl.app;

import com.whitewoodcity.fxgl.service.ReplaceableGameScene;

@FunctionalInterface
public non-sealed interface ReplaceableGameSceneBuilder extends GameSceneBuilder{
  @Override
  ReplaceableGameScene run(Object... parameters);
}
