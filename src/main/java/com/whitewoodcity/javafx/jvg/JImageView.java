package com.whitewoodcity.javafx.jvg;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JImageView extends ImageView {
  private Image defaultImage;
  public JImageView(Image image) {
    defaultImage = image;
    super(image);
  }

  public Image getDefaultImage() {
    return defaultImage;
  }

  public JImageView setDefaultImage(Image image){
    super.setImage(image);
    defaultImage = image;
    return this;
  }
}
