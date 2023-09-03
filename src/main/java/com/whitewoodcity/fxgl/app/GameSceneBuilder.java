package com.whitewoodcity.fxgl.app;

import com.whitewoodcity.fxgl.service.XGameScene;

public sealed interface GameSceneBuilder
  permits ReplaceableGameSceneBuilder, PushAndPopGameSubSceneBuilder{
  XGameScene run(Object... parameters);
}
