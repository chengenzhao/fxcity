package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.GameSubScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.whitewoodcity.fxgl.app.scene.ConcurrentGameSubScene;
import com.whitewoodcity.fxgl.service.component.AsyncLabel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public non-sealed interface ReplaceableGameScene extends FillService, DimensionService, XGameScene {
  default GameSubScene[] generateGameSubScenes() {
    return generateGameSubSceneList().toArray(GameSubScene[]::new);
  }

  default List<GameSubScene> generateGameSubSceneList() {
    return List.of();
  }

  default void replaceGameScene(GameScene gameScene, Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions, GameSubScene... gameSubScenes) {
    replaceGameScene("Xtrike", gameScene, keyPresses, keyReleases, keyActions, gameSubScenes);
  }

  default void replaceGameScene(String logoString, GameScene gameScene, Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions, GameSubScene... gameSubScenes) {
    keyPresses.clear();
    keyActions.clear();
    keyReleases.clear();

    if (FXGL.getSceneService().getCurrentScene().isSubState()) {
      //disable it which will be poped later in this method
      var scene = FXGL.getSceneService().getCurrentScene();
      scene.getInput().setRegisterInput(false);
      scene.getContentRoot().setDisable(true);
    }
    gameScene.getInput().setRegisterInput(false);
    gameScene.getContentRoot().setDisable(true);

    var subScene = new ConcurrentGameSubScene(FXGL.getAppWidth(), FXGL.getAppHeight());

    var paint = getLoadingBackgroundFill();

    var rect = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), paint);

    var label = new AsyncLabel(logoString);
    label.setFont(FXGL.getAssetLoader().loadFont("BlackOpsOne-Regular.ttf").newFont(200));
    label.setTextFill(getTheme().textColor);

    label.translateXProperty().bind(label.layoutBoundsProperty().map(layout -> FXGL.getAppCenter().getX() - layout.getWidth() / 2));
    label.translateYProperty().bind(label.layoutBoundsProperty().map(layout -> FXGL.getAppCenter().getY() - layout.getHeight() / 2));

    label.setEffect(getTheme().effect);

    label.opacityProperty().bind(rect.opacityProperty());

    subScene.getGameScene().addUINodes(rect, label);
    rect.setOpacity(0);

    var animation = new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(rect.opacityProperty(), 1)));
    animation.setOnFinished((e) -> {
      clearGameScene(gameScene);

      while (FXGL.getSceneService().getCurrentScene().isSubState()) {
        FXGL.getSceneService().popSubScene();
      }

      for (var gameSubScene : gameSubScenes)
        FXGL.getSceneService().pushSubScene(gameSubScene);

      FXGL.getSceneService().pushSubScene(subScene);

      //async load resources
      var service = new Service<String>() {
        @Override
        protected Task<String> createTask() {
          return new Task<>() {
            @Override
            protected String call() {
              try {
                asyncLoadResources(label);
              } catch (Throwable throwable) {
                Logger.get(this.getClass()).warning(throwable.getMessage());
              }
              return "ok";
            }
          };
        }
      };

      service.setOnSucceeded((state) ->
        //then init game
        Platform.runLater(() -> {

          decorate(gameScene, keyPresses, keyReleases, keyActions, gameSubScenes);

          var anime = new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(rect.opacityProperty(), 0)));
          anime.setOnFinished((ee) -> {
            //finally enable input
            gameScene.getInput().setRegisterInput(true);
            gameScene.getContentRoot().setDisable(false);
            FXGL.getSceneService().popSubScene();

            postInit();
          });
          anime.play();
        }));

      service.start();
    });
    animation.play();
    FXGL.getSceneService().pushSubScene(subScene);
  }

  private void decorate(GameScene gameScene, Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions, GameSubScene... gameSubScenes) {
    var scenes = Stream.concat(Stream.of(gameScene), Arrays.stream(gameSubScenes)).toList();

    var gameScenes = scenes.stream().map(scene -> {
      if (scene instanceof ConcurrentGameSubScene concurrentGameSubScene)
        return concurrentGameSubScene.getGameScene();
      else return (GameScene) scene;
    }).toList();
    setGameScene(gameScenes);

    var input = initInput(keyPresses, keyReleases, keyActions);
    initGame(gameScenes.stream().map(GameScene::getGameWorld).toList(), input);
    initPhysics(gameScenes.stream().map(GameScene::getPhysicsWorld).toList(), input);
    initUI(gameScene.getViewport(), gameScenes.getLast(), input);
  }

  default void setGameScene(List<GameScene> gameScenes) {
    var mainScene = gameScenes.getFirst();
    setGameScene(mainScene);

    gameScenes.stream().skip(1).forEach(scene -> {
      var camera = scene.getViewport().getCamera();
      scene.getViewport().bindToEntity(camera, 0, 0);

      camera.xProperty().bind(mainScene.getViewport().xProperty());
      camera.yProperty().bind(mainScene.getViewport().yProperty());

//      scene.getViewport().setX(mainScene.getViewport().getX());
//      scene.getViewport().setY(mainScene.getViewport().getY());
    });
  }

  default void setGameScene(GameScene gameScene) {
  }

  default XInput initInput(Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions) {
    return new XInput(keyPresses, keyReleases, keyActions);
  }

  default void initGame(List<GameWorld> gameWorlds, XInput input) {
    initGame(gameWorlds.getFirst(), input);
  }

  default void initGame(GameWorld gameWorld, XInput input) {
  }

  default void initPhysics(List<PhysicsWorld> physicsWorlds, XInput input) {
    for (var physicsWorld : physicsWorlds) {
      initPhysics(physicsWorld, input);
    }
  }

  default void initPhysics(PhysicsWorld physicsWorld, XInput input) {
    physicsWorld.setGravity(0, 960);
  }

  default void initUI(GameScene gameScene, XInput input) {
  }

  default void initUI(Viewport viewport, GameScene gameScene, XInput input) {
    initUI(gameScene, input);
  }

  default void asyncLoadResources(AsyncLabel label) {
  }

  default void postInit() {

  }
}
