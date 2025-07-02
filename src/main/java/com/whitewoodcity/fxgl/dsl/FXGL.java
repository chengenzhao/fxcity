package com.whitewoodcity.fxgl.dsl;

import com.almasb.fxgl.app.ReadOnlyGameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.services.FXGLAssetLoaderService;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.profile.SaveLoadService;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.Timer;
import com.whitewoodcity.fxgl.app.GameApplication;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;


public class FXGL {
  public static GameScene getGameScene(){
    return com.almasb.fxgl.dsl.FXGL.getGameScene();
  }

  public static SaveLoadService getSaveLoadService(){
    return com.almasb.fxgl.dsl.FXGL.getSaveLoadService();
  }

  public static Entity spawnWithScale(GameWorld gameWorld, String entityName , SpawnData data, Duration duration, Interpolator interpolator) {
    var e = gameWorld.create(entityName, data);
    return spawnWithScale(gameWorld, e, duration, interpolator);
  }

  public static Entity spawnWithScale(GameWorld gameWorld, Entity e){
    return spawnWithScale(gameWorld, e, Duration.seconds(1.0), Interpolator.LINEAR);
  }

  public static Entity spawnWithScale(GameWorld gameWorld, Entity e, Duration duration, Interpolator interpolator) {

    e.getTransformComponent().setScaleOrigin(new Point2D(e.getWidth() / 2, e.getHeight() / 2));;

    com.almasb.fxgl.dsl.FXGL.animationBuilder()
      .duration(duration)
      .interpolator(interpolator)
      .scale(e)
      .from(new Point2D(0.0, 0.0))
      .to(new Point2D(1.0, 1.0))
      .buildAndPlay();

    gameWorld.addEntity(e);

    return e;
  }

  public static EntityBuilder entityBuilder(){
    return new EntityBuilder();
  }

  public static Input getInput(){
    return com.almasb.fxgl.dsl.FXGL.getInput();
  }

  public static XInput getXInput(){
    return FXGL.getAppCast().getInput();
  }

  public static PhysicsWorld getPhysicsWorld(){
    return com.almasb.fxgl.dsl.FXGL.getPhysicsWorld();
  }

  public static void despawnWithScale(Entity e){
    despawnWithScale(com.almasb.fxgl.dsl.FXGL.getGameWorld(), e);
  }

  public static void despawnWithScale(GameWorld gameWorld, Entity e){
    despawnWithScale(gameWorld, e, Duration.seconds(1.0), Interpolator.LINEAR);
  }

  public static void despawnWithScale(Entity e, Duration duration, Interpolator interpolator) {
    despawnWithScale(com.almasb.fxgl.dsl.FXGL.getGameWorld(), e, duration, interpolator);
  }

  public static void despawnWithScale(GameWorld gameWorld, Entity e, Duration duration, Interpolator interpolator) {
    com.almasb.fxgl.dsl.FXGL.animationBuilder()
      .duration(duration)
      .interpolator(interpolator)
      .onFinished(()-> gameWorld.removeEntity(e))
      .scale(e)
      .from(new Point2D(1.0, 1.0))
      .to(new Point2D(0.0, 0.0))
      .buildAndPlay();
  }

  public static GameWorld getGameWorld(){
    return com.almasb.fxgl.dsl.FXGL.getGameWorld();
  }

  public static Image image(String assetName){
    return com.almasb.fxgl.dsl.FXGL.image(assetName);
  }

  public static Image image(String assetName, double width, double height){
    return com.almasb.fxgl.dsl.FXGL.image(assetName,width,height);
  }

  public static FXGLAssetLoaderService getAssetLoader(){
    return com.almasb.fxgl.dsl.FXGL.getAssetLoader();
  }

  public static <T> T geto(String varName){
    return com.almasb.fxgl.dsl.FXGL.geto(varName);
  }

  public static Music loopBGM(String assetName){
    return com.almasb.fxgl.dsl.FXGL.loopBGM(assetName);
  }

  public static MediaPlayer loopMusicFromExternalMusicDir(String fileName){
    return loopMusicFromExternalMusicDir(fileName,1);
  }

  /**
   * This method reads bgm from music directory which locates in the root dir of the program
   * The root directory could be found in File("").getAbsolutePath();
   * @param fileName music fileName
   * @param initialVolume initial volume of the media player
   * @return mediaPlayer object
   */
  public static MediaPlayer loopMusicFromExternalMusicDir(String fileName, double initialVolume){
    var player = loadMusicFromExternalMusicDir(fileName, initialVolume);
    if(player == null) return null;
    player.setCycleCount(Timeline.INDEFINITE);
    player.play();
    return player;
  }

  public static PropertyMap getWorldProperties(){
    return com.almasb.fxgl.dsl.FXGL.getWorldProperties();
  }

  public static Point2D getAppCenter(){
    return com.almasb.fxgl.dsl.FXGL.getAppCenter();
  }

  public static ReadOnlyGameSettings getSettings(){
    return com.almasb.fxgl.dsl.FXGL.getSettings();
  }

  public static Texture texture(String assetName){
    return com.almasb.fxgl.dsl.FXGL.texture(assetName);
  }

  public static Texture texture(String assetName, double width, double height){
    return com.almasb.fxgl.dsl.FXGL.texture(assetName,width,height);
  }

  public static double random() {
    return com.almasb.fxgl.dsl.FXGL.random();
  }

  public static double random(int min, int max) {
    return FXGLMath.random(min, max);
  }

  public static double random(double min, double max) {
    return FXGLMath.random(min, max);
  }

  public static int getAppWidth(){
    return com.almasb.fxgl.dsl.FXGL.getAppWidth();
  }

  public static int getAppHeight(){
    return com.almasb.fxgl.dsl.FXGL.getAppHeight();
  }

  public static <T extends GameApplication> T getAppCast(){
    return com.almasb.fxgl.dsl.FXGL.getAppCast();
  }

  public static void set(String varName, Object value) {
    com.almasb.fxgl.dsl.FXGL.getWorldProperties().setValue(varName, value);
  }

  public static Timer getGameTimer() {
    return com.almasb.fxgl.dsl.FXGL.getGameTimer();
  }

  public static void runOnce(Runnable action, Duration delay) {
    getGameTimer().runOnceAfter(action, delay);
  }

  public static <T> void spawn(T type, double x, double y) {
    getAppCast().spawn(type, x, y);
  }

  public static <T> void spawn(T type, SpawnData data) {
    getAppCast().spawn(type, data);
  }

  public static <T> void spawn(T type, SpawnData data, GameWorld gameWorld) {
    getAppCast().spawn(type, data, gameWorld);
  }

  public static void play(String assetName){
    com.almasb.fxgl.dsl.FXGL.play(assetName);
  }

  public static MediaPlayer playMusicFromExternalMusicDir(String fileName, double initialVolume){
    var player = loadMusicFromExternalMusicDir(fileName, initialVolume);
    if(player == null) return null;
    player.setCycleCount(1);
    player.play();
    return player;
  }

  public static MediaPlayer loadMusicFromExternalMusicDir(String fileName, double volume){
    try {
      var path = getExternalMusic(fileName);
      Media media = new Media(new File(path).toURI().toURL().toExternalForm());
      var player = new MediaPlayer(media);
      player.setVolume(volume);
      return player;
    }catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

  private static String getExternalMusic(String musicFileName){
    System.out.println(new File("").getAbsolutePath() + File.separator + "music" + File.separator + musicFileName);
    return new File("").getAbsolutePath() + File.separator + "music" + File.separator + musicFileName;
  }

  public static List<String> text(String assetName){
    return com.almasb.fxgl.dsl.FXGL.text(assetName);
  }

  public static AudioPlayer getAudioPlayer(){
    return com.almasb.fxgl.dsl.FXGL.getAudioPlayer();
  }
}
