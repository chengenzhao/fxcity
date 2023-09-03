package com.whitewoodcity.flame.pushnpoppane;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.Map;

public sealed interface PushAndPop
  permits PushAndPopPane, PushAndPopStackPane, PushAndPopVBox {
  void disable();

  void enable();

  default void dispose() {
  }

  Map<String, Object> getParameters();

  void setParameters(Map<String, Object> parameters);

  default Transition startTransition() {
    FadeTransition ft = new FadeTransition(Duration.millis(1000), getNode());
    ft.setFromValue(0.0);
    ft.setToValue(1.0);
    return ft;
  }

  default Transition endTransition() {
    FadeTransition ft = new FadeTransition(Duration.millis(1000), getNode());
    ft.setFromValue(1.0);
    ft.setToValue(0.0);
    return ft;
  }

  default Node getNode() {
    return (Node) this;
  }
}
