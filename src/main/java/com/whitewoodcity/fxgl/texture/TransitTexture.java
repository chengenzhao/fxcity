package com.whitewoodcity.fxgl.texture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitTexture extends Texture {

  public enum JsonKeys {
    X("x"),
    Y("y"),
    TRANSLATE_X("translateX"),
    TRANSLATE_Y("translateY"),
    ROTATES("rotates"),
    PIVOT_X("pivotX"),
    PIVOT_Y("pivotY"),
    ANGLE("angle"),
    TIME("time"),
    ;

    private final String key;

    JsonKeys(final String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }

  private Transition currentTransition;
  private final Map<String, Transition> transitions = new HashMap<>();
  private final Map<String, Transition> secondTrans = new HashMap<>();
  private final Map<String, ObjectNode> poses = new HashMap<>();

  protected TransitTexture(Image image) {
    super(image);
  }

  public TransitTexture(Image image, int howManyRotatesInTransforms) {
    super(image);

    for (int i = 0; i < howManyRotatesInTransforms; i++) {
      this.getTransforms().add(new Rotate());
    }
  }

  @Override
  public TransitTexture copy() {
    var texture = new TransitTexture(this.getImage());
    for (var transform : this.getTransforms()) {
      texture.getTransforms().add(transform.clone());
    }
    return texture;
  }

  public void record(String name, String jsonString) {
    record(name, jsonString, 1.0, false);
  }

  public void record(String name, String jsonString, double ratio, boolean round) {
    try {
      var jsonNode = new ObjectMapper().readTree(jsonString);
      record(name, jsonNode, ratio, round);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void record(String name, JsonNode json, double ratio) {
    record(name, json, ratio, false);
  }

  public void record(String name, JsonNode json, double ratio, boolean round) {
    switch (json) {
      case ArrayNode arrayNode -> {
        arrayNode.forEach(e -> zoom((ObjectNode) e, ratio, round));
        buildTransition(name, arrayNode);
      }
      case ObjectNode objectNode -> recordPose(name, zoom(objectNode, ratio, round));
      default -> throw new RuntimeException("Not supported JSON type");
    }
  }

  private ObjectNode zoom(ObjectNode obj, double ratio, boolean round) {
    zoomField(obj, JsonKeys.X, ratio, round);
    zoomField(obj, JsonKeys.Y, ratio, round);

    zoomField(obj, JsonKeys.TRANSLATE_X, ratio, round);
    zoomField(obj, JsonKeys.TRANSLATE_Y, ratio, round);

    var rotates = obj.withArray(JsonKeys.ROTATES.key);
    for (JsonNode r : rotates) {
      var rotate = (ObjectNode) r;
      zoomField(rotate, JsonKeys.PIVOT_X, ratio, round);
      zoomField(rotate, JsonKeys.PIVOT_Y, ratio, round);
    }
    return obj;
  }

  private void zoomField(ObjectNode obj, JsonKeys key, double ratio, boolean round) {
    if (obj.has(key.key)) {
      var value = obj.get(key.key).asDouble() * ratio;
      if (round) value = Math.round(value);
      obj.put(key.key, value);
    }
  }

  public void record(String name, JsonNode json) {
    switch (json) {
      case ArrayNode arrayNode -> buildTransition(name, arrayNode);
      case ObjectNode objectNode -> recordPose(name, objectNode);
      default -> throw new RuntimeException("Not supported JSON type");
    }
  }

  public Transition buildTransition(String name, String jsonArray) {
    var objectMapper = new ObjectMapper();
    try {
      var array = objectMapper.readTree(jsonArray);
      return buildTransition(name, (ArrayNode) array);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Transition buildTransition(String name, ArrayNode jsonArray) {
    var list = new ArrayList<TransitionData>();
    for (int i = 0; i < jsonArray.size() - 1; i++) {
      list.add(new TransitionData((ObjectNode) jsonArray.get(i), (ObjectNode) jsonArray.get(i + 1)));
    }
    var tran = new SequentialTransition(this);
    for (var data : list) {
      tran.getChildren().add(new CustomTransition(this, data.start(), data.end()));
    }
    this.transitions.put(name, tran);
    return tran;
  }

  public void recordPose(String name, ObjectNode json) {
    poses.put(name, json);
  }

  public void show(String json) {
    try {
      var obj = (ObjectNode) new ObjectMapper().readTree(json);
      show(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void show(ObjectNode json) {
    if (json.has(JsonKeys.X.key)) {
      this.setX(json.get(JsonKeys.X.key).asDouble());
    }

    if (json.has(JsonKeys.Y.key)) {
      this.setY(json.get(JsonKeys.Y.key).asDouble());
    }

    if (json.has(JsonKeys.TRANSLATE_X.key)) {
      this.setTranslateX(json.get(JsonKeys.TRANSLATE_X.key).asDouble());
    }

    if (json.has(JsonKeys.TRANSLATE_Y.key())) {
      this.setTranslateY(json.get(JsonKeys.TRANSLATE_Y.key).asDouble());
    }

    if(json.has(JsonKeys.ROTATES.key())) {
      var rotates = json.withArray(JsonKeys.ROTATES.key);
      List<Rotate> transforms =
        this.getTransforms().stream()
          .filter(Rotate.class::isInstance)
          .map(Rotate.class::cast)
          .toList();
      for (int i = 0; i < rotates.size(); i++) {
        var r = rotates.get(i);
        var rotate = transforms.get(i);
        rotate.setPivotX(r.get(JsonKeys.PIVOT_X.key).asDouble());
        rotate.setPivotY(r.get(JsonKeys.PIVOT_Y.key).asDouble());
        rotate.setAngle(r.get(JsonKeys.ANGLE.key).asDouble());
      }
    }
  }

  public void pose(String name) {
    var pose = poses.get(name);
    if (pose != null)
      show(pose);
  }

  public Transition startTransition(String name) {
    setTransition(name);
    return play();
  }

  public Transition loopTransition(String name) {
    setTransition(name);
    return loop();
  }

  public boolean loopNoOverride(String name){
    if(currentTransition!=null && currentTransition==transitions.get(name))
      return false;

    stopTransition();
    loopTransition(name);
    return true;
  }

  public Transition setTransition(String name) {
    return setTransition(name, 1);
  }

  public Transition setTransition(String name, int cycleCount) {
    currentTransition = transitions.get(name);
    if (currentTransition != null)
      currentTransition.setCycleCount(cycleCount);

    return currentTransition;
  }

  public void stopTransition() {
    if (currentTransition != null) {
      currentTransition.stop();
      currentTransition = null;
    }
  }

  public Transition getCurrentTransition() {
    return currentTransition;
  }

  public Transition stop() {
    stopTransition();
    return currentTransition;
  }

  public Transition play() {
    if (currentTransition != null)
      currentTransition.playFromStart();
    return currentTransition;
  }

  public Transition loop() {
    if (currentTransition != null) {
      currentTransition.setCycleCount(Timeline.INDEFINITE);
      currentTransition.play();
    }
    return currentTransition;
  }

  public Transition pause() {
    if (currentTransition != null)
      currentTransition.pause();
    return currentTransition;
  }

  public Transition resume() {
    if (currentTransition != null)
      currentTransition.play();
    return currentTransition;
  }

  public boolean isPaused() {
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.PAUSED;
    else return false;
  }

  public boolean isRunning() {
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.RUNNING;
    else return false;
  }

  public boolean isPlaying() {//playing means running only once
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.RUNNING && currentTransition.getCycleCount() == 1;
    else return false;
  }

  public boolean isLooping() {//looping means running infinite times
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.RUNNING && currentTransition.getCycleCount() == Timeline.INDEFINITE;
    else return false;
  }
}

record TransitionData(ObjectNode start, ObjectNode end) {
}

class CustomTransition extends Transition {

  private final TransitTexture cachedNode;
  private final ObjectNode start;
  private final ObjectNode end;

  public CustomTransition(TransitTexture cachedNode, ObjectNode start, ObjectNode end) {
    this.cachedNode = cachedNode;

    this.start = start;
    this.end = end;
    setCycleDuration(Duration.millis(end.get(TransitTexture.JsonKeys.TIME.key()).asDouble() - start.get(TransitTexture.JsonKeys.TIME.key()).asDouble()));
  }

  @Override
  protected void interpolate(double frac) {

    if(start.has(TransitTexture.JsonKeys.X.key()))
      cachedNode.setX((end.get(TransitTexture.JsonKeys.X.key()).asDouble() - start.get(TransitTexture.JsonKeys.X.key()).asDouble()) * frac + start.get(TransitTexture.JsonKeys.X.key()).asDouble());
    if(start.has(TransitTexture.JsonKeys.Y.key()))
      cachedNode.setY((end.get(TransitTexture.JsonKeys.Y.key()).asDouble() - start.get(TransitTexture.JsonKeys.Y.key()).asDouble()) * frac + start.get(TransitTexture.JsonKeys.Y.key()).asDouble());

    if(start.has(TransitTexture.JsonKeys.TRANSLATE_X.key()))
      cachedNode.setTranslateX((end.get(TransitTexture.JsonKeys.TRANSLATE_X.key()).asDouble() - start.get(TransitTexture.JsonKeys.TRANSLATE_X.key()).asDouble()) * frac + start.get(TransitTexture.JsonKeys.TRANSLATE_X.key()).asDouble());
    if(start.has(TransitTexture.JsonKeys.TRANSLATE_Y.key()))
      cachedNode.setTranslateY((end.get(TransitTexture.JsonKeys.TRANSLATE_Y.key()).asDouble() - start.get(TransitTexture.JsonKeys.TRANSLATE_Y.key()).asDouble()) * frac + start.get(TransitTexture.JsonKeys.TRANSLATE_Y.key()).asDouble());

    if(start.has(TransitTexture.JsonKeys.ROTATES.key())) {
      var rotatesStart = start.withArray(TransitTexture.JsonKeys.ROTATES.key());
      var rotatesEnd = end.withArray(TransitTexture.JsonKeys.ROTATES.key());
      for (int i = 0; i < rotatesStart.size(); i++) {
        var rotateStart = rotatesStart.get(i);
        var rotateEnd = rotatesEnd.get(i);
        var rotate = (Rotate) cachedNode.getTransforms().get(i);
        rotate.setPivotX((rotateEnd.get(TransitTexture.JsonKeys.PIVOT_X.key()).asDouble() - rotateStart.get(TransitTexture.JsonKeys.PIVOT_X.key()).asDouble()) * frac + rotateStart.get(TransitTexture.JsonKeys.PIVOT_X.key()).asDouble());
        rotate.setPivotY((rotateEnd.get(TransitTexture.JsonKeys.PIVOT_Y.key()).asDouble() - rotateStart.get(TransitTexture.JsonKeys.PIVOT_Y.key()).asDouble()) * frac + rotateStart.get(TransitTexture.JsonKeys.PIVOT_Y.key()).asDouble());
        rotate.setAngle((rotateEnd.get(TransitTexture.JsonKeys.ANGLE.key()).asDouble() - rotateStart.get(TransitTexture.JsonKeys.ANGLE.key()).asDouble()) * frac + rotateStart.get(TransitTexture.JsonKeys.ANGLE.key()).asDouble());
      }
    }
  }
}