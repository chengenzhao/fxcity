package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.achievement.AchievementService;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.FXGLApplication;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.services.FXGLAssetLoaderService;
import com.almasb.fxgl.app.services.FXGLDialogService;
import com.almasb.fxgl.app.services.IOTaskExecutorService;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.core.util.Platform;
import com.almasb.fxgl.cutscene.CutsceneService;
import com.almasb.fxgl.dev.DevService;
import com.almasb.fxgl.io.FileSystemService;
import com.almasb.fxgl.localization.Language;
import com.almasb.fxgl.localization.LocalizationService;
import com.almasb.fxgl.minigames.MiniGameService;
import com.almasb.fxgl.net.NetService;
import com.almasb.fxgl.notification.view.NotificationView;
import com.almasb.fxgl.notification.view.XboxNotificationView;
import com.almasb.fxgl.physics.CollisionDetectionStrategy;
import com.almasb.fxgl.profile.SaveLoadService;
import com.almasb.fxgl.ui.FXGLDialogFactoryServiceProvider;
import com.almasb.fxgl.ui.FXGLUIFactoryServiceProvider;
import javafx.scene.input.KeyCode;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import static com.almasb.fxgl.localization.Language.*;

public class GameSettings {
  RuntimeInfo runtimeInfo = new RuntimeInfo(Platform.WINDOWS, "11.x", "?");

  /**
   * Set title of the game. This will be shown as the
   * window header if the game isn't fullscreen.
   */
  String title = "Untitled";

  String version = "0.0";

  /**
   * Set target width. If the screen width is smaller,
   * the game will automatically scale down the image
   * while maintaining the aspect ratio.
   * <p>
   * All the game logic must use target width and height.
   */
  int width = 800;

  /**
   * Set target height. If the screen height is smaller,
   * the game will automatically scale down the image
   * while maintaining the aspect ratio.
   * <p>
   * All the game logic must use target width and height.
   */
  int height = 600;

  /**
   * Setting to true will allow the game to be able to enter full screen
   * from the menu or programmatically.
   */
  boolean isFullScreenAllowed = false;

  /**
   * Setting to true will start the game in fullscreen, provided
   * [isFullScreenAllowed] is also true.
   */
  boolean isFullScreenFromStart = false;

  /**
   * If enabled, users can drag the corner of the main window
   * to resize it and the game.
   */
  boolean isManualResizeEnabled = false;

  /**
   * If enabled, during resize black bars will be added to preserve the ratio.
   */
  boolean isPreserveResizeRatio = false;

  /**
   * If true, during resize the game will auto-scale to maintain consistency across all displays.
   * If false, during resize, only the window will change size, which allows different displays
   * to have different views.
   * For example, editor type apps may wish to set this to false to maximize "usable" space.
   */
  boolean isScaleAffectedOnResize = true;

  /**
   * If set to true, the intro video/animation will
   * be played before the start of the game.
   */
  boolean isIntroEnabled = false;

  /**
   * Setting to true enables the main menu.
   */
  boolean isMainMenuEnabled = false;

  /**
   * Setting to true enables the game menu.
   */
  boolean isGameMenuEnabled = true;

  boolean isUserProfileEnabled = false;

  /**
   * Setting to true will enable profiler that reports on performance
   * when FXGL exits.
   * Also shows render and performance FPS in the bottom left corner
   * when the application is run.
   */
  boolean isProfilingEnabled = false;

  boolean isDeveloperMenuEnabled = false;

  boolean isClickFeedbackEnabled = false;

  /**
   * If true, entity builder will preload entities on a background thread to speed up
   * entity building.
   * Default: true.
   */
  boolean isEntityPreloadEnabled = true;

  /**
   * If true, allows FXGL to make write calls to the file system, for example
   * to create log files.
   * In cases where running from a directory that requires elevated privileges,
   * it is recommended to disable this setting.
   */
  boolean isFileSystemWriteAllowed = true;

  /**
   * Setting to false will disable asking for confirmation on exit.
   * This is useful for faster compile -> run -> exit.
   */
  boolean isCloseConfirmation = false;

  boolean isSingleStep = false;

  boolean isPauseMusicWhenMinimized = true;

  /**
   * Sets application run mode. See [ApplicationMode] for more info.
   */
  ApplicationMode applicationMode = ApplicationMode.DEVELOPER;

  /**
   * Set the key that will trigger in-game menu.
   */
  KeyCode menuKey = KeyCode.ESCAPE;

  /**
   * Set additional credits.
   */
  List<String> credits = new ArrayList<>();

  EnumSet<MenuItem> enabledMenuItems = EnumSet.noneOf(MenuItem.class);
  StageStyle stageStyle = StageStyle.DECORATED;
  String appIcon = "fxgl_icon.png";

  /**
   * Add extra css from /assets/ui/css/.
   */
  List<String> cssList = new ArrayList<>(List.of("fxgl_dark.css"));

  /**
   * Set font to be used in UI controls.
   * The font will be loaded from "/assets/ui/fonts".
   */
  String fontUI = "VarelaRound-Regular.ttf";
  String fontMono = "TerminalLandMono-Regular.otf";
  String fontText = "Courier-Prime.ttf";
  String fontGame = "Abel-Regular.ttf";

  String soundNotification = "core/notification.wav";
  String soundMenuBack = "menu/back.wav";
  String soundMenuPress = "menu/press.wav";
  String soundMenuSelect = "menu/select.wav";

  double pixelsPerMeter = 50.0;

  CollisionDetectionStrategy collisionDetectionStrategy = CollisionDetectionStrategy.BRUTE_FORCE;

  /**
   * Set how many real seconds are in 24 game hours, default = 60.
   */
  int secondsIn24h = 60;

  /**
   * Seed used to initialize the random number generator in FXGLMath.
   * Default value is -1, which means do not use the seed.
   * Any other value is supplied directly to FXGLMath random.
   */
  long randomSeed = -1L;

  /**
   * Number of ticks per second computed by the engine.
   * This value can be the same as or less than the display refresh rate.
   * Default value is -1, which means "match display refresh rate".
   */
  int ticksPerSecond = -1;

  /**
   * How fast the 3D mouse movements are (example, rotating the camera).
   */
  double mouseSensitivity = 0.2;

  Language defaultLanguage = ENGLISH;

  CursorInfo defaultCursor = new CursorInfo("fxgl_default_cursor.png", 7.0, 6.0);

  boolean isNative = false;

  /**
   * Set this to true if this is a 3D game.
   */
  boolean is3D = false;

  /* EXPERIMENTAL */

  boolean isExperimentalTiledLargeMap = false;

  /* CONFIGS */

  Class configClass = null;

  /* CUSTOMIZABLE SERVICES BELOW */
  List<Class<? extends EngineService>> engineServices = new ArrayList<>(List.of(
    // this is the order in which services will be initialized
    // by design, the order of services should not matter,
    // however some services can depend on others, so no-dep ones should come first
    FXGLAssetLoaderService.class,
    FXGLApplication.GameApplicationService.class,
    FXGLDialogService.class,
    IOTaskExecutorService.class,
    FileSystemService.class,
    LocalizationService.class,
//    SystemBundleService.class,
    SaveLoadService.class,
    FXGLUIFactoryServiceProvider.class,
    FXGLDialogFactoryServiceProvider.class,
    AudioPlayer.class,
//    NotificationServiceProvider.class,
    AchievementService.class,
    CutsceneService.class,
    MiniGameService.class,
    NetService.class,
//    UpdaterService.class,
    DevService.class
  ));

  /**
   * Provide a custom scene factory.
   */
  SceneFactory sceneFactory = new SceneFactory();

  Class<? extends NotificationView> notificationViewClass = XboxNotificationView.class;

  List<Achievement> achievements = new ArrayList<>();

  List<Language> supportedLanguages = new ArrayList<>(List.of(ENGLISH, FRENCH, GERMAN, RUSSIAN, HUNGARIAN));

  public RuntimeInfo getRuntimeInfo() {
    return runtimeInfo;
  }

  public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
    this.runtimeInfo = runtimeInfo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public boolean isFullScreenAllowed() {
    return isFullScreenAllowed;
  }

  public void setFullScreenAllowed(boolean fullScreenAllowed) {
    isFullScreenAllowed = fullScreenAllowed;
  }

  public boolean isFullScreenFromStart() {
    return isFullScreenFromStart;
  }

  public void setFullScreenFromStart(boolean fullScreenFromStart) {
    isFullScreenFromStart = fullScreenFromStart;
  }

  public boolean isManualResizeEnabled() {
    return isManualResizeEnabled;
  }

  public void setManualResizeEnabled(boolean manualResizeEnabled) {
    isManualResizeEnabled = manualResizeEnabled;
  }

  public boolean isPreserveResizeRatio() {
    return isPreserveResizeRatio;
  }

  public void setPreserveResizeRatio(boolean preserveResizeRatio) {
    isPreserveResizeRatio = preserveResizeRatio;
  }

  public boolean isScaleAffectedOnResize() {
    return isScaleAffectedOnResize;
  }

  public void setScaleAffectedOnResize(boolean scaleAffectedOnResize) {
    isScaleAffectedOnResize = scaleAffectedOnResize;
  }

  public boolean isIntroEnabled() {
    return isIntroEnabled;
  }

  public void setIntroEnabled(boolean introEnabled) {
    isIntroEnabled = introEnabled;
  }

  public boolean isMainMenuEnabled() {
    return isMainMenuEnabled;
  }

  public void setMainMenuEnabled(boolean mainMenuEnabled) {
    isMainMenuEnabled = mainMenuEnabled;
  }

  public boolean isGameMenuEnabled() {
    return isGameMenuEnabled;
  }

  public void setGameMenuEnabled(boolean gameMenuEnabled) {
    isGameMenuEnabled = gameMenuEnabled;
  }

  public boolean isUserProfileEnabled() {
    return isUserProfileEnabled;
  }

  public void setUserProfileEnabled(boolean userProfileEnabled) {
    isUserProfileEnabled = userProfileEnabled;
  }

  public boolean isProfilingEnabled() {
    return isProfilingEnabled;
  }

  public void setProfilingEnabled(boolean profilingEnabled) {
    isProfilingEnabled = profilingEnabled;
  }

  public boolean isDeveloperMenuEnabled() {
    return isDeveloperMenuEnabled;
  }

  public void setDeveloperMenuEnabled(boolean developerMenuEnabled) {
    isDeveloperMenuEnabled = developerMenuEnabled;
  }

  public boolean isClickFeedbackEnabled() {
    return isClickFeedbackEnabled;
  }

  public void setClickFeedbackEnabled(boolean clickFeedbackEnabled) {
    isClickFeedbackEnabled = clickFeedbackEnabled;
  }

  public boolean isEntityPreloadEnabled() {
    return isEntityPreloadEnabled;
  }

  public void setEntityPreloadEnabled(boolean entityPreloadEnabled) {
    isEntityPreloadEnabled = entityPreloadEnabled;
  }

  public boolean isFileSystemWriteAllowed() {
    return isFileSystemWriteAllowed;
  }

  public void setFileSystemWriteAllowed(boolean fileSystemWriteAllowed) {
    isFileSystemWriteAllowed = fileSystemWriteAllowed;
  }

  public boolean isCloseConfirmation() {
    return isCloseConfirmation;
  }

  public void setCloseConfirmation(boolean closeConfirmation) {
    isCloseConfirmation = closeConfirmation;
  }

  public boolean isSingleStep() {
    return isSingleStep;
  }

  public void setSingleStep(boolean singleStep) {
    isSingleStep = singleStep;
  }

  public boolean isPauseMusicWhenMinimized() {
    return isPauseMusicWhenMinimized;
  }

  public void setPauseMusicWhenMinimized(boolean pauseMusicWhenMinimized) {
    isPauseMusicWhenMinimized = pauseMusicWhenMinimized;
  }

  public ApplicationMode getApplicationMode() {
    return applicationMode;
  }

  public void setApplicationMode(ApplicationMode applicationMode) {
    this.applicationMode = applicationMode;
  }

  public KeyCode getMenuKey() {
    return menuKey;
  }

  public void setMenuKey(KeyCode menuKey) {
    this.menuKey = menuKey;
  }

  public List<String> getCredits() {
    return credits;
  }

  public void setCredits(List<String> credits) {
    this.credits = credits;
  }

  public EnumSet<MenuItem> getEnabledMenuItems() {
    return enabledMenuItems;
  }

  public void setEnabledMenuItems(EnumSet<MenuItem> enabledMenuItems) {
    this.enabledMenuItems = enabledMenuItems;
  }

  public StageStyle getStageStyle() {
    return stageStyle;
  }

  public void setStageStyle(StageStyle stageStyle) {
    this.stageStyle = stageStyle;
  }

  public String getAppIcon() {
    return appIcon;
  }

  public void setAppIcon(String appIcon) {
    this.appIcon = appIcon;
  }

  public List<String> getCssList() {
    return cssList;
  }

  public void setCssList(List<String> cssList) {
    this.cssList = cssList;
  }

  public String getFontUI() {
    return fontUI;
  }

  public void setFontUI(String fontUI) {
    this.fontUI = fontUI;
  }

  public String getFontMono() {
    return fontMono;
  }

  public void setFontMono(String fontMono) {
    this.fontMono = fontMono;
  }

  public String getFontText() {
    return fontText;
  }

  public void setFontText(String fontText) {
    this.fontText = fontText;
  }

  public String getFontGame() {
    return fontGame;
  }

  public void setFontGame(String fontGame) {
    this.fontGame = fontGame;
  }

  public String getSoundNotification() {
    return soundNotification;
  }

  public void setSoundNotification(String soundNotification) {
    this.soundNotification = soundNotification;
  }

  public String getSoundMenuBack() {
    return soundMenuBack;
  }

  public void setSoundMenuBack(String soundMenuBack) {
    this.soundMenuBack = soundMenuBack;
  }

  public String getSoundMenuPress() {
    return soundMenuPress;
  }

  public void setSoundMenuPress(String soundMenuPress) {
    this.soundMenuPress = soundMenuPress;
  }

  public String getSoundMenuSelect() {
    return soundMenuSelect;
  }

  public void setSoundMenuSelect(String soundMenuSelect) {
    this.soundMenuSelect = soundMenuSelect;
  }

  public double getPixelsPerMeter() {
    return pixelsPerMeter;
  }

  public void setPixelsPerMeter(double pixelsPerMeter) {
    this.pixelsPerMeter = pixelsPerMeter;
  }

  public CollisionDetectionStrategy getCollisionDetectionStrategy() {
    return collisionDetectionStrategy;
  }

  public void setCollisionDetectionStrategy(CollisionDetectionStrategy collisionDetectionStrategy) {
    this.collisionDetectionStrategy = collisionDetectionStrategy;
  }

  public int getSecondsIn24h() {
    return secondsIn24h;
  }

  public void setSecondsIn24h(int secondsIn24h) {
    this.secondsIn24h = secondsIn24h;
  }

  public long getRandomSeed() {
    return randomSeed;
  }

  public void setRandomSeed(long randomSeed) {
    this.randomSeed = randomSeed;
  }

  public int getTicksPerSecond() {
    return ticksPerSecond;
  }

  public void setTicksPerSecond(int ticksPerSecond) {
    this.ticksPerSecond = ticksPerSecond;
  }

  public double getMouseSensitivity() {
    return mouseSensitivity;
  }

  public void setMouseSensitivity(double mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }

  public Language getDefaultLanguage() {
    return defaultLanguage;
  }

  public void setDefaultLanguage(Language defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  public CursorInfo getDefaultCursor() {
    return defaultCursor;
  }

  public void setDefaultCursor(CursorInfo defaultCursor) {
    this.defaultCursor = defaultCursor;
  }

  public boolean isNative() {
    return isNative;
  }

  public void setNative(boolean aNative) {
    isNative = aNative;
  }

  public boolean isIs3D() {
    return is3D;
  }

  public void setIs3D(boolean is3D) {
    this.is3D = is3D;
  }

  public boolean isExperimentalTiledLargeMap() {
    return isExperimentalTiledLargeMap;
  }

  public void setExperimentalTiledLargeMap(boolean experimentalTiledLargeMap) {
    isExperimentalTiledLargeMap = experimentalTiledLargeMap;
  }

  public Class getConfigClass() {
    return configClass;
  }

  public void setConfigClass(Class configClass) {
    this.configClass = configClass;
  }

  public List<Class<? extends EngineService>> getEngineServices() {
    return engineServices;
  }

  public void setEngineServices(List<Class<? extends EngineService>> engineServices) {
    this.engineServices = engineServices;
  }

  public SceneFactory getSceneFactory() {
    return sceneFactory;
  }

  public void setSceneFactory(SceneFactory sceneFactory) {
    this.sceneFactory = sceneFactory;
  }

  public Class getNotificationViewClass() {
    return notificationViewClass;
  }

  public void setNotificationViewClass(Class notificationViewClass) {
    this.notificationViewClass = notificationViewClass;
  }

  public List<Achievement> getAchievements() {
    return achievements;
  }

  public void setAchievements(List<Achievement> achievements) {
    this.achievements = achievements;
  }

  public List<Language> getSupportedLanguages() {
    return supportedLanguages;
  }

  public void setSupportedLanguages(List<Language> supportedLanguages) {
    this.supportedLanguages = supportedLanguages;
  }


  void addEngineService(Class<? extends EngineService> service ) {
    engineServices.add(service);
  }

  boolean removeEngineService(Class<? extends EngineService> service ) {
    return engineServices.remove(service);
  }

  void setEngineServiceProvider(Class<? extends EngineService> oldService , Class<? extends EngineService> newService) {
    engineServices.removeIf(oldService::isAssignableFrom);
    addEngineService(newService);
  }

  void setHeightFromRatio(double ratio) {
    height = (int)Math.round(width / ratio);
  }

  void setWidthFromRatio(double ratio) {
    width = (int)Math.round(height * ratio);
  }

  public ReadOnlyGameSettings toReadOnly(){
    return toReadOnly(GameApplication.class);
  }

  public ReadOnlyGameSettings toReadOnly(Class userAppClass)  {
    return new ReadOnlyGameSettings(
      runtimeInfo,
      title,
      version,
      width,
      height,
      isFullScreenAllowed,
      isFullScreenFromStart,
      isManualResizeEnabled,
      isPreserveResizeRatio,
      isScaleAffectedOnResize,
      isIntroEnabled,
      isMainMenuEnabled,
      isGameMenuEnabled,
      isUserProfileEnabled,
      isProfilingEnabled,
      isDeveloperMenuEnabled,
      isClickFeedbackEnabled,
      isEntityPreloadEnabled,
      isFileSystemWriteAllowed,
      isCloseConfirmation,
      isSingleStep,
      isPauseMusicWhenMinimized,
      applicationMode,
      menuKey,
      List.copyOf(credits),
      enabledMenuItems,
      stageStyle,
      appIcon,
      List.copyOf(cssList),
      fontUI,
      fontMono,
      fontText,
      fontGame,
      soundNotification,
      soundMenuBack,
      soundMenuPress,
      soundMenuSelect,
      pixelsPerMeter,
      collisionDetectionStrategy,
      secondsIn24h,
      randomSeed,
      ticksPerSecond,
      userAppClass,
      mouseSensitivity,
      defaultLanguage,
      defaultCursor,
      isNative,
      is3D,
      isExperimentalTiledLargeMap,
      configClass,
      List.copyOf(engineServices),
      sceneFactory,
      notificationViewClass,
      List.copyOf(achievements),
      List.copyOf(supportedLanguages.stream().sorted(Comparator.comparing(Language::getName)).toList())
    );
  }
}
