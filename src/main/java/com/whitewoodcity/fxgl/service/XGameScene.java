package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public sealed interface XGameScene
  permits ReplaceableGameScene, PushAndPopGameSubScene{

  default void clearGameScene(GameScene gameScene) {
    gameScene.getViewport().unbind();
    gameScene.getViewport().getCamera().xProperty().unbind();
    gameScene.getViewport().getCamera().yProperty().unbind();
    gameScene.getPhysicsWorld().clearCollisionHandlers();
    gameScene.getTimer().clear();
    List.copyOf(gameScene.getGameWorld().getEntities()).forEach(Entity::removeFromWorld);
    gameScene.clearGameViews();
    gameScene.clearUINodes();
    gameScene.clearEffect();
    gameScene.clearCSS();
  }

  default void resetGameScene(GameScene gameScene){
    gameScene.getViewport().setX(0);
    gameScene.getViewport().getCamera().setX(0);
    gameScene.getViewport().getCamera().setY(0);
  }
}
