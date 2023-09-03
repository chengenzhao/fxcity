package com.whitewoodcity.flame.pushnpoppane;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public non-sealed class PushAndPopVBox extends VBox implements PushAndPop {
  private Map<String, Object> parameters;
  protected Stage stage;

  public PushAndPopVBox(Stage stage) {
    this.stage = stage;
    prefWidthProperty().bind(stage.getScene().widthProperty());
    prefHeightProperty().bind(stage.getScene().heightProperty());
  }

  public PushAndPopVBox(Stage stage, Map<String, Object> parameters) {
    this(stage);
    this.parameters = parameters;
  }

  public void disable(){
    this.setDisable(true);
  }

  public void enable(){
    this.setDisable(false);
  }

  public void dispose(){
    prefWidthProperty().unbind();
    prefHeightProperty().unbind();
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
}
