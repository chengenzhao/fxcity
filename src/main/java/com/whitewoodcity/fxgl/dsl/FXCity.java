package com.whitewoodcity.fxgl.dsl;

import module javafx.controls;
import module javafx.media;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.GameSubScene;
import com.almasb.fxgl.entity.GameWorld;

import java.util.Optional;

public class FXCity extends FXGL{
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

  public static Optional<GameWorld> getCurrentGameWorld(){
    var scene = FXGL.getSceneService().getCurrentScene();
    if(scene instanceof GameScene gameScene){
      return Optional.of(gameScene.getGameWorld());
    }else if(scene instanceof GameSubScene gameScene){
      return Optional.of(gameScene.getGameWorld());
    }else return Optional.empty();
  }
}
