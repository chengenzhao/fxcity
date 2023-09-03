package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.app.scene.ErrorSubScene;
import com.almasb.fxgl.app.scene.FXGLScene;
import com.almasb.fxgl.core.fsm.StateMachine;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.MouseEventData;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.scene.CSS;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.scene.SubScene;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract sealed class MainWindow
  permits EmbeddedPaneWindow, PrimaryStageWindow {
  /**
   * The starting scene which is used when the window is created.
   */
  FXGLScene scene;

  protected ReadOnlyGameSettings settings;

  public MainWindow(FXGLScene scene, ReadOnlyGameSettings settings) {
    this.scene = scene;
    this.settings = settings;
  }

  protected Logger log = Logger.get(MainWindow.class);

  ReadOnlyObjectWrapper<FXGLScene> currentFXGLSceneProperty = new ReadOnlyObjectWrapper<>(scene);
  ReadOnlyObjectWrapper<Scene> currentSceneProperty = new ReadOnlyObjectWrapper<>(scene);

  FXGLScene currentFXGLScene;

  public FXGLScene getCurrentFXGLScene() {
    return currentFXGLSceneProperty.getValue();
  }

  Scene currentScene;

  public Scene getCurrentScene() {
    return currentSceneProperty.getValue();
  }

  Runnable onClose = null;
  ImageCursor defaultCursor = null;

  protected double x, y, width, height;
  protected boolean isFocused;

  protected List<Scene> scenes = new ArrayList<>();

  protected List<SceneEventSubscriber<?>> eventSubscribers = new ArrayList<>();

  protected DoubleProperty scaleRatioX = new SimpleDoubleProperty();
  protected DoubleProperty scaleRatioY = new SimpleDoubleProperty();
  protected DoubleProperty scaledWidth = new SimpleDoubleProperty();
  protected DoubleProperty scaledHeight = new SimpleDoubleProperty();

  protected StateMachine<Scene> stateMachine = new StateMachine<>(scene);

  /**
   * Input that is active in any scene.
   */
  Input input = new Input();

  protected void setInitialScene(FXGLScene scene) {
    registerScene(scene);

    currentFXGLSceneProperty.setValue(scene);
    scene.activeProperty().set(true);
    currentSceneProperty.setValue(scene);

    log.debug("Set initial scene to $scene");
  }

  abstract ReadOnlyBooleanProperty iconifiedProperty();

  /**
   * Add desktop taskbar / window icon.
   * Multiple images of different sizes can be added: 16x16, 32x32
   * and most suitable will be chosen.
   * Can only be called before [show].
   */
  abstract void addIcons(Image... images);

  abstract void addCSS(CSS... cssList);

  Boolean isInHierarchy(Scene scene) {
    return stateMachine.isInHierarchy(scene);
  }

  void update(double tpf) {
    input.update(tpf);
    stateMachine.runOnActiveStates(it -> {
      it.update(tpf);
      return null;
    });
  }

  /**
   * Set current FXGL scene.
   * The scene will be immediately displayed.
   *
   * @param scene the scene
   */
  void setScene(FXGLScene scene) {
    popAllSubScenes();

    if (!scenes.contains(scene)) {
      registerScene(scene);
    }

    var prevScene = stateMachine.getParentState();

    stateMachine.changeState(scene);

    if (stateMachine.getParentState() == prevScene) {
      log.warning("Cannot set to $scene. Probably because subscenes are present.");
      return;
    }

    prevScene.getInput().clearAll();

    currentFXGLSceneProperty.getValue().activeProperty().set(false);

    currentFXGLSceneProperty.setValue(scene);
    setRoot(scene.getRoot());
    scene.activeProperty().set(true);

    currentSceneProperty.setValue(scene);

    log.debug(prevScene + " -> " + scene);
  }

  protected abstract void setRoot(Pane root);

  void pushState(SubScene newScene) {
    log.debug("Push state: " + newScene);

    var prevScene = stateMachine.getCurrentState();

    stateMachine.changeState(newScene);

    prevScene.getInput().clearAll();

    // push view to content root, which is correctly offset, scaled etc.
    currentFXGLScene.getContentRoot().getChildren().add(newScene.getRoot());

    currentSceneProperty.setValue(stateMachine.getCurrentState());

    log.debug(prevScene + " -> " + stateMachine.getCurrentState());
  }

  void popState() {
    final var prevScene = stateMachine.getCurrentState();

    if (!stateMachine.popSubState()) {
      log.warning("Cannot pop substate. Probably because substates are empty!");
      return;
    }

    log.debug("Pop state: " + prevScene);

    prevScene.getInput().clearAll();

    // pop view
    currentFXGLScene.getContentRoot().getChildren().remove(prevScene.getRoot());

    currentSceneProperty.setValue(stateMachine.getCurrentState());

    log.debug(stateMachine.getCurrentState() + " <- " + prevScene);
  }

  void popAllSubScenes() {
    while (currentScene != currentFXGLScene) {
      popState();
    }
  }

  abstract void show();

  /**
   * Register an FXGL scene to be managed by display settings.
   *
   * @param scene the scene
   */
  private void registerScene(Scene scene) {
    scene.bindSize(scaledWidth, scaledHeight, scaleRatioX, scaleRatioY);

    if (!settings.isNative()
      && settings.isDesktop()
      && scene instanceof FXGLScene fxglScene
      && scene.getRoot().getCursor() == null) {
      if (defaultCursor != null) {
        fxglScene.setCursor(defaultCursor.getImage(), new Point2D(defaultCursor.getHotspotX(), defaultCursor.getHotspotY()));
      }
    }

    scenes.add(scene);
  }

  protected void addKeyHandler(javafx.scene.Scene fxScene, EventHandler<KeyEvent> handler) {
    eventSubscribers.add(new SceneEventSubscriber<>(fxScene, KeyEvent.ANY, handler, false));
  }

  protected void addMouseHandler(javafx.scene.Scene fxScene, MouseEventDataHandler handler) {
    var mouseHandler = new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent mouseEvent) {
        var data = new MouseEventData(
          mouseEvent,
          new Point2D(currentFXGLScene.getContentRoot().getTranslateX(), currentFXGLScene.getContentRoot().getTranslateY()),
          new Point2D(currentFXGLScene.getViewport().getX(), currentFXGLScene.getViewport().getY()),
          currentFXGLScene.getViewport().getZoom(),
          scaleRatioX.getValue(),
          scaleRatioY.getValue()
        );

        handler.handle(data);
      }
    };

    eventSubscribers.add(new SceneEventSubscriber<>(fxScene, MouseEvent.ANY, mouseHandler, false));
  }

  protected void addGlobalHandler(javafx.scene.Scene fxScene, EventHandler<Event> filter, EventHandler<Event> handler) {
    eventSubscribers.add(new SceneEventSubscriber<>(fxScene, EventType.ROOT, filter, true));
    eventSubscribers.add(new SceneEventSubscriber<>(fxScene, EventType.ROOT, handler, false));
  }

  protected void removeAllEventFiltersAndHandlers(javafx.scene.Scene fxScene) {
    eventSubscribers.removeIf((it) -> {
      var shouldRemove = it.fxScene == fxScene;

      if (shouldRemove) it.unsubscribe();

      return shouldRemove;
    });
  }

  abstract Image takeScreenshot();

  void showFatalError(Throwable error, Runnable action) {
    pushState(new ErrorSubScene(settings.getWidth(), settings.getHeight(), error, action));
  }

  abstract void close();
}

final class PrimaryStageWindow extends MainWindow {
  private Stage stage;
  private javafx.scene.Scene fxScene;

  /**
   * Primary stage.
   */
  PrimaryStageWindow(
    Stage stage,
    FXGLScene scene,
    ReadOnlyGameSettings settings
  ) {
    super(scene, settings);
    this.stage = stage;

    x = stage.getX();
    y = stage.getY();
    width = stage.getWidth();
    height = stage.getHeight();
    isFocused = stage.isFocused();

    fxScene = createFXScene(scene.getRoot());

    setInitialScene(scene);

    initStage();

    addKeyHandler(fxScene, e -> {
      input.onKeyEvent(e);
      stateMachine.runOnActiveStates(it -> {
        it.getInput().onKeyEvent(e);
        return null;
      });
    });

    addMouseHandler(fxScene, e -> {
      input.onMouseEvent(e);
      stateMachine.runOnActiveStates(it -> {
        it.getInput().onMouseEvent(e);
        return null;
      });
    });

    // reroute any events to current state input
    addGlobalHandler(fxScene,
      e -> {
        input.fireEventViaFilters(e);
        stateMachine.runOnActiveStates(it -> {
          it.getInput().fireEventViaFilters(e);
          return null;
        });
      },
      e -> {
        input.fireEventViaHandlers(e);
        stateMachine.runOnActiveStates(it -> {
          it.getInput().fireEventViaHandlers(e);
          return null;
        });
      }
    );
  }

  /**
   * Construct the only JavaFX scene with computed size based on user settings.
   */
  private javafx.scene.Scene createFXScene(Parent root) {
    log.debug("Creating a JavaFX scene");

    double newW = settings.width;
    double newH = settings.height;

    var bounds = (settings.isFullScreenAllowed) ? Screen.getPrimary().getBounds() :
      Screen.getPrimary().getVisualBounds();

    if (newW > bounds.getWidth() || newH > bounds.getHeight()) {
      log.debug("Target size > screen size");

      // margin so the window size is slightly smaller than bounds
      // to account for platform-specific window borders
      var extraMargin = 25.0;
      var ratio = newW / newH;

      for (int newWidth = (int) bounds.getWidth(); newWidth >= 1; newWidth--) {
        if (newWidth / ratio <= bounds.getHeight()) {
          newW = newWidth - extraMargin;
          newH = newWidth / ratio;
          break;
        }
      }
    }

    // round to a whole number
    newW = (int) newW;
    newH = (int) newH;

    var scene = new javafx.scene.Scene(root, newW, newH);

    scaledWidth.set(newW);
    scaledHeight.set(newH);
    scaleRatioX.set(scaledWidth.getValue() / settings.getWidth());
    scaleRatioY.set(scaledHeight.getValue() / settings.getHeight());

    log.debug("Target settings size: " + (double) settings.getWidth() + " x " + (double) settings.getHeight());
    log.debug("Scaled scene size:    " + newW + " x " + newH);
    log.debug("Scaled ratio: (" + scaleRatioX.getValue() + ", " + scaleRatioY.getValue() + ")");

    return scene;
  }

  /**
   * Configure main stage based on user settings.
   */
  private void initStage() {
    stage.setScene(fxScene);

    stage.setTitle(settings.getTitle() + " " + settings.getVersion());

    stage.setResizable(settings.isManualResizeEnabled());

    if (settings.isDesktop()) {
      stage.initStyle(settings.getStageStyle());
    }

    stage.setOnCloseRequest(e -> {
      e.consume();
      if (onClose != null)
        onClose.run();
    });

    if (settings.isFullScreenAllowed()) {
      stage.setFullScreenExitHint("");
      // don't let the user exit FS mode manually
      stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

      settings.fullScreen.addListener((observable, old, fullscreenNow) -> {
        stage.setFullScreen(fullscreenNow);
      });
    }

    stage.sizeToScene();
    stage.centerOnScreen();
  }

  private double windowBorderWidth = 0.0;
  private double windowBorderHeight = 0.0;

  @Override
  void show() {
    log.debug("Opening main window");

    stage.show();

    // platform offsets
    windowBorderWidth = stage.getWidth() - scaledWidth.getValue();
    windowBorderHeight = stage.getHeight() - scaledHeight.getValue();

    // this is a hack to estimate platform offsets on ubuntu and potentially other Linux os
    // because for some reason javafx does not create a stage to contain scene of given size
    if (windowBorderHeight < 0.5 && settings.isLinux()) {
      windowBorderHeight = 35.0;
    }

    scaledWidth.bind(stage.widthProperty().subtract(
      Bindings.when(stage.fullScreenProperty()).then(0).otherwise(windowBorderWidth)
    ));
    scaledHeight.bind(stage.heightProperty().subtract(
      Bindings.when(stage.fullScreenProperty()).then(0).otherwise(windowBorderHeight)
    ));

    settings.scaledWidthProp.bind(scaledWidth);
    settings.scaledHeightProp.bind(scaledHeight);

    if (settings.isScaleAffectedOnResize()) {
      if (settings.isPreserveResizeRatio()) {
        scaleRatioX.bind(Bindings.min(
          scaledWidth.divide(settings.getWidth()), scaledHeight.divide(settings.getHeight())
        ));
        scaleRatioY.bind(scaleRatioX);
      } else {
        scaleRatioX.bind(scaledWidth.divide(settings.getWidth()));
        scaleRatioY.bind(scaledHeight.divide(settings.getHeight()));
      }
    } else {
      scaleRatioX.setValue(1.0);
      scaleRatioY.setValue(1.0);

      scaledWidth.addListener((observable, old, newWidth) -> onStageResize());
      scaledHeight.addListener((observable, old, newHeight) -> onStageResize());
    }

    log.debug("Window border size: " + windowBorderWidth + ", " + windowBorderHeight);
    log.debug("Scaled size: " + scaledWidth.getValue() + " x " + scaledHeight.getValue());
    log.debug("Scaled ratio: (" + scaleRatioX.getValue() + ", " + scaleRatioY.getValue() + ")");
    log.debug("Scene size: " + stage.getScene().getWidth() + " x " + stage.getScene().getHeight());
    log.debug("Stage size: " + stage.getWidth() + " x " + stage.getHeight());

    if (settings.isFullScreenAllowed && settings.isFullScreenFromStart) {
      stage.setFullScreen(true);

      log.debug("Going fullscreen");
    }
  }

  @Override
  void close() {
    log.debug("Closing main window");

    stage.close();
  }

  @Override
  void addIcons(Image... images) {
    if (!settings.isNative) {
      stage.getIcons().addAll(images);
    }
  }

  @Override
  void addCSS(CSS... cssList) {
    var cssArrays = Arrays.stream(cssList).map(CSS::getExternalForm).toList();
    fxScene.getStylesheets().addAll(cssArrays);
  }

  @Override
  protected void setRoot(Pane root) {
    fxScene.setRoot(root);
  }

  @Override
  ReadOnlyBooleanProperty iconifiedProperty() {
    return stage.iconifiedProperty();
  }

  @Override
  Image takeScreenshot() {
    return fxScene.snapshot(null);
  }

  /**
   * Called when the user has resized the main window.
   * Only called when settings.isScaleAffectedOnResize = false.
   */
  private void onStageResize() {
    var newW = scaledWidth.getValue();
    var newH = scaledHeight.getValue();

    log.debug("On Stage resize: " + newW + "x" + newH);

    scenes.stream().filter(FXGLScene.class::isInstance)
      .map(FXGLScene.class::cast)
      .forEach(it -> {
        it.getViewport().setWidth(newW);
        it.getViewport().setHeight(newH);
      });
  }
}

final class EmbeddedPaneWindow extends MainWindow {

  private final FXGLPane fxglPane;

  EmbeddedPaneWindow(
    FXGLPane fxglPane,
    FXGLScene scene,
    ReadOnlyGameSettings settings
  ) {
    super(scene, settings);
    this.fxglPane = fxglPane;

    computeScaledDimensions();

    // this clips the max area (ensures max size)
    clipRect.widthProperty().bind(fxglPane.renderWidthProperty());
    clipRect.heightProperty().bind(fxglPane.renderHeightProperty());
    fxglPane.setClip(clipRect);

    // this rect ensures min size
    backgroundRect.widthProperty().bind(fxglPane.renderWidthProperty());
    backgroundRect.heightProperty().bind(fxglPane.renderHeightProperty());
    backgroundRect.fillProperty().bind(fxglPane.renderFillProperty());
    fxglPane.getAllChildren().addAll(backgroundRect);

    setInitialScene(scene);

    fxglPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {

      if (oldScene != null) {
        removeAllEventFiltersAndHandlers(oldScene);
      }

      if (newScene != null) {
        addKeyHandler(newScene, e -> {
          input.onKeyEvent(e);
          stateMachine.runOnActiveStates(it -> {
            it.getInput().onKeyEvent(e);
            return null;
          });
        });

        // we also take fxglPane scene location into account, hence we add event subscriber directly
        EventHandler<MouseEvent> mouseHandler = event -> {
          MouseEventData data = new MouseEventData(
            event,
            fxglPane.localToScene(0.0, 0.0).add(currentFXGLScene.getContentRoot().getTranslateX(), currentFXGLScene.getContentRoot().getTranslateY()),
            new Point2D(currentFXGLScene.getViewport().getX(), currentFXGLScene.getViewport().getY()),
            currentFXGLScene.getViewport().getZoom(),
            scaleRatioX.getValue(),
            scaleRatioY.getValue()
          );

          input.onMouseEvent(data);
          stateMachine.runOnActiveStates(it -> {
            it.getInput().onMouseEvent(data);
            return null;
          });
        };

        eventSubscribers.add(new SceneEventSubscriber<>(newScene, MouseEvent.ANY, mouseHandler, false));

        // reroute any events to current state input
        addGlobalHandler(newScene,
          e -> {
            input.fireEventViaFilters(e);
            stateMachine.runOnActiveStates(it -> {
              it.getInput().fireEventViaFilters(e);
              return null;
            });
          },
          e -> {
            input.fireEventViaHandlers(e);
            stateMachine.runOnActiveStates(it -> {
              it.getInput().fireEventViaHandlers(e);
              return null;
            });
          }
        );
      }
    });
  }

  private Rectangle backgroundRect = new Rectangle();
  private Rectangle clipRect = new Rectangle();

  public double getX() {
    return fxglPane.localToScreen(0.0, 0.0).getX();
  }

  public double getY() {
    return fxglPane.localToScreen(0.0, 0.0).getY();
  }

  boolean isFocused;

  // TODO: fix impl
  public boolean isFocused() {
    return true;
  }

  public double getWidth() {
    return fxglPane.getRenderWidth();
  }

  public double getHeight() {
    return fxglPane.getRenderHeight();
  }


  private void computeScaledDimensions() {
    double newW = settings.width;
    double newH = settings.height;

    var bounds = new Rectangle2D(0.0, 0.0, newW, newH);

    if (newW > bounds.getWidth() || newH > bounds.getHeight()) {
      log.debug("Target size > screen size");

      // margin so the window size is slightly smaller than bounds
      // to account for platform-specific window borders
      var extraMargin = 25.0;
      var ratio = newW / newH;

      for (int newWidth = (int) bounds.getWidth(); newWidth >= 1; newWidth--) {
        if (newWidth / ratio <= bounds.getHeight()) {
          newW = newWidth - extraMargin;
          newH = newWidth / ratio;
          break;
        }
      }
    }

    // round to a whole number
    newW = (int) newW;
    newH = (int) newH;

    scaledWidth.set(newW);
    scaledHeight.set(newH);
    scaleRatioX.set(scaledWidth.getValue() / settings.width);
    scaleRatioY.set(scaledHeight.getValue() / settings.height);

    log.debug("Target settings size: " + (double) settings.width + " x " + (double) settings.height);
    log.debug("Scaled scene size:    " + newW + " x " + newH);
    log.debug("Scaled ratio: (" + scaleRatioX.getValue() + ", " + scaleRatioY.getValue() + ")");
  }

  @Override
  ReadOnlyBooleanProperty iconifiedProperty() {
    return new ReadOnlyBooleanWrapper().getReadOnlyProperty();
  }

  @Override
  void addIcons(Image... images) {
  }

  @Override
  void addCSS(CSS... cssList) {
    var cssArrays = Arrays.stream(cssList).map(CSS::getExternalForm).toList();
    fxglPane.getStylesheets().addAll(cssArrays);
  }

  @Override
  protected void setRoot(Pane root) {
    fxglPane.getAllChildren().setAll(
      backgroundRect,
      root
    );
  }

  @Override
  void show() {
    log.debug("Opening embedded window");

    // platform offsets
    var windowBorderWidth = 0;
    var windowBorderHeight = 0;

    scaledWidth.bind(fxglPane.renderWidthProperty());
    scaledHeight.bind(fxglPane.renderHeightProperty());

    settings.scaledWidthProp.bind(scaledWidth);
    settings.scaledHeightProp.bind(scaledHeight);

    if (settings.isScaleAffectedOnResize) {
      if (settings.isPreserveResizeRatio) {
        scaleRatioX.bind(Bindings.min(
          scaledWidth.divide(settings.width), scaledHeight.divide(settings.height)
        ));
        scaleRatioY.bind(scaleRatioX);
      } else {
        scaleRatioX.bind(scaledWidth.divide(settings.width));
        scaleRatioY.bind(scaledHeight.divide(settings.height));
      }
    } else {
      scaleRatioX.setValue(1.0);
      scaleRatioY.setValue(1.0);

      scaledWidth.addListener((observableValue, old, newWidth) -> onStageResize());
      scaledHeight.addListener((observableValue, old, newHeight) -> onStageResize());
    }

    log.debug("Window border size: (" + windowBorderWidth + ", " + windowBorderHeight + ")");
    log.debug("Scaled size: " + scaledWidth.getValue() + " x " + scaledHeight.getValue());
    log.debug("Scaled ratio: (" + scaleRatioX.getValue() + ", " + scaleRatioY.getValue() + ")");
  }

  /**
   * Called when the user has resized the main window.
   * Only called when settings.isScaleAffectedOnResize = false.
   */
  private void onStageResize() {
    var newW = scaledWidth.getValue();
    var newH = scaledHeight.getValue();

    log.debug("On Stage resize: " + newW + "x" + newH);

    scenes.stream().filter(FXGLScene.class::isInstance)
      .map(FXGLScene.class::cast)
      .forEach(it -> {
        it.getViewport().setWidth(newW);
        it.getViewport().setHeight(newH);
      });
  }

  @Override
  Image takeScreenshot() {
    return new WritableImage(1, 1);
  }

  @Override
  void close() {
  }
}