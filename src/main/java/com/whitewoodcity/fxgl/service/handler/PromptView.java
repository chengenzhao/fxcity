package com.whitewoodcity.fxgl.service.handler;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PromptView extends StackPane {

  Rectangle background;
  Rectangle border;
  private Text text;

  public PromptView() {
    this(24.0);
  }

  public PromptView(double size) {
    this("Test", Font.font(size - 2), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String string) {
    this(string, Font.font(22.0), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String string, double size) {
    this(string, Font.font(size), Color.ORANGE, Color.BLACK);
  }

  public PromptView(String text, Font font, Color textColor, Color backgroundColor) {
    var size = font.getSize() + 2;
    this.text = new Text(text);
    this.text.setFill(textColor);
    this.text.setFont(font);

    background = new Rectangle(size * 0.95, size * 1.2, backgroundColor);
    border = new Rectangle(size * 1.01, size * 1.25, null);

    border.setArcWidth(size / 4);
    border.setArcHeight(size / 4);
    border.setStroke(textColor);
    border.setStrokeWidth(size / 11);
    border.strokeProperty().bind(this.text.fillProperty());

    background.setWidth(this.text.getLayoutBounds().getWidth() * 1.25);
    border.setWidth(this.text.getLayoutBounds().getWidth() * 1.26);

    this.getChildren().addAll(background, border, this.text);
  }

  public ObjectProperty<Paint> backgroundColorProperty() {
    return background.fillProperty();
  }

  public ObjectProperty<Paint> textColorProperty() {
    return text.fillProperty();
  }

  public Text getText() {
    return text;
  }

  public void setBackgroundFill(Paint fill) {
    background.setFill(fill);
  }
}