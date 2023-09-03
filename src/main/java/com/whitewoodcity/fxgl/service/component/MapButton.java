package com.whitewoodcity.fxgl.service.component;

import com.whitewoodcity.flame.SVG;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapButton extends StackPane {
  private final Rectangle background = new Rectangle();

  private Runnable runnable;

  public MapButton(SVG svg){
    background.setFill(Color.web("f2fcf3cc"));

    background.widthProperty().bind(this.widthProperty().map(w -> w.doubleValue() * 0.95));
    background.heightProperty().bind(this.heightProperty().map(h -> h.doubleValue() * 0.95));
    background.arcHeightProperty().bind(background.heightProperty().map(h -> h.doubleValue() / 5));
    background.arcWidthProperty().bind(background.arcHeightProperty());

    this.setOnMouseClicked(_1 -> action());
    this.setOnMousePressed(_1 -> press());
    this.setOnMouseReleased(_1 -> release());

    svg.prefHeightProperty().bind(background.heightProperty().multiply(0.6));
    svg.setMouseTransparent(true);

    this.setOnMouseEntered(_1 -> background.setStroke(Color.web("62b768")));
    this.setOnMouseExited(_1 -> {
      background.setFill(Color.web("f2fcf3cc"));
      background.setStroke(null);
    });

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(background, svg);
  }

  public MapButton(SVG svg, Runnable runnable) {
    this(svg);
    this.runnable = runnable;
  }

  public void press(){
    background.setFill(Color.web("e9f5e6cc"));
    background.setStroke(Color.web("62b768"));
  }

  public void release(){
    background.setFill(Color.web("f2fcf3cc"));
    background.setStroke(null);
  }

  public void action(){
    if(isDisabled()){
      return;
    }
    runnable.run();
  }

  public void setOnAction(Runnable runnable){
    this.runnable = runnable;
  }

  public void releaseAndAction(){
    release();
    action();
  }

  public void disable(){
    this.getChildren().forEach(node -> {
      node.setOpacity(0.6);
      node.setDisable(true);
    });
  }

  public void enable(){
    this.getChildren().forEach(node -> {
      node.setOpacity(1);
      node.setDisable(false);
    });
  }
}