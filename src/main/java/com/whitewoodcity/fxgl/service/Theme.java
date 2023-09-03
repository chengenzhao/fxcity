package com.whitewoodcity.fxgl.service;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Theme{
//  DARK(Color.WHITE, Color.web("030534aa"), Color.web("3978edcc"), new Bloom()),
  DARK(Color.WHITE, Color.rgb( 28, 38, 50, 170.0/255), Color.web("3978edcc"), new Bloom()),
  LIGHT(Color.web("62b768"), Color.web("bbedbcaa"), Color.web("f2fcf3ee"), new Glow()),
  //DAWN(Color.web("EF6F71"), Color.web("#EC9190AA"), Color.web("ffffffee"), new Glow()),//Sepia tone
  ;

  public final Color textColor;
  public final Color outerFrameColor;
  public final Paint innerBackgroundFill;
  public final Effect effect;

  Theme(Color textColor, Color outerFrameColor, Paint innerBackgroundFill, Effect effect) {
    this.textColor = textColor;
    this.outerFrameColor = outerFrameColor;
    this.innerBackgroundFill = innerBackgroundFill;
    this.effect = effect;
  }

  public Color getBackgroundColor(Color defaultColor){
    if(innerBackgroundFill instanceof Color color)
      return color;
    else
      return defaultColor;
  }
}