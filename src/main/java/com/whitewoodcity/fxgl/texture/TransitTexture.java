package com.whitewoodcity.fxgl.texture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
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
    for(var transform:this.getTransforms()){
      texture.getTransforms().add(transform.clone());
    }
    return texture;
  }

  public void buildTransition(String name, ArrayNode jsonArray) {
    var list = new ArrayList<TransitionData>();
    for (int i = 0; i < jsonArray.size() - 1; i++) {
      list.add(new TransitionData((ObjectNode) jsonArray.get(i), (ObjectNode) jsonArray.get(i + 1)));
    }
    var tran = new SequentialTransition(this);
    for (var data : list) {
      tran.getChildren().add(new CusteomTransition(this,data.start(), data.end()));
    }
    this.transitions.put(name, tran);
  }

  public void recordPose(String name, JsonNode json){
    poses.put(name,json);
  }

  public void show(JsonNode json){
    this.setX(json.get("x").asDouble());
    this.setY(json.get("y").asDouble());

    var rotates = json.withArray("rotates");
    for(int i=0;i<rotates.size();i++){
      var r = rotates.get(i);
      var rotate = (Rotate)this.getTransforms().get(i);
      rotate.setPivotX(r.get("pivotX").asDouble());
      rotate.setPivotY(r.get("pivotY").asDouble());
      rotate.setAngle(r.get("angle").asDouble());
    }
  }

  public void show(String name){
    var pose = poses.get(name);
    if(pose!=null)
      show(pose);
  }

  public void startTransition(String name) {
    currentTransition = transitions.get(name);
    if(currentTransition!=null){
      currentTransition.setCycleCount(1);
      currentTransition.play();
    }
  }

  public void loopTransition(String name) {
    currentTransition = transitions.get(name);
    if(currentTransition!=null){
      currentTransition.setCycleCount(Timeline.INDEFINITE);
      currentTransition.play();
    }
  }

  public void stopTransition() {
    if(currentTransition!=null) {
      currentTransition.stop();
    }
  }
}

record TransitionData(ObjectNode start, ObjectNode end) { }

class CusteomTransition extends Transition {

  private final TransitTexture cachedNode;
  private final ObjectNode start;
  private final ObjectNode end;

  public CusteomTransition(TransitTexture cachedNode, ObjectNode start, ObjectNode end) {
    this.cachedNode = cachedNode;
    this.start = start;
    this.end = end;
    setCycleDuration(Duration.millis(end.get("time").asDouble() - start.get("time").asDouble()));
  }

  @Override
  protected void interpolate(double frac) {

    cachedNode.setX((end.get("x").asDouble() - start.get("x").asDouble())*frac + start.get("x").asDouble());
    cachedNode.setY((end.get("y").asDouble() - start.get("y").asDouble())*frac + start.get("y").asDouble());

    var rotatesStart = start.withArray("rotates");
    var rotatesEnd = end.withArray("rotates");
    for(int i=0;i<rotatesStart.size();i++){
      var rotateStart = rotatesStart.get(i);
      var rotateEnd = rotatesEnd.get(i);
      var rotate = (Rotate)cachedNode.getTransforms().get(i);
      rotate.setPivotX((rotateEnd.get("pivotX").asDouble() - rotateStart.get("pivotX").asDouble())*frac + rotateStart.get("pivotX").asDouble());
      rotate.setPivotY((rotateEnd.get("pivotY").asDouble() - rotateStart.get("pivotY").asDouble())*frac + rotateStart.get("pivotY").asDouble());
      rotate.setAngle((rotateEnd.get("angle").asDouble() - rotateStart.get("angle").asDouble())*frac + rotateStart.get("angle").asDouble());
    }
  }
}