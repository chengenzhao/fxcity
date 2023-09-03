package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.GameSubScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.whitewoodcity.fxgl.app.scene.ConcurrentGameSubScene;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public non-sealed interface PushAndPopGameSubScene extends XGameScene{
  default ConcurrentGameSubScene generateGameSubScene(){
    return new ConcurrentGameSubScene(allowConcurrency(), FXGL.getAppWidth(), FXGL.getAppHeight());
  }

  private void decorate(GameSubScene subScene) {
    setGameScene(subScene.getGameScene());
    var input = initInput(subScene.getInput());
    initGame(subScene.getGameWorld(),input);
    initPhysics(subScene.getPhysicsWorld(),input);
    initUI(subScene.getGameScene(),input);
  }

  default void setGameScene(GameScene subScene) {
    subScene.getContentRoot().setPrefWidth(FXGL.getAppWidth());
    subScene.getContentRoot().setPrefHeight(FXGL.getAppHeight());
  }

  default XInput initInput(Input input) {
    return new XInput(input);
  }

  default void initGame(GameWorld gameWorld, XInput input) {
  }

  default void initPhysics(PhysicsWorld physicsWorld, XInput input) {
    physicsWorld.setGravity(0, 960);
  }

  default void initUI(GameScene gameScene, XInput input) {
  }

  default Animation pushAnimation(GameSubScene subScene){
    var content = subScene.getContentRoot();
    content.setOpacity(0);
    return new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(content.opacityProperty(), 1)));
  }

  default void pushSubScene(GameSubScene subScene, ReplaceableGameSceneWithConcurrentService... bottomSceneServices) {
    if(!allowConcurrency()){
      for(var service:bottomSceneServices)
        service.disableConcurrency();
    }
    if(allowConcurrency() && disallowConcurrentInput()){//allow concurrency but doesn't allow concurrent input, so manually disable input
      if(bottomSceneServices.length > 0){
        for(var service:bottomSceneServices)
          service.disableInput();
      }else {
        FXGL.getSceneService().getCurrentScene().getInput().setRegisterInput(false);
      }
    }

    decorate(subScene);

    subScene.getInput().setRegisterInput(false);
    var content = subScene.getContentRoot();
    content.setDisable(true);
    var anime = pushAnimation(subScene);
    var onFinished = anime.getOnFinished();
    anime.setOnFinished(event -> {
      if(onFinished != null)
        onFinished.handle(event);
      subScene.getInput().setRegisterInput(true);
      content.setDisable(false);
    });
    anime.play();

    FXGL.getSceneService().pushSubScene(subScene);
  }

  default void popSubScene(ReplaceableGameSceneWithConcurrentService... bottomSceneServices) {
    var scene = FXGL.getSceneService().getCurrentScene();
    if (scene instanceof GameSubScene gameSubScene) {
      gameSubScene.getInput().setRegisterInput(false);
    }

    var p = scene.getContentRoot();
    p.setDisable(true);

    var timeline = new Timeline(new KeyFrame(Duration.seconds(.5), new KeyValue(p.opacityProperty(), 0)));
    timeline.setOnFinished(_1 -> {
      if(!allowConcurrency()){
        for(var service:bottomSceneServices)
          service.restoreConcurrency();
      }
      FXGL.getSceneService().popSubScene();
      if(allowConcurrency() && disallowConcurrentInput()){//resume input which was disabled when this sub-scene is pushed
        if(bottomSceneServices.length > 0){
          for(var service:bottomSceneServices)
            service.restoreInput();
        }else {
          FXGL.getSceneService().getCurrentScene().getInput().setRegisterInput(true);
        }
      }
    });
    timeline.play();
  }

  default void update() {
  }

  default boolean allowConcurrency() {
    return false;
  }

  default boolean disallowConcurrentInput(){
    return false;
  }
}
