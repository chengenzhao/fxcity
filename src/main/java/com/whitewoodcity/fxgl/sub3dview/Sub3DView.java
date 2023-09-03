package com.whitewoodcity.fxgl.sub3dview;

import com.almasb.fxgl.core.Copyable;
import com.almasb.fxgl.core.View;
import javafx.scene.*;

public class Sub3DView extends SubScene implements View, Copyable<Sub3DView> {
  public Sub3DView(Parent root, double width, double height) {
    super(root, width, height);
  }

  public Sub3DView(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing sceneAntialiasing) {
    super(root, width, height, depthBuffer, sceneAntialiasing);
  }

  @Override
  public Sub3DView copy() {
    return new Sub3DView(getRoot(), getWidth(), getHeight(), isDepthBuffer(), getAntiAliasing());
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public void dispose() {

  }

  @Override
  public void onUpdate(double tpf) {

  }
}
