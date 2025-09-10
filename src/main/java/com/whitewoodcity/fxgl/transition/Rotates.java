package com.whitewoodcity.fxgl.transition;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whitewoodcity.fxgl.vectorview.JVG;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Rotates {

}

record TransitionData(ObjectNode start, ObjectNode end) {
}

class CustomTransition extends Transition {

  private final Node cachedNode;
  private final ObjectNode start;
  private final ObjectNode end;

  public CustomTransition(Node cachedNode, ObjectNode start, ObjectNode end) {
    this.cachedNode = cachedNode;

    this.start = start;
    this.end = end;
    setCycleDuration(Duration.millis(end.get(JsonKeys.TIME.key()).asDouble() - start.get(JsonKeys.TIME.key()).asDouble()));
  }

  @Override
  protected void interpolate(double frac) {

    if(start.has(JsonKeys.X.key())||start.has(JsonKeys.Y.key())) {
      var x = 0.0;
      var y = 0.0;
      if (start.has(JsonKeys.X.key()))
        x = (end.get(JsonKeys.X.key()).asDouble() - start.get(JsonKeys.X.key()).asDouble()) * frac + start.get(JsonKeys.X.key()).asDouble();
      if (start.has(JsonKeys.Y.key()))
        y = (end.get(JsonKeys.Y.key()).asDouble() - start.get(JsonKeys.Y.key()).asDouble()) * frac + start.get(JsonKeys.Y.key()).asDouble();

      switch (cachedNode) {
        case JVG jvg -> jvg.set(x, y);
        case ImageView imageView -> {
          imageView.setX(x);
          imageView.setY(y);
        }
        case Rectangle rect -> {
          rect.setX(x);
          rect.setY(y);
        }
        default ->
          throw new RuntimeException("Unable to move Node by x&y coordinates, only ImageView, JVG and Rectangle could be moved by setting x & y coordinates");
      }
    }

    if(start.has(JsonKeys.TRANSLATE_X.key()))
      cachedNode.setTranslateX((end.get(JsonKeys.TRANSLATE_X.key()).asDouble() - start.get(JsonKeys.TRANSLATE_X.key()).asDouble()) * frac + start.get(JsonKeys.TRANSLATE_X.key()).asDouble());
    if(start.has(JsonKeys.TRANSLATE_Y.key()))
      cachedNode.setTranslateY((end.get(JsonKeys.TRANSLATE_Y.key()).asDouble() - start.get(JsonKeys.TRANSLATE_Y.key()).asDouble()) * frac + start.get(JsonKeys.TRANSLATE_Y.key()).asDouble());

    if(start.has(JsonKeys.ROTATES.key())) {
      var rotatesStart = start.withArray(JsonKeys.ROTATES.key());
      var rotatesEnd = end.withArray(JsonKeys.ROTATES.key());
      for (int i = 0; i < rotatesStart.size(); i++) {
        var rotateStart = rotatesStart.get(i);
        var rotateEnd = rotatesEnd.get(i);
        var rotate = (Rotate) cachedNode.getTransforms().get(i);
        rotate.setPivotX((rotateEnd.get(JsonKeys.PIVOT_X.key()).asDouble() - rotateStart.get(JsonKeys.PIVOT_X.key()).asDouble()) * frac + rotateStart.get(JsonKeys.PIVOT_X.key()).asDouble());
        rotate.setPivotY((rotateEnd.get(JsonKeys.PIVOT_Y.key()).asDouble() - rotateStart.get(JsonKeys.PIVOT_Y.key()).asDouble()) * frac + rotateStart.get(JsonKeys.PIVOT_Y.key()).asDouble());
        rotate.setAngle((rotateEnd.get(JsonKeys.ANGLE.key()).asDouble() - rotateStart.get(JsonKeys.ANGLE.key()).asDouble()) * frac + rotateStart.get(JsonKeys.ANGLE.key()).asDouble());
      }
    }
  }
}