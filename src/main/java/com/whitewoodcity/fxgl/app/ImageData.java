package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;

public record ImageData(String assetName, double width, double height) {
  public Image image(){
    return FXGL.image(assetName, width, height);
  }
}
