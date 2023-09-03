package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;

public interface UpdateService {
  default void update(double tpf){}
}
