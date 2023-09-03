package com.whitewoodcity.fxgl.texture;

import com.almasb.fxgl.core.Copyable;
import com.almasb.fxgl.core.View;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class Texture extends ImageView implements View, Copyable<Texture> {
  private final double width, height;

  public Texture(Image image) {
    super(image);
    width = image.getWidth();
    height = image.getHeight();
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  @Override
  public Texture copy() {
    return new Texture(this.getImage());
  }

  /**
   * Given a rectangular area, produces a sub-texture of this texture.
   * <p>
   * Rectangle cannot cover area out of the original texture image.
   *
   * @param area area of the original texture that represents sub-texture
   * @return sub-texture
   */
  public Texture subTexture(Rectangle2D area) {
    //sub image of area
    final var minX = (int) area.getMinX();
    final var minY = (int) area.getMinY();
    final var maxX = (int) area.getMaxX();
    final var maxY = (int) area.getMaxY();

    assert (minX >= 0);
    assert (minY >= 0);
    assert (maxX <= this.width);
    assert (maxY <= this.height);

    final var pixelReader = this.getImage().getPixelReader();
    final var newImage = new WritableImage(maxX - minX, maxY - minY);

    newImage.getPixelWriter().setPixels(0, 0, (int) newImage.getWidth(), (int) newImage.getHeight(), pixelReader, minX, minY);

    return new Texture(newImage);
  }

  /**
   * Generates a new texture which combines this and given texture.
   * The given texture is appended based on the direction provided.
   *
   * @param other     the texture to append to this one
   * @param direction the direction to append from
   * @return new combined texture
   */
  public Texture superTexture(Texture other, HorizontalDirection direction) {
    final Image leftImage;
    final Image rightImage;

    if (direction == HorizontalDirection.LEFT) {
      leftImage = other.getImage();
      rightImage = this.getImage();
    } else {
      leftImage = this.getImage();
      rightImage = other.getImage();
    }

    final var width = (int) (leftImage.getWidth() + rightImage.getWidth());
    final var height = (int) Math.max(leftImage.getHeight(), rightImage.getHeight());

    final var leftReader = leftImage.getPixelReader();
    final var rightReader = rightImage.getPixelReader();
    final var image = new WritableImage(width, height);
    final var pixelWriter = image.getPixelWriter();

    pixelWriter.setPixels(0, 0, (int) leftImage.getWidth(), (int) leftImage.getHeight(), leftReader, 0, 0);
    pixelWriter.setPixels((int) leftImage.getWidth(), 0, (int) rightImage.getWidth(), (int) rightImage.getHeight(), rightReader, 0, 0);

    return new Texture(image);
  }

  /**
   * Generates a new texture which combines this and given texture.
   * The given texture is appended based on the direction provided.
   *
   * @param other the texture to append to this one
   * @param direction the direction to append from
   * @return new combined texture
   */
  public Texture superTexture(Texture other, VerticalDirection direction)  {
    final Image topImage;
    final Image bottomImage;

    if (direction == VerticalDirection.DOWN) {
      topImage = this.getImage();
      bottomImage = other.getImage();
    } else {
      topImage = other.getImage();
      bottomImage = this.getImage();
    }

    final var width = (int)Math.max(topImage.getWidth(), bottomImage.getWidth());
    final var height = (int)(topImage.getHeight() + bottomImage.getHeight());
    final var topReader = topImage.getPixelReader();
    final var bottomReader = bottomImage.getPixelReader();
    final var image = new WritableImage(width, height);
    final var pixelWriter = image.getPixelWriter();

    pixelWriter.setPixels(0, 0, (int)topImage.getWidth(), (int)topImage.getHeight(), topReader, 0, 0);
    pixelWriter.setPixels(0, (int)topImage.getHeight(), (int)bottomImage.getWidth(), (int)bottomImage.getHeight(), bottomReader, 0, 0);

    return new Texture(image);
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public void dispose() {
    setImage(null);
  }

  @Override
  public void onUpdate(double tpf) {
    // no-op
  }
}
