package com.whitewoodcity.fxgl.dsl;

import module javafx.controls;
import module javafx.media;

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

}
