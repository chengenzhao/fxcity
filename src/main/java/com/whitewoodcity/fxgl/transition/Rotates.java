package com.whitewoodcity.fxgl.transition;

import module com.fasterxml.jackson.databind;
import module java.base;
import module javafx.controls;
import com.whitewoodcity.fxgl.vectorview.JVG;
import javafx.util.Duration;

public class Rotates {
  private final Node node;

  private Transition currentTransition;
  private final Map<String, Transition> transitions = new HashMap<>();
  private final Map<String, ObjectNode> poses = new HashMap<>();
  
  public Rotates(Node node, int howManyRotatesInTransforms) {
    this.node = node;

    for (int i = 0; i < howManyRotatesInTransforms; i++) {
      node.getTransforms().add(new Rotate());
    }
  }

  public Node getNode() {
    return node;
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
    zoomField(obj, RotateJsonKeys.X, ratio, round);
    zoomField(obj, RotateJsonKeys.Y, ratio, round);

    zoomField(obj, RotateJsonKeys.TRANSLATE_X, ratio, round);
    zoomField(obj, RotateJsonKeys.TRANSLATE_Y, ratio, round);

    var rotates = obj.withArray(RotateJsonKeys.ROTATES.key());
    for (JsonNode r : rotates) {
      var rotate = (ObjectNode) r;
      zoomField(rotate, RotateJsonKeys.PIVOT_X, ratio, round);
      zoomField(rotate, RotateJsonKeys.PIVOT_Y, ratio, round);
    }
    return obj;
  }

  private void zoomField(ObjectNode obj, RotateJsonKeys key, double ratio, boolean round) {
    if (obj.has(key.key())) {
      var value = obj.get(key.key()).asDouble() * ratio;
      if (round) value = Math.round(value);
      obj.put(key.key(), value);
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
    var tran = new SequentialTransition(getNode());
    for (var data : list) {
      tran.getChildren().add(new CustomTransition(getNode(), data.start(), data.end()));
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

    if(json.has(RotateJsonKeys.X.key())||json.has(RotateJsonKeys.Y.key())) {
      var x = 0.;
      var y = 0.;
      if (json.has(RotateJsonKeys.X.key())) {
        x = json.get(RotateJsonKeys.X.key()).asDouble();
      }

      if (json.has(RotateJsonKeys.Y.key())) {
        y = json.get(RotateJsonKeys.Y.key()).asDouble();
      }

      switch (getNode()){
        case JVG jvg -> jvg.set(x,y);
        case ImageView imageView -> {
          imageView.setX(x);
          imageView.setY(y);
        }
        case Rectangle rectangle -> {
          rectangle.setX(x);
          rectangle.setY(y);
        }
        case null -> throw new RuntimeException("Node is null");
        default -> throw new RuntimeException("Unable to move Node by x&y coordinates, only ImageView, JVG and Rectangle could be moved by setting x & y coordinates");
      }
    }

    if (json.has(RotateJsonKeys.TRANSLATE_X.key())) {
      getNode().setTranslateX(json.get(RotateJsonKeys.TRANSLATE_X.key()).asDouble());
    }

    if (json.has(RotateJsonKeys.TRANSLATE_Y.key())) {
      getNode().setTranslateY(json.get(RotateJsonKeys.TRANSLATE_Y.key()).asDouble());
    }

    if(json.has(RotateJsonKeys.ROTATES.key())) {
      var rotates = json.withArray(RotateJsonKeys.ROTATES.key());
      List<Rotate> transforms =
        getNode().getTransforms().stream()
          .filter(Rotate.class::isInstance)
          .map(Rotate.class::cast)
          .toList();
      for (int i = 0; i < rotates.size(); i++) {
        var r = rotates.get(i);
        var rotate = transforms.get(i);
        rotate.setPivotX(r.get(RotateJsonKeys.PIVOT_X.key()).asDouble());
        rotate.setPivotY(r.get(RotateJsonKeys.PIVOT_Y.key()).asDouble());
        rotate.setAngle(r.get(RotateJsonKeys.ANGLE.key()).asDouble());
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

  private final Node node;
  private final ObjectNode start;
  private final ObjectNode end;

  public CustomTransition(Node node, ObjectNode start, ObjectNode end) {
    this.node = node;

    this.start = start;
    this.end = end;
    setCycleDuration(Duration.millis(end.get(RotateJsonKeys.TIME.key()).asDouble() - start.get(RotateJsonKeys.TIME.key()).asDouble()));
  }

  @Override
  protected void interpolate(double frac) {

    if(start.has(RotateJsonKeys.X.key())||start.has(RotateJsonKeys.Y.key())) {
      var x = 0.0;
      var y = 0.0;
      if (start.has(RotateJsonKeys.X.key()))
        x = (end.get(RotateJsonKeys.X.key()).asDouble() - start.get(RotateJsonKeys.X.key()).asDouble()) * frac + start.get(RotateJsonKeys.X.key()).asDouble();
      if (start.has(RotateJsonKeys.Y.key()))
        y = (end.get(RotateJsonKeys.Y.key()).asDouble() - start.get(RotateJsonKeys.Y.key()).asDouble()) * frac + start.get(RotateJsonKeys.Y.key()).asDouble();

      switch (node) {
        case JVG jvg -> jvg.set(x, y);
        case ImageView imageView -> {
          imageView.setX(x);
          imageView.setY(y);
        }
        case Rectangle rect -> {
          rect.setX(x);
          rect.setY(y);
        }
        case null -> throw new RuntimeException("Node is null");
        default ->
          throw new RuntimeException("Unable to move Node by x&y coordinates, only ImageView, JVG and Rectangle could be moved by setting x & y coordinates");
      }
    }

    if(start.has(RotateJsonKeys.TRANSLATE_X.key()))
      node.setTranslateX((end.get(RotateJsonKeys.TRANSLATE_X.key()).asDouble() - start.get(RotateJsonKeys.TRANSLATE_X.key()).asDouble()) * frac + start.get(RotateJsonKeys.TRANSLATE_X.key()).asDouble());
    if(start.has(RotateJsonKeys.TRANSLATE_Y.key()))
      node.setTranslateY((end.get(RotateJsonKeys.TRANSLATE_Y.key()).asDouble() - start.get(RotateJsonKeys.TRANSLATE_Y.key()).asDouble()) * frac + start.get(RotateJsonKeys.TRANSLATE_Y.key()).asDouble());

    if(start.has(RotateJsonKeys.ROTATES.key())) {
      var rotatesStart = start.withArray(RotateJsonKeys.ROTATES.key());
      var rotatesEnd = end.withArray(RotateJsonKeys.ROTATES.key());
      for (int i = 0; i < rotatesStart.size(); i++) {
        var rotateStart = rotatesStart.get(i);
        var rotateEnd = rotatesEnd.get(i);
        var rotate = (Rotate) node.getTransforms().get(i);
        rotate.setPivotX((rotateEnd.get(RotateJsonKeys.PIVOT_X.key()).asDouble() - rotateStart.get(RotateJsonKeys.PIVOT_X.key()).asDouble()) * frac + rotateStart.get(RotateJsonKeys.PIVOT_X.key()).asDouble());
        rotate.setPivotY((rotateEnd.get(RotateJsonKeys.PIVOT_Y.key()).asDouble() - rotateStart.get(RotateJsonKeys.PIVOT_Y.key()).asDouble()) * frac + rotateStart.get(RotateJsonKeys.PIVOT_Y.key()).asDouble());
        rotate.setAngle((rotateEnd.get(RotateJsonKeys.ANGLE.key()).asDouble() - rotateStart.get(RotateJsonKeys.ANGLE.key()).asDouble()) * frac + rotateStart.get(RotateJsonKeys.ANGLE.key()).asDouble());
      }
    }
  }
}