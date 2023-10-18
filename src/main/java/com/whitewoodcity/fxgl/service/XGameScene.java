package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public sealed interface XGameScene
  permits ReplaceableGameScene, PushAndPopGameSubScene{

  default void clearGameScene(GameScene gameScene) {
    //todo fxgl 21 release change to .unbind();
//    gameScene.getViewport().unbind();
//    gameScene.getViewport().setX(0);
    var viewport = gameScene.getViewport();
    viewport.bindToEntity(viewport.getCamera(), 0, 0);
    viewport.getCamera().xProperty().unbind();
    viewport.getCamera().yProperty().unbind();
    viewport.getCamera().setX(0);
    viewport.getCamera().setY(0);
    gameScene.getPhysicsWorld().clearCollisionHandlers();
    gameScene.getTimer().clear();
    List.copyOf(gameScene.getGameWorld().getEntities()).forEach(Entity::removeFromWorld);
    gameScene.clearGameViews();
    gameScene.clearUINodes();
    gameScene.clearEffect();
    gameScene.clearCSS();
  }
}
