package com.whitewoodcity.fxgl.texture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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

  private Transition currentTransition;
  private final Map<String, Transition> transitions = new HashMap<>();
  private final Map<String, JsonNode> poses = new HashMap<>();

  public TransitTexture(Image image) {
    super(image);
  }

  @Override
  public TransitTexture copy() {
    var texture = new TransitTexture(this.getImage());
    for (var transform : this.getTransforms()) {
      texture.getTransforms().add(transform.clone());
    }
    return texture;
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
      tran.getChildren().add(new CusteomTransition(this, data.start(), data.end()));
    }
    this.transitions.put(name, tran);
    return tran;
  }

  public void recordPose(String name, JsonNode json) {
    poses.put(name, json);
  }

  public void show(String json) {
    try {
      var obj = new ObjectMapper().readTree(json);
      show(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void show(JsonNode json) {
    this.setX(json.get("x").asDouble());
    this.setY(json.get("y").asDouble());

    if (json.has("translateX")) {
      this.setTranslateX(json.get("translateX").asDouble());
    }

    if (json.has("translateY")) {
      this.setTranslateY(json.get("translateY").asDouble());
    }

    var rotates = json.withArray("rotates");
    List<Rotate> transforms =
      this.getTransforms().stream()
        .filter(Rotate.class::isInstance)
        .map(Rotate.class::cast)
        .toList();
    for (int i = 0; i < rotates.size(); i++) {
      var r = rotates.get(i);
      var rotate = transforms.get(i);
      rotate.setPivotX(r.get("pivotX").asDouble());
      rotate.setPivotY(r.get("pivotY").asDouble());
      rotate.setAngle(r.get("angle").asDouble());
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
    }
  }

  public Transition getCurrentTransition() {
    return currentTransition;
  }

  public Transition stop() {
    if (currentTransition != null)
      currentTransition.stop();
    return currentTransition;
  }

  public Transition play() {
    if (currentTransition != null)
      currentTransition.play();
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

class CusteomTransition extends Transition {

  private final TransitTexture cachedNode;
  private final ObjectNode start;
  private final ObjectNode end;

  public CusteomTransition(TransitTexture cachedNode, ObjectNode start, ObjectNode end) {
    this.cachedNode = cachedNode;

    initObjectNode(start, end);

    this.start = start;
    this.end = end;
    setCycleDuration(Duration.millis(end.get("time").asDouble() - start.get("time").asDouble()));
  }

  private void initObjectNode(ObjectNode... nodes) {
    var factory = JsonNodeFactory.instance;
    for (var node : nodes) {
      if (!node.has("x")) node.set("x", factory.numberNode(0.0));
      if (!node.has("y")) node.set("y", factory.numberNode(0.0));
      if (!node.has("translateX")) node.set("translateX", factory.numberNode(0.0));
      if (!node.has("translateY")) node.set("translateY", factory.numberNode(0.0));
    }
  }

  @Override
  protected void interpolate(double frac) {

    cachedNode.setX((end.get("x").asDouble() - start.get("x").asDouble()) * frac + start.get("x").asDouble());
    cachedNode.setY((end.get("y").asDouble() - start.get("y").asDouble()) * frac + start.get("y").asDouble());

    cachedNode.setTranslateX((end.get("translateX").asDouble() - start.get("translateX").asDouble()) * frac + start.get("translateX").asDouble());
    cachedNode.setTranslateY((end.get("translateY").asDouble() - start.get("translateY").asDouble()) * frac + start.get("translateY").asDouble());

    var rotatesStart = start.withArray("rotates");
    var rotatesEnd = end.withArray("rotates");
    for (int i = 0; i < rotatesStart.size(); i++) {
      var rotateStart = rotatesStart.get(i);
      var rotateEnd = rotatesEnd.get(i);
      var rotate = (Rotate) cachedNode.getTransforms().get(i);
      rotate.setPivotX((rotateEnd.get("pivotX").asDouble() - rotateStart.get("pivotX").asDouble()) * frac + rotateStart.get("pivotX").asDouble());
      rotate.setPivotY((rotateEnd.get("pivotY").asDouble() - rotateStart.get("pivotY").asDouble()) * frac + rotateStart.get("pivotY").asDouble());
      rotate.setAngle((rotateEnd.get("angle").asDouble() - rotateStart.get("angle").asDouble()) * frac + rotateStart.get("angle").asDouble());
    }
  }
}