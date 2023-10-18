package com.whitewoodcity.flame;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class SVG extends Region {
  public SVG(){
    minWidthProperty().bind(prefWidthProperty());
    maxWidthProperty().bind(prefWidthProperty());

    minHeightProperty().bind(prefHeightProperty());
    maxHeightProperty().bind(prefHeightProperty());
  }

  public SVG(double width, double height) {
    this();
    setPrefWidth(width);
    setPrefHeight(height);
  }

  public void setWidth(double width) {
    super.setWidth(width);
    if(!prefWidthProperty().isBound())
      prefWidthProperty().set(width);
  }

  public void setHeight(double height) {
    super.setHeight(height);
    if(!prefHeightProperty().isBound())
      prefHeightProperty().set(height);
  }

  public static SVG newSVG(String path, double width, double height, Paint paint) {
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG(width, height);
    svg.setShape(svgPath);
    svg.setFill(paint);
    return svg;
  }

  public static SVG newSVG(String path, double width, double height) {
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG(width, height);
    svg.setShape(svgPath);
    svg.setStyle("-fx-background-color: -color-fg-default;");
    return svg;
  }


  public static SVG newSVG(String path, Paint paint){
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG();
    svg.setShape(svgPath);
    svg.setFill(paint);
    return svg;
  }

  public static SVG newSVG(String path){
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG();
    svg.setShape(svgPath);
    svg.setStyle("-fx-background-color: -color-fg-default;");
    return svg;
  }

  public static SVG newSVGWithProportionalHeight(String path, Paint paint, double widthToHeightRatio){
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG();
    svg.prefHeightProperty().bind(svg.prefWidthProperty().map(w -> w.doubleValue() * widthToHeightRatio));
    svg.setShape(svgPath);
    svg.setFill(paint);
    return svg;
  }

  public static SVG newSVGWithProportionalWidth(String path, Paint paint){
    return newSVGWithProportionalWidth(path, paint, 1);
  }

  public static SVG newSVGWithProportionalWidth(String path, Paint paint, double widthToHeightRatio){
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG();
    svg.prefWidthProperty().bind(svg.prefHeightProperty().map(h -> h.doubleValue() * widthToHeightRatio));
    svg.setShape(svgPath);
    svg.setFill(paint);
    return svg;
  }

  public static SVG newSVGWithProportionalWidth(String path){
    return newSVGWithProportionalWidth(path, 1);
  }

  public static SVG newSVGWithProportionalWidth(String path, double widthToHeightRatio){
    var svgPath = new SVGPath();
    svgPath.setContent(path);
    final SVG svg = new SVG();
    svg.prefWidthProperty().bind(svg.prefHeightProperty().map(h -> h.doubleValue() * widthToHeightRatio));
    svg.setShape(svgPath);
    svg.setStyle("-fx-background-color: -color-fg-default;");
    return svg;
  }

  public void setFill(Paint paint){
    if(paint instanceof Color color){
      String hex = String.format("#%02x%02x%02x", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
      this.setStyle("-fx-background-color: "+hex+";");
    }else
      this.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
  }

  public void setFill(String backgroundColorCss){
    this.setStyle("-fx-background-color: "+backgroundColorCss+";");
  }

  public Paint getFill(){
    return this.getBackground().getFills().get(0).getFill();
  }
}
