package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.GameSubScene;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.whitewoodcity.fxgl.app.scene.ConcurrentGameSubScene;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGameScene implements ReplaceableGameSceneWithConcurrentService {

  protected List<GameScene> gameScenes;
  protected final Map<GameScene, ConcurrentGameSubScene> gameSubSceneMap = new HashMap<>();
  protected GameScene backgroundScene, immateriumScene, materiumScene;
  protected GameWorld background, immaterium, materium;

  private double gameWidth, gameHeight;

  @Override
  public List<GameSubScene> generateGameSubSceneList() {
    var concurrentGameSubScenes = List.of(
      new ConcurrentGameSubScene(getGameWidth(), getGameHeight()),
      new ConcurrentGameSubScene(getGameWidth(), getGameHeight()));

    concurrentGameSubScenes.forEach(s -> gameSubSceneMap.put(s.getGameScene(), s));

    return new ArrayList<>(concurrentGameSubScenes);
  }

  public static double calculateGameHeight(){
    var screenBound = Screen.getPrimary().getBounds();
    return screenBound.getHeight() / screenBound.getWidth() >= 2 ? 2000 : 1000;
  }

  public static double calculateGameWidth(){
    var screenBound = Screen.getPrimary().getBounds();
    return screenBound.getWidth() * calculateGameHeight() / screenBound.getHeight();
  }

  @Override
  public void setGameScene(List<GameScene> gameScenes) {
    this.gameScenes = gameScenes;
    backgroundScene = gameScenes.getFirst();
    setGameScene(backgroundScene);

    gameHeight = calculateGameHeight();
    gameWidth = calculateGameWidth();

    immateriumScene = gameScenes.get(1);
    materiumScene = gameScenes.get(2);

    background = backgroundScene.getGameWorld();
    immaterium = immateriumScene.getGameWorld();
    materium = materiumScene.getGameWorld();
  }

  @Override
  public void setGameScene(GameScene backgroundScene) {
    ReplaceableGameSceneWithConcurrentService.super.setGameScene(backgroundScene);
  }

  @Override
  public void initPhysics(PhysicsWorld physicsWorld, XInput input) {
    physicsWorld.setGravity(0, 0);
  }

  @Override
  public void disableConcurrency() {
    for (var gameScene : gameScenes) {
      if (gameSubSceneMap.containsKey(gameScene))
        gameSubSceneMap.get(gameScene).setAllowConcurrency(false);
    }
  }

  @Override
  public void restoreConcurrency() {
    for (var gameScene : gameScenes) {
      if (gameSubSceneMap.containsKey(gameScene))
        gameSubSceneMap.get(gameScene).setAllowConcurrency(true);
    }
  }

  @Override
  public void disableInput() {
    for (var gameScene : gameScenes) {
      gameScene.getInput().setRegisterInput(false);
    }
  }

  @Override
  public void restoreInput() {
    for (var gameScene : gameScenes) {
      gameScene.getInput().setRegisterInput(true);
    }
  }

  @Override
  public int getGameWidth() {
    return (int) gameWidth;
  }

  @Override
  public int getGameHeight() {
    return (int) gameHeight;
  }
}
