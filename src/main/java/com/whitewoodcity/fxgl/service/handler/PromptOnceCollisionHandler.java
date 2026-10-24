package com.whitewoodcity.fxgl.service.handler;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.whitewoodcity.fxgl.service.Theme;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.CacheHint;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import static com.whitewoodcity.fxgl.dsl.FXGL.*;

public class PromptOnceCollisionHandler<T> extends CollisionHandler {

  private int x = -1, y = -1;
  UserAction userAction;
  //  TriggerListener listener = new TriggerListener();
  XInput xInput;
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
  Runnable collisionAction = null;

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

    prompt.getViewComponent().addChild(promptView);

    spawnWithScale(gameWorld, prompt, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
    xInput = getXInput();

    collisionAction = () -> {
      this.onCollisionEnd(player, entity);
      physicsWorld.removeCollisionHandler(this);
      userAction.run(player, entity);
    };

    xInput.cacheAction(collisionAction);

    xInput.onActionEnd(KeyCode.ENTER, ()-> xInput.getFirstCachedAction().run());
  }

  @Override
  protected void onCollisionEnd(Entity player, Entity tessa) {
    if (prompt != null)
      despawnWithScale(gameWorld, prompt, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
//    FXGL.getInput().removeTriggerListener(listener);
    if (xInput != null) {
      xInput.removeCachedAction(collisionAction);
      var input = xInput;
      xInput.onActionEnd(KeyCode.ENTER, () -> input.getFirstCachedAction().run());
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