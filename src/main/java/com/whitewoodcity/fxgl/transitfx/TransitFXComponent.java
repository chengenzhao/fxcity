package com.whitewoodcity.fxgl.transitfx;

import com.almasb.fxgl.entity.component.Component;
import io.javafx.whitewoodcity.JVGComponent;
import io.javafx.whitewoodcity.JsonFile;
import io.javafx.whitewoodcity.jvg.JVG;
import io.javafx.whitewoodcity.jvg.JVGs;
import io.javafx.whitewoodcity.transition.Rotates;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public abstract class TransitFXComponent extends Component implements TransitFXComponentInterface {
  protected final double factor;

  protected Map<JVGComponent, ImageView> imageViewMap = new HashMap<>();
  protected Map<JVGComponent, Rotates> rotatesMap = new HashMap<>();

  protected TransitFXComponent(double factor) {
    this.factor = factor;
  }

  public static Map<JVGComponent, Image> loadImage(Map<JVGComponent, JVG> jvgMap, double zoomFactor) {
    Map<JVGComponent, Image> images = new HashMap<>();
    CountDownLatch latch = new CountDownLatch(jvgMap.size());
    for (var c : jvgMap.keySet()) {
      var jvg = jvgMap.get(c).copy().zoom(zoomFactor);

      if (Platform.isFxApplicationThread()) {
        images.put(c, jvg.toImage());
        latch.countDown();
      } else {
        Platform.runLater(() -> {
          images.put(c, jvg.toImage());
          latch.countDown();
        });
      }
    }

    try {
      latch.await();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return images;
  }

  public static Map<JVGComponent, Image> loadImage(List<JVGComponent> components, double zoomFactor) {
    return loadImage(loadJVG(components), zoomFactor);
  }

  public static Map<String, JsonObject> loadActions(List<JsonFile> actions) {
    return TransitFXComponentInterface.loadJsonObject(actions);
  }

  public double getFactor() {
    return factor;
  }

  public static Map<JVGComponent, JVG> loadJVG(List<JVGComponent> components) {
    Map<JVGComponent, JVG> jvgMap = new HashMap<>();
    var set = new HashSet<>(components);
    for (var c : set) {
      var jvg = new JVG(c.getJsonString(), true);
      jvgMap.put(c, jvg);
    }
    return jvgMap;
  }

  public static Map<JsonFile, JVGs> loadJVGs(List<JsonFile> footages) {
    Map<JsonFile, JVGs> jvgMap = new HashMap();
    var set = new HashSet<>(footages);
    for (var c : set) {
      var jvgs = new JVGs(c.getJsonString(), true);
      jvgMap.put(c, jvgs);
    }
    return jvgMap;
  }


  public void buildImageViewAndRotations(Map<JVGComponent, Image> jvgMap, Map<String, JsonObject> actMap) {
    buildImageViews(jvgMap);

    buildRotations(jvgMap.keySet(), actMap);
  }

  public void buildRotations(Collection<JVGComponent> componentCollection, Map<String, JsonObject> actMap) {
    for (var a : actMap.keySet()) {
      var json = actMap.get(a);
      for (var c : componentCollection) {
        switch (json.getValue(c.getFileName())) {
          case JsonObject object -> rotatesMap.get(c).record(a, object.toString(), factor, true);
          case JsonArray array -> rotatesMap.get(c).record(a, array.toString(), factor, true);
          default -> throw new RuntimeException(json + " is not a valid Json String");
        }
      }
    }
  }

  public void buildImageViews(Map<JVGComponent, Image> jvgMap) {
    CountDownLatch latch = new CountDownLatch(jvgMap.size());
    for (var c : jvgMap.keySet()) {
      var obj = jvgMap.get(c);

      if (Platform.isFxApplicationThread()) {
        buildImageView(obj, c);
        latch.countDown();
      } else {
        Platform.runLater(() -> {
          buildImageView(obj, c);
          latch.countDown();
        });
      }
    }

    try {
      latch.await();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void buildImageView(Image image, JVGComponent c) {
    var view = new ImageView(image);
    imageViewMap.put(c, view);
    rotatesMap.put(c, new Rotates(view, c.rotateNum()));
  }

  public abstract List<JVGComponent> getComponents();

  protected void pose(String actionName) {
    pose(getComponents(), actionName);
  }

  protected void pose(List<JVGComponent> components, String actionName) {
    for (var c : components) {
      rotatesMap.get(c).pose(actionName);
    }
  }

  protected void gotoLoop(String actionName) {
    for (var c : getComponents()) {
      rotatesMap.get(c).gotoLoop(actionName);
    }
  }
}
