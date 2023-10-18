package com.whitewoodcity.fxgl.service.handler;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.KeyTrigger;
import com.almasb.fxgl.input.Trigger;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.whitewoodcity.fxgl.service.Theme;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.beans.property.ObjectProperty;
import javafx.scene.CacheHint;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.whitewoodcity.fxgl.dsl.FXGL.*;

public class PromptOnceCollisionHandler<T> extends CollisionHandler {

  private int x = -1, y = -1;
  UserAction userAction;
  //  TriggerListener listener = new TriggerListener();
  XInput xInput;
  Runnable originalAction, originalPressAction, originalReleaseAction;
  PromptView promptView;
  GameWorld gameWorld;
  PhysicsWorld physicsWorld;

  public PromptOnceCollisionHandler(T a, T b, PromptView promptView, UserAction userAction) {
    super(a, b);
    this.promptView = promptView;
    this.userAction = userAction;
  }

  public PromptOnceCollisionHandler(T a, T b, double x, double y, UserAction userAction, Theme theme) {
    this(FXGL.getGameWorld(), FXGL.getPhysicsWorld(), a, b, (int) x, (int) y, userAction, theme);
  }

  public PromptOnceCollisionHandler(GameWorld gameWorld, PhysicsWorld physicsWorld, T a, T b, double x, double y, UserAction userAction, Theme theme) {
    this(gameWorld, physicsWorld, a, b, (int) x, (int) y, userAction, theme);
  }

  public PromptOnceCollisionHandler(GameWorld gameWorld, PhysicsWorld physicsWorld, T a, T b, int x, int y, UserAction userAction, Theme theme) {
    this(gameWorld, physicsWorld, a, b, userAction, theme);
    this.x = x;
    this.y = y;
    this.userAction = userAction;
  }

  public PromptOnceCollisionHandler(T a, T b, UserAction userAction, Theme theme) {
    this(FXGL.getGameWorld(), FXGL.getPhysicsWorld(), a, b, userAction, theme);
  }

  public PromptOnceCollisionHandler(GameWorld gameWorld, PhysicsWorld physicsWorld, T a, T b, UserAction userAction, Theme theme) {
    super(a, b);

    this.gameWorld = gameWorld;
    this.physicsWorld = physicsWorld;

    promptView = new PromptView("⏎ ENTER", 30);
    promptView.setBackgroundFill(theme.innerBackgroundFill);
    promptView.getText().setFill(theme.textColor);
    promptView.getText().setEffect(theme.effect);
    promptView.setCache(true);
    promptView.setCacheHint(CacheHint.SCALE);

    this.userAction = userAction;
  }

  Entity prompt;

  @Override
  protected void onCollisionBegin(Entity player, Entity entity) {

    var lift = new LiftComponent();
    lift.setGoingUp(true);
    lift.yAxisDistanceDuration(6, Duration.seconds(0.76));

    prompt = FXGL.entityBuilder()
      .at(x > 0 ? x : entity.getRightX(), y > 0 ? y : entity.getY())
      .with(lift)
      .zIndex(Integer.MAX_VALUE)
      .build();

//    prompt = gameWorld.create("prompt", new SpawnData(tessa.getRightX(), tessa.getY()));
    prompt.getViewComponent().addChild(promptView);

    spawnWithScale(gameWorld, prompt, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
    xInput = getXInput();

    originalAction = xInput.getClickAction(KeyCode.ENTER);
    originalPressAction = xInput.getPressAction(KeyCode.ENTER);
    originalReleaseAction = xInput.getReleaseAction(KeyCode.ENTER);

    xInput.onActionEnd(KeyCode.ENTER, () -> {
      if (originalReleaseAction != null) originalReleaseAction.run();
      this.onCollisionEnd(player, entity);
      physicsWorld.removeCollisionHandler(this);
      userAction.run(player, entity);
    });

//    listener.setOnTrigger(() -> {
//      Platform.runLater(() -> this.onCollisionEnd(player, tessa));
//      physicsWorld.removeCollisionHandler(this);
//      userAction.run(player, tessa);
//    });
//    FXGL.getInput().addTriggerListener(listener);
  }

  @Override
  protected void onCollisionEnd(Entity player, Entity tessa) {
    if (prompt != null)
      despawnWithScale(gameWorld, prompt, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
//    FXGL.getInput().removeTriggerListener(listener);
    if (xInput != null) {
      if (originalAction != null) xInput.onAction(KeyCode.ENTER, originalAction);
      if (originalPressAction != null) xInput.onActionBegin(KeyCode.ENTER, originalPressAction);
      if (originalReleaseAction != null) xInput.onActionEnd(KeyCode.ENTER, originalReleaseAction);
      xInput = null;
    }
  }

  public PromptView getPromptView() {
    return promptView;
  }

  public void setPromptView(PromptView promptView) {
    this.promptView = promptView;
  }

  @FunctionalInterface
  public interface UserAction {
    void run(Entity player, Entity tessa);
  }
}

class TriggerListener extends com.almasb.fxgl.input.TriggerListener {
  Runnable runnable;

  @Override
  protected void onActionEnd(Trigger trigger) {
    if (trigger instanceof KeyTrigger keyTrigger &&
      keyTrigger.getKey() == KeyCode.ENTER) {
      if (runnable != null) runnable.run();
    }
  }

  public void setOnTrigger(Runnable runnable) {
    this.runnable = runnable;
  }
}

class PromptView extends StackPane {

  Rectangle background;
  Rectangle border;
  private Text text;

  public PromptView() {
    this(24.0);
  }

  public PromptView(double size) {
    this("Test", Font.font(size - 2), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String string) {
    this(string, Font.font(22.0), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String string, double size) {
    this(string, Font.font(size), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String text, Font font, Color textColor, Color backgroundColor) {
    var size = font.getSize() + 2;
    this.text = new Text(text);
    this.text.setFill(textColor);
    this.text.setFont(font);

    background = new Rectangle(size * 0.95, size * 1.2, backgroundColor);
    border = new Rectangle(size * 1.01, size * 1.25, null);

    border.setArcWidth(size / 4);
    border.setArcHeight(size / 4);
    border.setStroke(textColor);
    border.setStrokeWidth(size / 11);
    border.strokeProperty().bind(this.text.fillProperty());

    background.setWidth(this.text.getLayoutBounds().getWidth() * 1.25);
    border.setWidth(this.text.getLayoutBounds().getWidth() * 1.26);

    this.getChildren().addAll(background, border, this.text);
  }

  public ObjectProperty<Paint> backgroundColorProperty() {
    return background.fillProperty();
  }

  public ObjectProperty<Paint> textColorProperty() {
    return text.fillProperty();
  }

  public Text getText() {
    return text;
  }

  public void setBackgroundFill(Paint fill) {
    background.setFill(fill);
  }
}
