package com.whitewoodcity.fxgl.service.component;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class AsyncLabel extends Label {
  public AsyncLabel(String s) {
    super(s);
  }

  public void syncText(String text){
    Platform.runLater(()-> this.setText(text));
  }
}
