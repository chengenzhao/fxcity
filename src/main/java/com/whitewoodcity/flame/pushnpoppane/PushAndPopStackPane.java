package com.whitewoodcity.flame.pushnpoppane;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Map;

public non-sealed class PushAndPopStackPane extends StackPane implements PushAndPop {
  public void disable(){
    this.setDisable(true);
  }

  public void enable(){
    this.setDisable(false);
  }

  private Map<String, Object> parameters;
  protected Stage stage;

  public PushAndPopStackPane(Stage stage) {
    this.stage = stage;
    prefWidthProperty().bind(stage.getScene().widthProperty());
    prefHeightProperty().bind(stage.getScene().heightProperty());
  }

  public PushAndPopStackPane(Stage stage, Map<String, Object> parameters) {
    this(stage);
    this.parameters = parameters;
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
