package fyi.sample.fxgl;

import com.whitewoodcity.atlantafx.base.theme.CityDark;
import com.whitewoodcity.fxgl.app.ReplaceableGameSceneBuilder;
import com.whitewoodcity.fxgl.service.FillService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApplication extends Application implements FillService {
  @Override
  public void start(Stage stage) {
    Application.setUserAgentStylesheet(new CityDark().getUserAgentStylesheet());

    stage.setWidth(Screen.getPrimary().getBounds().getWidth() * .75);
    stage.setHeight(Screen.getPrimary().getBounds().getHeight() * .75);

    var game = new GameApp(Index.SCENE_NAME);//GameApp(Index.SCENE_NAME, (_1)-> new Index())
    var gamePane = GameApp.embeddedLaunch(game);
    stage.setScene(new Scene(gamePane));

    stage.getScene().setFill(getLoadingBackgroundFill());

    gamePane.prefWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.prefHeightProperty().bind(stage.getScene().heightProperty());
    gamePane.renderWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.renderHeightProperty().bind(stage.getScene().heightProperty());

    gamePane.setRenderFill(Color.TRANSPARENT);

//    stage.setFullScreen(true);
    stage.show();
  }

  public static void main(String[] args) {
    System.setProperty("prism.lcdtext", "false");
    MainApplication.launch(MainApplication.class, args);
  }

}
