package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.app.scene.GameScene;
import com.whitewoodcity.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.KeyTrigger;
import com.almasb.fxgl.input.Trigger;
import com.almasb.fxgl.input.TriggerListener;
import com.whitewoodcity.fxgl.service.icons.Skip;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.*;

public interface DialogService extends ThemeService {
  default void showDialog(GameScene gameScene, double x, double y, double gameHeight, String scriptsFileName) {
    showDialog(gameScene, x, y, gameHeight, scriptsFileName, () -> {});
  }

  default void showDialog(GameScene gameScene, double x, double y, double gameHeight, String scriptsFileName, Runnable onFinished, boolean skippable) {
    showDialog(gameScene, x, y, gameHeight, new ScriptsList().append(scriptsFileName), onFinished, skippable);
  }

  default void showDialog(GameScene gameScene, double x, double y, double gameHeight, String scriptsFileName, Runnable onFinished) {
    showDialog(gameScene, x, y, gameHeight, new ScriptsList().append(scriptsFileName), onFinished, true);
  }

  private void showDialog(GameScene gameScene, double x, double y, double gameHeight, ScriptsList scripts, Runnable onFinished, boolean skippable) {
    var screenWidth = Screen.getPrimary().getBounds().getWidth();
    var screenHeight = Screen.getPrimary().getBounds().getHeight();
    double width = screenWidth / screenHeight * gameHeight;

    showDialog(gameScene, x, y, width, gameHeight, scripts, onFinished, Duration.seconds(.3), getTheme(), skippable);
  }

  private void showDialog(GameScene gameScene, double x, double y, double width, double height, ScriptsList scripts, Runnable onFinished, Duration duration, Theme theme, boolean skippable) {
    var dialogBackground = new Rectangle(1, 1);
    dialogBackground.setVisible(false);
    gameScene.addUINode(dialogBackground);

    var dialogFrame = new Rectangle(1, 1);
    dialogFrame.setVisible(false);
    gameScene.addUINode(dialogFrame);

    var skip = new Skip(theme.textColor);
    skip.setHeight(FXGL.getAppHeight() * .07);
    skip.setTranslateX((width - height * .05 / 3) - FXGL.getAppHeight() * .07 - 10);
    skip.setTranslateY(height - FXGL.getAppHeight() * .07 - 10);

    BooleanProperty skipProperty = new SimpleBooleanProperty(false);

    var transition = new Transition() {
      {
        setCycleDuration(duration);
        dialogBackground.setOpacity(1);
        dialogFrame.setOpacity(1);
        dialogBackground.setVisible(true);
        dialogFrame.setVisible(true);
        dialogBackground.setFill(theme.outerFrameColor);
        dialogFrame.setFill(theme.innerBackgroundFill);
      }

      @Override
      protected void interpolate(double frac) {
        dialogBackground.setWidth(width * frac);
        dialogBackground.setHeight(height / 3 * frac);
        dialogBackground.setTranslateX(-x * frac + x);
        dialogBackground.setTranslateY((height / 3 * 2 - y) * frac + y);
        dialogBackground.setArcWidth(50 * frac);
        dialogBackground.setArcHeight(50 * frac);

        dialogFrame.setWidth((width - height * .1 / 3) * frac);
        dialogFrame.setHeight(height / 3 * .9 * frac);
        dialogFrame.setTranslateX((height * .05 / 3 - x) * frac + x);
        dialogFrame.setTranslateY((height * 2.05 / 3 - y) * frac + y);
        dialogFrame.setArcHeight(50 * frac);
        dialogFrame.setArcWidth(50 * frac);
      }
    };

    Runnable finished = () -> {
      fadeOut(gameScene, duration, dialogBackground, dialogFrame, skip);
      onFinished.run();
    };

    transition.setOnFinished(e -> {
      if (skippable)
        fadeIn(gameScene, duration, (ee) -> {
          skip.setOnMouseEntered(event -> skip.setEffect(theme.effect));
          skip.setOnMouseExited(event -> skip.setEffect(null));
        }, skip);

      processScript(gameScene, scripts, height, width, finished, duration, theme, skipProperty);
    });
    transition.play();

    skip.setOnMouseClicked(e -> {
      skip.setDisable(true);
      skipProperty.set(true);
      gameScene.getUINodes().filtered(node -> node.getId() != null && node.getId().startsWith("_dialog"))
        .forEach(node -> fadeOut(gameScene, duration, node));
      finished.run();
    });
  }

  private void processScript(GameScene gameScene, ScriptsList list, double height, double width, Runnable onFinished, Duration duration, Theme theme) {
    processScript(gameScene, list, height, width, onFinished, duration, theme, null);
  }

  private void processScript(GameScene gameScene, ScriptsList list, double height, double width, Runnable onFinished, Duration duration, Theme theme, BooleanProperty skip) {
    if (skip != null && skip.get()) return;

    if (!list.isEmpty()) {
      var script = list.remove(0);
      var image = FXGL.image(getMugFileByName(script.left()));
      var leftView = new ImageView(image);
      leftView.setPreserveRatio(true);
      leftView.setFitHeight(image.getHeight() );
      leftView.setLayoutY(height * 2 / 3 - leftView.getFitHeight());
      if (script.talker() != 0) {
        var colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.7);
        leftView.setEffect(colorAdjust);
      } else leftView.setEffect(null);

      var from = switch (script.talker()) {
        case 0 -> script.left();
        default -> script.right();
      };
      var font = FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50);
      Dialog dialog = new Dialog(theme);
      dialog.getName().setText(from);
      dialog.getName().setFont(font);
      dialog.setTranslateX(height * .1 / 3);
      dialog.setTranslateY(height * 2.1 / 3);
      dialog.setMaxWidth(width - height * .2 / 3);

      final ImageView rightView;
      if (script.right() != null) {
        image = FXGL.image(getMugFileByName(script.right()));
        rightView = new ImageView(image);
        rightView.setPreserveRatio(true);
        rightView.setScaleX(-1);
        rightView.setFitHeight(image.getHeight());
        rightView.setFitWidth(image.getWidth());
        rightView.setLayoutY(height * 2 / 3 - rightView.getFitHeight());
        rightView.setLayoutX(width - rightView.getFitWidth());
        if (script.talker() == 0) {
          var colorAdjust = new ColorAdjust();
          colorAdjust.setBrightness(-0.7);
          rightView.setEffect(colorAdjust);
        } else rightView.setEffect(null);
      } else {
        rightView = null;
      }

      EventHandler<ActionEvent> setOnFinished = (e ->
        processLineByLine(new ArrayList<>(Arrays.asList(script.script().split("\n"))), dialog.getText(), () -> {
          fadeOut(gameScene, duration, leftView, dialog);

          if (rightView != null) {
            fadeOut(gameScene, duration, rightView);
          }

          processScript(gameScene, list, height, width, onFinished, duration, theme, skip);
        }, skip));

      leftView.setId("_dialog_leftView");
      dialog.setId("_dialog_dialog");
      if (rightView == null) {
        fadeIn(gameScene, duration, setOnFinished, leftView, dialog);
      } else {
        rightView.setId("_dialog_rightView");
        fadeIn(gameScene, duration, setOnFinished, leftView, dialog, rightView);
      }

    } else {
      onFinished.run();
    }
  }

  private void fadeIn(GameScene gameScene, Duration duration, EventHandler<ActionEvent> runnable, Node... nodes) {
    var transition = new ParallelTransition();

    for (Node node : nodes) {
      node.setOpacity(0);
      gameScene.addUINode(node);
      var ft = new FadeTransition(duration, node);
      ft.setToValue(1);
      transition.getChildren().add(ft);
    }

    transition.setOnFinished(runnable);
    transition.play();
  }

  private void fadeOut(GameScene gameScene, Duration duration, Node... nodes) {
    for (Node node : nodes) {
      var ft = new FadeTransition(duration, node);
      ft.setToValue(0);
      ft.setOnFinished(e -> gameScene.removeUINode(node));
      ft.play();
    }
  }

  String getMugFileByName(String name);

  default void processLineByLine(List<String> list, Text text, Runnable onFinished, BooleanProperty skip) {
    if (skip != null && skip.get()) return;

    if (!list.isEmpty()) {
      String line = list.remove(0);
//      var blip = FXGL.getAssetLoader().loadMusic("text-blip.mp3");
//      FXGL.getAudioPlayer().playMusic(blip);
//      blip.getAudio().setVolume(FXGL.getSettings().getGlobalSoundVolume());
      FXGL.play("text-blip.wav");
      var font = FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50);
      text.setFont(font);
      final Animation animation = new Transition() {
        {
          setCycleDuration(Duration.millis(line.length() * 25));
        }

        protected void interpolate(double frac) {
          final int length = line.length();
          final int n = Math.round(length * (float) frac);
          text.setText(line.substring(0, n));
        }
      };

      animation.setOnFinished(e -> {
//        FXGL.getAudioPlayer().stopMusic(blip);
        FXGL.getAudioPlayer().stopAllSounds();
        FXGL.getInput().addTriggerListener(new TriggerListener() {
          @Override
          protected void onActionBegin(Trigger trigger) {
            if (trigger instanceof KeyTrigger) {
              Platform.runLater(() -> {
                FXGL.getInput().removeTriggerListener(this);
                processLineByLine(list, text, onFinished, skip);
              });
            }
          }
        });
      });
      animation.play();
    } else {
//      iterator.close();
      onFinished.run();
    }
  }
}

record Script(String left, String right, int talker, String script) {
  public Script {
    Objects.requireNonNull(left);
    Objects.requireNonNull(script);
  }

  public Script(String from, String script) {
    this(from, null, 0, script);
  }

  public Script(String from, String to, String script) {
    this(from, to, 0, script);
  }
}

class ScriptsList extends LinkedList<Script> {
  public ScriptsList append(Script script) {
    this.add(script);
    return this;
  }

  public ScriptsList append(String textFilename) {
    var texts = FXGL.text(textFilename).iterator();
    while (texts.hasNext()) {
      String talkers = texts.next();
      texts.remove();
      var talker = talkers.split(" ");

      StringBuilder text = new StringBuilder();
      while (texts.hasNext()) {
        var line = texts.next();
        texts.remove();
        if (line.isBlank()) break;
        if (!text.isEmpty()) text.append(System.lineSeparator());
        text.append(line);
      }

      switch (talker.length) {
        case 1 -> {
          if (talker[0].startsWith("_")) talker[0] = talker[0].substring(1);

          this.append(new Script(talker[0], text.toString()));
        }
        case 2 -> {
          var index = 0;
          if (talker[0].startsWith("_")) talker[0] = talker[0].substring(1);
          if (talker[1].startsWith("_")) {
            talker[1] = talker[1].substring(1);
            index = 1;
          }

          this.append(new Script(talker[0], talker[1], index, text.toString()));
        }
        default -> {
          //do nothing
        }
      }
    }
    return this;
  }
}

class Dialog extends TextFlow {
  private final Text name = new Text();
  private final Text text = new Text();

  public Dialog(Theme theme) {
    name.setFill(theme.textColor);
    name.setEffect(theme.effect);

    text.setFill(theme.textColor);
    text.effectProperty().bind(name.effectProperty());

    this.getChildren().addAll(name, new Text(System.lineSeparator()), text);
  }

  public Text getName() {
    return name;
  }

  public Text getText() {
    return text;
  }
}