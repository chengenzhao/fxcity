package com.whitewoodcity.fxgl.dsl.components;

import com.almasb.fxgl.entity.component.Component;

import javafx.util.Duration;

public class ExpireCleanComponent extends Component {

  private final Duration expire;

  public ExpireCleanComponent(Duration expire) {
    this.expire = expire;
  }

  private boolean animate = false;
  private double time = 0.0;

  public void onUpdate(double tpf) {
    time += tpf;

    if (animate) {
      updateOpacity();
    }

    if (time >= expire.toSeconds()) {
      entity.removeFromWorld();
    }
  }

  private void updateOpacity() {
    entity.setOpacity(time >= expire.toSeconds() ? 0.0 : 1 - time / expire.toSeconds());
  }

  /**
   * Enables diminishing opacity over time.
   *
   * @return this component
   */
  public ExpireCleanComponent animateOpacity(){
    animate = true;
    return this;
  }

  public boolean isComponentInjectionRequired(){
    return false;
  }

  public Duration getExpire() {
    return expire;
  }
}
