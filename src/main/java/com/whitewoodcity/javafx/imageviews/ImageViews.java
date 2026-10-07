package com.whitewoodcity.javafx.imageviews;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ImageViews extends ImageView {
  private final List<Image> images;

  public ImageViews(List<Image> images){
    this.images = List.copyOf(images);
    this.setImage(this.images.getFirst());
  }

  public List<Image> getImages() {
    return images;
  }

  public void setDefaultImage(){
    this.setImage(images.getFirst());
  }
}
