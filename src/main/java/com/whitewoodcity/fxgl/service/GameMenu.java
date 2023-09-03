package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

public class GameMenu extends FXGLMenu {

  private static final int SIZE = 150;
  private final Animation<?> animation;

  Text textResume;
  Text textExit;

  private final Paint fill;
  private final Color textColor;
  private final Color borderColor;

  public GameMenu(Theme theme){
    this(theme.innerBackgroundFill, theme.textColor, theme.outerFrameColor);
  }

  public GameMenu(Paint fill, Color textColor, Color borderColor) {
    super(MenuType.GAME_MENU);

    this.fill = fill;
    this.textColor = textColor;
    this.borderColor = borderColor;

    getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
    getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);
    textResume = FXGL.getUIFactoryService().newText("Resume", textColor, FontType.UI, 50);
//    textExit = FXGL.getUIFactoryService().newText("Exit", Color.WHITE, FontType.UI, 50);
    var resume = buildMenuItem(textResume, this::fireResume);
//    var exit = buildMenuItem(textExit,
//      () -> Navigator.replaceNamed("/map", FXGL.<ShootThemUp>getAppCast().getParameters()));
//    exit.setLayoutY(SIZE + 2.5);

    getContentRoot().getChildren().addAll(resume);//exit

    animation = FXGL.animationBuilder()
      .duration(Duration.seconds(0.66))
      .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
      .scale(getContentRoot())
      .from(new Point2D(0, 0))
      .to(new Point2D(1, 1))
      .build();
  }

  @Override
  public void onCreate() {
    animation.setOnFinished(EmptyRunnable.INSTANCE);
    animation.stop();
    animation.start();
  }

  @Override
  protected void onUpdate(double tpf) {
    animation.onUpdate(tpf);
  }

  public void setMenuStrings(Map<String, String> menuStrings) {
    textResume.setText(menuStrings.get("resume"));
//    textExit.setText(menuStrings.get("exit"));
  }

  private Pane buildMenuItem(Text text, Runnable setOnClick) {
    var shape = new Rectangle(0, 0, SIZE * 2, SIZE);

    var pane = new StackPane();
    pane.getChildren().addAll(shape, text);
    pane.prefWidthProperty().bind(shape.widthProperty());
    pane.prefHeightProperty().bind(shape.heightProperty());

    shape.setStrokeWidth(2.5);
    shape.strokeProperty().bind(
      Bindings.when(pane.hoverProperty())
        .then(textColor)
        .otherwise(borderColor)
    );

    shape.setFill(fill);//Color.web("3978edcc")

    pane.setOnMouseClicked(_1 -> setOnClick.run());

    return pane;
  }
}