package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.serialization.SerializableType;
import com.almasb.fxgl.core.util.Platform;
import com.almasb.fxgl.gameplay.GameDifficulty;
import com.almasb.fxgl.localization.Language;
import com.almasb.fxgl.notification.view.NotificationView;
import com.almasb.fxgl.physics.CollisionDetectionStrategy;
import javafx.beans.property.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ReadOnlyGameSettings implements SerializableType {
  final RuntimeInfo runtimeInfo;

  /**
   * Set title of the game. This will be shown as the
   * window header if the game isn't fullscreen.
   */
  final String title;

  final String version;

  /**
   * Set target width. If the screen width is smaller,
   * the game will automatically scale down the image
   * while maintaining the aspect ratio.
   *
   * All the game logic must use target width and height.
   */
  final int width;

  /**
   * Set target height. If the screen height is smaller,
   * the game will automatically scale down the image
   * while maintaining the aspect ratio.
   *
   * All the game logic must use target width and height.
   */
  final int height;

  /**
   * Setting to true will allow the game to be able to enter full screen
   * from the menu or programmatically.
   */
  final boolean isFullScreenAllowed;

  /**
   * Setting to true will start the game in fullscreen, provided
   * [isFullScreenAllowed] is also true.
   */
  final boolean isFullScreenFromStart;

  /**
   * If enabled, users can drag the corner of the main window
   * to resize it and the game.
   */
  final boolean isManualResizeEnabled;

  final boolean isPreserveResizeRatio;

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
  final boolean isIntroEnabled;

  /**
   * Setting to true enables the main menu.
   */
  final boolean isMainMenuEnabled;

  /**
   * Setting to true enables the game menu.
   */
  final boolean isGameMenuEnabled;

  final boolean isUserProfileEnabled;

  /**
   * Setting to true will enable profiler that reports on performance
   * when FXGL exits.
   * Also shows render and performance FPS in the bottom left corner
   * when the application is run.
   */
  final boolean isProfilingEnabled;

  final boolean isDeveloperMenuEnabled;

  final boolean isClickFeedbackEnabled;

  /**
   * If true, entity builder will preload entities on a background thread to speed up
   * entity building.
   * Default: true.
   */
  final boolean isEntityPreloadEnabled;

  /**
   * If true, allows FXGL to make write calls to the file system, for example
   * to create log files.
   * In cases where running from a directory that requires elevated privileges,
   * it is recommended to disable this setting.
   */
  final boolean isFileSystemWriteAllowed;

  /**
   * Setting to false will disable asking for confirmation on exit.
   * This is useful for faster compile -> run -> exit.
   */
  final boolean isCloseConfirmation;

  final boolean isSingleStep;

  final boolean isPauseMusicWhenMinimized;

  /**
   * Sets application run mode. See [ApplicationMode] for more info.
   */
  final ApplicationMode applicationMode;

  /**
   * Set the key that will trigger in-game menu.
   */
  final KeyCode menuKey;

  /**
   * Set additional credits.
   */
  final  List<String> credits;
  final EnumSet<MenuItem> enabledMenuItems;
  final StageStyle stageStyle;
  final String appIcon;

  final List<String> cssList;
  public List<String> getCSSList(){
    return cssList;
  }

  /**
   * Set font to be used in UI controls.
   * The font will be loaded from "/assets/ui/fonts".
   */
  final String fontUI;
  final String fontMono;
  final String fontText;
  final String fontGame;

  final String soundNotification;
  final String soundMenuBack;
  final String soundMenuPress;
  final String soundMenuSelect;

  final double pixelsPerMeter;

  final CollisionDetectionStrategy collisionDetectionStrategy;

  /**
   * Set how many real seconds are in 24 game hours, default = 60.
   */
  final int secondsIn24h;

  final long randomSeed;

  final int ticksPerSecond;

  final Class userAppClass;

  /**
   * How fast the 3D mouse movements are (example, rotating the camera).
   */
  final double mouseSensitivity;

  final private Language defaultLanguage;

  final CursorInfo defaultCursor;

  /**
   * Are running on mobile, or natively (AOT-compiled) on desktop.
   */
  final boolean isNative;

  final boolean is3D;

  /* EXPERIMENTAL */

  final boolean isExperimentalTiledLargeMap;

  /* CONFIGS */

  final private Class configClassInternal;

  /* CUSTOMIZABLE SERVICES BELOW */

  final List<Class<? extends EngineService>> engineServices;

  /**
   * Provide a custom scene factory.
   */
  final SceneFactory sceneFactory;

  final Class<? extends NotificationView> notificationViewClass;

  final List<Achievement> achievements;

  final List<Language> supportedLanguages;

  public ReadOnlyGameSettings(RuntimeInfo runtimeInfo, String title, String version, int width, int height, boolean isFullScreenAllowed, boolean isFullScreenFromStart, boolean isManualResizeEnabled, boolean isPreserveResizeRatio, boolean isScaleAffectedOnResize, boolean isIntroEnabled, boolean isMainMenuEnabled, boolean isGameMenuEnabled, boolean isUserProfileEnabled, boolean isProfilingEnabled, boolean isDeveloperMenuEnabled, boolean isClickFeedbackEnabled, boolean isEntityPreloadEnabled, boolean isFileSystemWriteAllowed, boolean isCloseConfirmation, boolean isSingleStep, boolean isPauseMusicWhenMinimized, ApplicationMode applicationMode, KeyCode menuKey, List<String> credits, EnumSet<MenuItem> enabledMenuItems, StageStyle stageStyle, String appIcon, List<String> cssList, String fontUI, String fontMono, String fontText, String fontGame, String soundNotification, String soundMenuBack, String soundMenuPress, String soundMenuSelect, double pixelsPerMeter, CollisionDetectionStrategy collisionDetectionStrategy, int secondsIn24h, long randomSeed, int ticksPerSecond, Class userAppClass, double mouseSensitivity, Language defaultLanguage, CursorInfo defaultCursor, boolean isNative, boolean is3D, boolean isExperimentalTiledLargeMap, Class configClassInternal, List<Class<? extends EngineService>> engineServices, SceneFactory sceneFactory, Class<? extends NotificationView> notificationViewClass, List<Achievement> achievements, List<Language> supportedLanguages) {
    this.runtimeInfo = runtimeInfo;
    this.title = title;
    this.version = version;
    this.width = width;
    this.height = height;
    this.isFullScreenAllowed = isFullScreenAllowed;
    this.isFullScreenFromStart = isFullScreenFromStart;
    this.isManualResizeEnabled = isManualResizeEnabled;
    this.isPreserveResizeRatio = isPreserveResizeRatio;
    this.isScaleAffectedOnResize = isScaleAffectedOnResize;
    this.isIntroEnabled = isIntroEnabled;
    this.isMainMenuEnabled = isMainMenuEnabled;
    this.isGameMenuEnabled = isGameMenuEnabled;
    this.isUserProfileEnabled = isUserProfileEnabled;
    this.isProfilingEnabled = isProfilingEnabled;
    this.isDeveloperMenuEnabled = isDeveloperMenuEnabled;
    this.isClickFeedbackEnabled = isClickFeedbackEnabled;
    this.isEntityPreloadEnabled = isEntityPreloadEnabled;
    this.isFileSystemWriteAllowed = isFileSystemWriteAllowed;
    this.isCloseConfirmation = isCloseConfirmation;
    this.isSingleStep = isSingleStep;
    this.isPauseMusicWhenMinimized = isPauseMusicWhenMinimized;
    this.applicationMode = applicationMode;
    this.menuKey = menuKey;
    this.credits = credits;
    this.enabledMenuItems = enabledMenuItems;
    this.stageStyle = stageStyle;
    this.appIcon = appIcon;
    this.cssList = cssList;
    this.fontUI = fontUI;
    this.fontMono = fontMono;
    this.fontText = fontText;
    this.fontGame = fontGame;
    this.soundNotification = soundNotification;
    this.soundMenuBack = soundMenuBack;
    this.soundMenuPress = soundMenuPress;
    this.soundMenuSelect = soundMenuSelect;
    this.pixelsPerMeter = pixelsPerMeter;
    this.collisionDetectionStrategy = collisionDetectionStrategy;
    this.secondsIn24h = secondsIn24h;
    this.randomSeed = randomSeed;
    this.ticksPerSecond = ticksPerSecond;
    this.userAppClass = userAppClass;
    this.mouseSensitivity = mouseSensitivity;
    this.defaultLanguage = defaultLanguage;
    this.defaultCursor = defaultCursor;
    this.isNative = isNative;
    this.is3D = is3D;
    this.isExperimentalTiledLargeMap = isExperimentalTiledLargeMap;
    this.configClassInternal = configClassInternal;
    this.engineServices = engineServices;
    this.sceneFactory = sceneFactory;
    this.notificationViewClass = notificationViewClass;
    this.achievements = achievements;
    this.supportedLanguages = supportedLanguages;
  }

  public RuntimeInfo getRuntimeInfo() {
    return runtimeInfo;
  }

  public String getTitle() {
    return title;
  }

  public String getVersion() {
    return version;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isFullScreenAllowed() {
    return isFullScreenAllowed;
  }

  public boolean isFullScreenFromStart() {
    return isFullScreenFromStart;
  }

  public boolean isManualResizeEnabled() {
    return isManualResizeEnabled;
  }

  public boolean isPreserveResizeRatio() {
    return isPreserveResizeRatio;
  }

  public boolean isScaleAffectedOnResize() {
    return isScaleAffectedOnResize;
  }

  public boolean isIntroEnabled() {
    return isIntroEnabled;
  }

  public boolean isMainMenuEnabled() {
    return isMainMenuEnabled;
  }

  public boolean isGameMenuEnabled() {
    return isGameMenuEnabled;
  }

  public boolean isUserProfileEnabled() {
    return isUserProfileEnabled;
  }

  public boolean isProfilingEnabled() {
    return isProfilingEnabled;
  }

  public boolean isDeveloperMenuEnabled() {
    return isDeveloperMenuEnabled;
  }

  public boolean isClickFeedbackEnabled() {
    return isClickFeedbackEnabled;
  }

  public boolean isEntityPreloadEnabled() {
    return isEntityPreloadEnabled;
  }

  public boolean isFileSystemWriteAllowed() {
    return isFileSystemWriteAllowed;
  }

  public boolean isCloseConfirmation() {
    return isCloseConfirmation;
  }

  public boolean isSingleStep() {
    return isSingleStep;
  }

  public boolean isPauseMusicWhenMinimized() {
    return isPauseMusicWhenMinimized;
  }

  public ApplicationMode getApplicationMode() {
    return applicationMode;
  }

  public KeyCode getMenuKey() {
    return menuKey;
  }

  public List<String> getCredits() {
    return credits;
  }

  public EnumSet<MenuItem> getEnabledMenuItems() {
    return enabledMenuItems;
  }

  public StageStyle getStageStyle() {
    return stageStyle;
  }

  public String getAppIcon() {
    return appIcon;
  }

  public List<String> getCssList() {
    return cssList;
  }

  public String getFontUI() {
    return fontUI;
  }

  public String getFontMono() {
    return fontMono;
  }

  public String getFontText() {
    return fontText;
  }

  public String getFontGame() {
    return fontGame;
  }

  public String getSoundNotification() {
    return soundNotification;
  }

  public String getSoundMenuBack() {
    return soundMenuBack;
  }

  public String getSoundMenuPress() {
    return soundMenuPress;
  }

  public String getSoundMenuSelect() {
    return soundMenuSelect;
  }

  public double getPixelsPerMeter() {
    return pixelsPerMeter;
  }

  public CollisionDetectionStrategy getCollisionDetectionStrategy() {
    return collisionDetectionStrategy;
  }

  public int getSecondsIn24h() {
    return secondsIn24h;
  }

  public long getRandomSeed() {
    return randomSeed;
  }

  public int getTicksPerSecond() {
    return ticksPerSecond;
  }

  public Class getUserAppClass() {
    return userAppClass;
  }

  public Language getDefaultLanguage() {
    return defaultLanguage;
  }

  public CursorInfo getDefaultCursor() {
    return defaultCursor;
  }

  public boolean isNative() {
    return isNative;
  }

  public boolean isIs3D() {
    return is3D;
  }

  public boolean isExperimentalTiledLargeMap() {
    return isExperimentalTiledLargeMap;
  }

  public Class getConfigClassInternal() {
    return configClassInternal;
  }

  public List<Class<? extends EngineService>> getEngineServices() {
    return engineServices;
  }

  public SceneFactory getSceneFactory() {
    return sceneFactory;
  }

  public Class<? extends NotificationView> getNotificationViewClass() {
    return notificationViewClass;
  }

  public List<Achievement> getAchievements() {
    return achievements;
  }

  public List<Language> getSupportedLanguages() {
    return supportedLanguages;
  }

  /* STATIC - cannot be modified at runtime */

  /**
   * where to look for latest stable project POM
   */
  final String urlPOM = "https://raw.githubusercontent.com/AlmasB/FXGL/release/README.md";

  /**
   * project GitHub repo
   */
  final String  urlGithub = "https://github.com/AlmasB/FXGL";

  /**
   * link to Heroku leaderboard server
   */
  final String  urlLeaderboard = "http://fxgl-top.herokuapp.com/";

  /**
   * how often to check for updates
   */
  final int versionCheckDays = 7;

  /**
   * profiles are saved in this directory
   */
  final String profileDir = "profiles/";

  final String saveFileExt = "sav";

  Platform platform;
  public Platform getPlatform(){
    return runtimeInfo.platform();
  }

  boolean isDesktop;
  public boolean isDesktop(){
    return platform.isDesktop();
  }

  boolean isMobile;
  public boolean isMobile(){
    return platform.isMobile();
  }

  boolean isEmbedded;
  public boolean isEmbedded(){
    return platform.isEmbedded();
  }

  boolean isBrowser;
  public boolean isBrowser(){
    return platform.isBrowser();
  }

  boolean isWindows;
  public boolean isWindows(){
    return platform == Platform.WINDOWS;
  }

  boolean isMac;
  public boolean isMac(){
    return platform == Platform.MAC;
  }

  boolean isLinux;
  public boolean isLinux(){
    return platform == Platform.LINUX;
  }

  boolean isIOS;
  public boolean isIOS(){
    return platform == Platform.IOS;
  }

  boolean isAndroid;
  public boolean isAndroid(){
    return platform == Platform.ANDROID;
  }

  // DYNAMIC - can be modified at runtime

  final ObjectProperty<Color> devBBoxColor = new SimpleObjectProperty<>(Color.web("#ff0000"));
  public ObjectProperty<Color> devBBoxColorProperty(){
    return devBBoxColor;
  }

  final ObjectProperty<Color> devSensorColor = new SimpleObjectProperty<>(Color.YELLOW);
  public ObjectProperty<Color> devSensorColorProperty(){
    return devSensorColor;
  }

  BooleanProperty devShowBBox = new SimpleBooleanProperty(false);
  public BooleanProperty devShowBBoxProperty(){
    return devShowBBox;
  }

  BooleanProperty devShowPosition = new SimpleBooleanProperty(false);
  public BooleanProperty devShowPositionProperty(){
    return devShowPosition;
  }

  BooleanProperty devEnableDebugCamera = new SimpleBooleanProperty(false);
  public BooleanProperty devEnableDebugCameraProperty(){
    return devEnableDebugCamera;
  }

    /*
    Usage of below:
    1. UI objects should bi-directionally bind to these properties.
    2. Engine services should one-directionally bind to these properties and not expose their own properties.
    3. Any kind of programmatic access should modify these properties, in which case (1) and (2) are auto-updated.

    These are saved by the Settings, so engine services do not need to save their copy of these.
     */

  ObjectProperty<Language> language = new SimpleObjectProperty<>(getDefaultLanguage());

  /**
   * Allows toggling fullscreen on/off from code.
   * [isFullScreenAllowed] must be true, otherwise it's no-op.
   */
  BooleanProperty fullScreen = new SimpleBooleanProperty(isFullScreenFromStart());

  ReadOnlyDoubleWrapper scaledWidthProp = new ReadOnlyDoubleWrapper();
  ReadOnlyDoubleWrapper scaledHeightProp = new ReadOnlyDoubleWrapper();

  /**
   * @return actual width of the scene root
   */
  public ReadOnlyDoubleProperty actualWidthProperty(){
    return scaledWidthProp.getReadOnlyProperty();
  }

  /**
   * @return actual height of the scene root
   */
  public ReadOnlyDoubleProperty actualHeightProperty(){
   return scaledHeightProp.getReadOnlyProperty();
  }

  double actualWidth;
  public double getActualWidth(){
    return scaledWidthProp.getValue();
  }

  double actualHeight;
  public double getActualHeight(){
    return scaledHeightProp.getValue();
  }

  private final ReadOnlyDoubleProperty appWidthProp = new ReadOnlyDoubleWrapper(getWidth()).getReadOnlyProperty();
  private final ReadOnlyDoubleProperty appHeightProp = new ReadOnlyDoubleWrapper(getHeight()).getReadOnlyProperty();

  /**
   * @return a convenience property that auto-sets to target (app) width if auto-scaling is enabled
   * and uses actual javafx scene width if not
   */
  ReadOnlyDoubleProperty prefWidthProperty()  {
    return (isScaleAffectedOnResize) ? appWidthProp : actualWidthProperty();
  }

  /**
   * @return a convenience property that auto-sets to target (app) height if auto-scaling is enabled
   * and uses actual javafx scene height if not
   */
  ReadOnlyDoubleProperty prefHeightProperty() {
    return  (isScaleAffectedOnResize) ? appHeightProp : actualHeightProperty();
  }

  StringProperty profileName = new SimpleStringProperty("DEFAULT");

  private final ObjectProperty<GameDifficulty> gameDifficultyProp = new SimpleObjectProperty<>(GameDifficulty.MEDIUM);

  ObjectProperty<GameDifficulty> gameDifficultyProperty(){
    return gameDifficultyProp;
  }

  GameDifficulty gameDifficulty;
  public GameDifficulty getGameDifficulty(){
    return gameDifficultyProp.getValue();
  }
  public void setGameDifficulty(GameDifficulty value){
    gameDifficultyProp.setValue(value);
  }

  DoubleProperty globalMusicVolumeProperty = new SimpleDoubleProperty(0.5);
  public DoubleProperty globalMusicVolumeProperty(){
    return globalMusicVolumeProperty;
  }

  /**
   * Set global music volume in the range [0..1],
   * where 0 = 0%, 1 = 100%.
   */
  public double getGlobalMusicVolume(){
    return globalMusicVolumeProperty.getValue();
  }
  public void setGlobalMusicVolume(double value){
    globalMusicVolumeProperty.setValue(value);
  }

  DoubleProperty globalSoundVolumeProperty = new SimpleDoubleProperty(0.5);
  public DoubleProperty globalSoundVolumeProperty(){
    return globalSoundVolumeProperty;
  }

  /**
   * Set global sound volume in the range [0..1],
   * where 0 = 0%, 1 = 100%.
   */
  public double getGlobalSoundVolume(){
    return globalSoundVolumeProperty.getValue();
  }
  public void setGlobalSoundVolume(double value){
    globalSoundVolumeProperty.setValue(value);
  }

  private final DoubleProperty mouseSensitivityProp = new SimpleDoubleProperty(getMouseSensitivity());

  public double getMouseSensitivity(){
    return mouseSensitivityProp.getValue();
  }
  public void setMouseSensitivity(double value){
    mouseSensitivityProp.setValue(value);
  }

  // WRAPPERS

  public Optional<Class> getConfigClass(){
    return Optional.ofNullable(configClassInternal);
  }

  {
    applySettings();
  }

  @Override public void write(Bundle bundle) {
    bundle.put("fullscreen", fullScreen.getValue());
    bundle.put("globalMusicVolume", getGlobalMusicVolume());
    bundle.put("globalSoundVolume", getGlobalSoundVolume());
  }

  @Override public void read(Bundle bundle) {
    fullScreen.setValue(bundle.get("fullscreen"));

    setGlobalMusicVolume( bundle.get("globalMusicVolume"));;
    setGlobalSoundVolume( bundle.get("globalSoundVolume"));;

    applySettings();
  }

  private void applySettings() {
    if (randomSeed != -1L)
      FXGLMath.setRandom(new Random(randomSeed));
  }

  @Override public String toString() {
    return "Title: " + title + '\n' +
      "Version: " + version + '\n' +
      "Width: " + width + '\n' +
      "Height: " + height + '\n' +
      "Fullscreen: " + isFullScreenAllowed + '\n' +
      "Intro: " + isIntroEnabled + '\n' +
      "Profiling: " + isProfilingEnabled + '\n' +
      "Single step:" + isSingleStep + '\n' +
      "App Mode: " + applicationMode + '\n' +
      "Menu Key: " + menuKey + '\n' +
      "Stage Style: " + stageStyle + '\n' +
      "Scene Factory: " + sceneFactory.getClass() + '\n';
  }
}
