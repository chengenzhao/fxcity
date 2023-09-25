package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitewoodcity.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.*;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"unused","rawtypes","unchecked"})
public abstract class GameApplication extends com.almasb.fxgl.app.GameApplication implements FillService {

  protected String logoString = "Xtrike";
  Map<KeyCode, Runnable> keyPresses = new HashMap<>();
  Map<KeyCode, Runnable> keyReleases = new HashMap<>();
  Map<KeyCode, Runnable> keyActions = new HashMap<>();

  protected final LinkedList<Object> gameScenes = new LinkedList<>();
  protected final Map<String, GameSceneBuilder> builderMap = new HashMap<>();
  private final String initSceneName;
  XInput input;
  UpdateService updateService;
  DimensionService dimensionService;

  public GameApplication(String initSceneName, ReplaceableGameSceneBuilder sceneBuilder) {
    builderMap.put(initSceneName, sceneBuilder);
    this.initSceneName = initSceneName;
  }

  public GameApplication(String initSceneName) {
    this.initSceneName = initSceneName;
  }

  @Override
  protected void initInput() {
    FXGL.getGameScene().setCursor(Cursor.DEFAULT);

    var keySets = Arrays.stream(KeyCode.values()).filter(keyCode -> !keyCode.isModifierKey()).collect(Collectors.toSet());

    for (KeyCode keyCode : keySets) {
      FXGL.getInput().addAction(new UserAction(keyCode.name()) {
        @Override
        protected void onActionBegin() {
          if (keyPresses.containsKey(keyCode))
            keyPresses.get(keyCode).run();
        }

        @Override
        protected void onAction() {
          if(keyActions.containsKey(keyCode))
            keyActions.get(keyCode).run();
        }

        @Override
        protected void onActionEnd() {
          if (keyReleases.containsKey(keyCode))
            keyReleases.get(keyCode).run();
        }
      }, keyCode);
    }

    gameScenes.add(getGameSceneByName(initSceneName));
    input = ((ReplaceableGameScene) gameScenes.getFirst()).initInput(keyPresses, keyReleases, keyActions);
  }

  @Override
  protected void onPreInit() {
    FXGL.getSaveLoadService().addHandler(new SaveLoadHandler());
  }

  @Override
  protected void initGame() {
    super.initGame();
    ((ReplaceableGameScene) gameScenes.getFirst()).initGame(FXGL.getGameScene().getGameWorld(), input);
  }

  @Override
  protected void initUI() {
    super.initGame();
    Platform.runLater(() -> ((ReplaceableGameScene) gameScenes.getFirst()).initUI(FXGL.getGameScene(), input));
  }

  @Override
  protected void initPhysics() {
    var config = new File("conf", "xtrike.config.json");
    if (config.exists()) {
      ObjectMapper mapper = new ObjectMapper();
      try {
        var jsonNode = mapper.readTree(config);
        FXGL.getSettings().setGlobalMusicVolume(jsonNode.path("musicVolume").asDouble(0.5));
        FXGL.getSettings().setGlobalSoundVolume(jsonNode.path("soundVolume").asDouble(1.0));
      } catch (NullPointerException e) {
        //do nothing
      } catch (Exception e) {
        Logger.get(this.getClass()).warning(e.getMessage());
      }
    }
  }


  public void registerReplaceableGameScene(String sceneName, ReplaceableGameSceneBuilder builder){
    builderMap.put(sceneName, builder);
  }

  public void registerPushAndPopGameSubScene(String sceneName, PushAndPopGameSubSceneBuilder builder){
    builderMap.put(sceneName, builder);
  }

  protected XGameScene getGameSceneByName(String sceneName, Object... parameters){
    return builderMap.get(sceneName).run(parameters);
  }

  public Object push(String sceneName, Object... parameters) {
    var app = getGameSceneByName(sceneName, parameters);

    if (app instanceof ReplaceableGameScene scene) {
      assert(!gameScenes.isEmpty());

      for(var application: gameScenes){
        if(application instanceof ExitService exitService){
          exitService.onReplaced();
        }
      }

      scene.replaceGameScene(logoString, FXGL.getGameScene(), keyPresses, keyReleases, keyActions, scene.generateGameSubScenes());

      gameScenes.clear();
      gameScenes.add(app);

//      FXGL.set(PropertyKey.SCENE_NAME, sceneName);
    }

    if (app instanceof PushAndPopGameSubScene scene) {
      if(gameScenes.getLast() instanceof ReplaceableGameSceneWithConcurrentService allowConcurrencyService){
        scene.pushSubScene(scene.generateGameSubScene(), allowConcurrencyService);
      }else {
        scene.pushSubScene(scene.generateGameSubScene());
      }
      gameScenes.add(app);
    }

    setServices();

    return app;
  }

  public void pop(){
    if(gameScenes.size() <= 1) return;

    var app = gameScenes.removeLast();

    if(app instanceof PushAndPopGameSubScene pushAndPopGameSubScene){
      if(gameScenes.getLast() instanceof ReplaceableGameSceneWithConcurrentService allowConcurrencyService){
        pushAndPopGameSubScene.popSubScene(allowConcurrencyService);
      }else {
        pushAndPopGameSubScene.popSubScene();
      }
    }

    if(app instanceof ExitService exitService){
      exitService.onPop();
    }

    setServices();
  }

  protected void setServices(){
    var app = gameScenes.getLast();

    if (app instanceof UpdateService service)
      this.updateService = service;
    else
      this.updateService = null;

    if (app instanceof DimensionService service)
      dimensionService = service;
    else
      dimensionService = null;
  }

  public double getGameWidth() {
    return (dimensionService == null) ? FXGL.getAppWidth() : dimensionService.getGameWidth();
  }

  public double getGameHeight() {
    return (dimensionService == null) ? FXGL.getAppHeight() : dimensionService.getGameHeight();
  }

  @Override
  protected void onUpdate(double tpf) {
    if (updateService != null) {
      try {
        updateService.update(tpf);
      }catch (NullPointerException e) {
        //do nothing
      } catch (Exception e){
        Logger.get(this.getClass()).warning(e.getMessage());
      }
    }
  }

  public <T> void spawn(T type, double x, double y) {
    var app = gameScenes.getLast();
    if (app instanceof SpawnService spawnService)
      try {
        spawnService.spawn(type, x, y);
      }catch (NullPointerException e) {
        //do nothing
      } catch (Exception e){
        Logger.get(this.getClass()).warning(e.getMessage());
      }
  }

  public <T> void spawn(T type, SpawnData data) {
    var app = gameScenes.getLast();
    if (app instanceof SpawnService spawnService)
      try {
        spawnService.spawn(type, data);
      }catch (NullPointerException e) {
        //do nothing
      } catch (Exception e){
        Logger.get(this.getClass()).warning(e.getMessage());
      }
  }

  public <T> void spawn(T type, SpawnData data, GameWorld gameWorld) {
    var app = gameScenes.getLast();
    if (app instanceof SpawnService spawnService)
      try {
        spawnService.spawn(type, data, gameWorld);
      }catch (NullPointerException e) {
        //do nothing
      } catch (Exception e){
        Logger.get(this.getClass()).warning(e.getMessage());
      }
  }
}
