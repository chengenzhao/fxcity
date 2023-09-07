package fyi.sample.fxgl;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.whitewoodcity.fxgl.app.GameApplication;
import com.whitewoodcity.fxgl.app.ReplaceableGameSceneBuilder;
import com.whitewoodcity.fxgl.service.LoadingScene;
import com.whitewoodcity.fxgl.service.XGameScene;
import javafx.stage.Screen;

public class GameApp extends GameApplication {
  public GameApp(String initSceneName, ReplaceableGameSceneBuilder sceneBuilder) {
    super(initSceneName, sceneBuilder);
  }

  public GameApp(String initSceneName) {
    super(initSceneName);
  }

  @Override
  protected void initSettings(GameSettings settings) {
    settings.setHeight(1000);
    settings.setWidth((int) (Screen.getPrimary().getBounds().getWidth() / Screen.getPrimary().getBounds().getHeight() * 1000));
    settings.setTitle("Xtrike");
    settings.setMainMenuEnabled(false);
    settings.setGameMenuEnabled(false);
    settings.setFontUI("BlackOpsOne-Regular.ttf");
    settings.setSceneFactory(new SceneFactory() {
      @Override
      public LoadingScene newLoadingScene() {
        return new LoadingScene(getLoadingBackgroundFill(), getTheme().textColor);
      }
    });
  }

  @Override
  protected XGameScene getGameSceneByName(String sceneName, Object... parameters) {
    return switch (sceneName) {
      case Index.SCENE_NAME -> new Index();
      default -> throw new RuntimeException("Wrong XGameScene type");
    };
  }
}
