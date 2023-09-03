package com.whitewoodcity.flame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Bgm {
  public MediaPlayer mediaPlayer;

  public void initialize(String bgm){
    try {
      mediaPlayer = new MediaPlayer((Media) Flame.audio.load(bgm));

      mediaPlayer.setCycleCount(Integer.MAX_VALUE);
    }catch (Throwable t){
      t.printStackTrace();
      mediaPlayer = null;
    }
  }

  public void play(double volume){
    if(mediaPlayer!=null){
      mediaPlayer.setVolume(volume);
      mediaPlayer.play();
    }
  }

  public void dispose(){
    mediaPlayer.dispose();
    mediaPlayer = null;
  }
}
