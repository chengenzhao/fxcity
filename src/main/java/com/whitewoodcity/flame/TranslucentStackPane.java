package com.whitewoodcity.flame;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class TranslucentStackPane<C extends Node> extends Pane {

  private final StackPane stackPane;
  private C node;

  public TranslucentStackPane(C node){
    this(node, Pos.CENTER, Color.rgb(255, 255, 255, 0.5), Color.rgb(255, 255, 255, 0.3));
  }

  public TranslucentStackPane(C node, Pos alignment){
    this(node, alignment, Color.rgb(255, 255, 255, 0.5), Color.rgb(255, 255, 255, 0.3));
  }

  public TranslucentStackPane(C node, Color backgroundBorderColor, Color backgroundCentralColor){
    this(node, Pos.CENTER, backgroundBorderColor, backgroundCentralColor);
  }

  public TranslucentStackPane(C node, Pos alignment, Color backgroundBorderColor, Color backgroundCentralColor) {
    this(alignment, backgroundBorderColor, backgroundCentralColor);
    setContent(node);
  }

  public TranslucentStackPane(){
    this(Pos.BOTTOM_CENTER ,Color.rgb(255, 255, 255, 0.5), Color.rgb(255, 255, 255, 0.3));
  }

  public TranslucentStackPane(Pos alignment, Color backgroundBorderColor, Color backgroundCentralColor) {
    var bg = new Rectangle();
    bg.widthProperty().bind(this.widthProperty());
    bg.heightProperty().bind(this.heightProperty());
    bg.arcHeightProperty().bind(bg.heightProperty().divide(10));
    bg.arcWidthProperty().bind(bg.arcHeightProperty());

    Stop[] stops = new Stop[]{new Stop(0, backgroundBorderColor), new Stop(0.4, backgroundCentralColor), new Stop(0.6, backgroundCentralColor), new Stop(1, backgroundBorderColor)};
    bg.setFill(new LinearGradient(0, 1, 1, 1, true, CycleMethod.NO_CYCLE, stops));

    var upperBound = new Line();
    stops = new Stop[]{new Stop(0, Color.TRANSPARENT), new Stop(0.2, Color.WHITE), new Stop(0.8, Color.WHITE), new Stop(1, Color.TRANSPARENT)};
    var linearGradient = new LinearGradient(0, 1, 1, 1, true, CycleMethod.NO_CYCLE, stops);
    upperBound.setStroke(linearGradient);
    upperBound.endXProperty().bind(this.widthProperty());
    upperBound.setStrokeWidth(1);

    var lowerBound = new Line();
    lowerBound.setStroke(linearGradient);
    lowerBound.endXProperty().bind(this.widthProperty());
    lowerBound.startYProperty().bind(this.heightProperty());
    lowerBound.endYProperty().bind(lowerBound.startYProperty());
    lowerBound.setStrokeWidth(1);

    this.stackPane = new StackPane();
    stackPane.prefHeightProperty().bind(bg.heightProperty());
    stackPane.prefWidthProperty().bind(bg.widthProperty());
    stackPane.setAlignment(alignment);

    this.getChildren().addAll(bg, upperBound, stackPane, lowerBound);
  }

  public C getContent() {
    return node;
  }

  public TranslucentStackPane<C> setContent(C node){
    this.node = node;
    this.stackPane.getChildren().clear();
    this.stackPane.getChildren().add(node);
    return this;
  }

  public TranslucentStackPane<C> addContent(C node){
    this.node = node;
    this.stackPane.getChildren().add(node);
    return this;
  }

  public TranslucentStackPane<C> removeContent(C node){
    this.stackPane.getChildren().remove(node);
    return this;
  }
}
