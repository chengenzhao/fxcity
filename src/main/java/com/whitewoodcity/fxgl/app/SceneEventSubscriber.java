package com.whitewoodcity.fxgl.app;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public class SceneEventSubscriber<T extends Event> {

  javafx.scene.Scene fxScene;
  EventType<T> eventType;
  EventHandler<T> eventHandler;
  boolean isFilter;

  SceneEventSubscriber(javafx.scene.Scene fxScene, EventType<T> eventType, EventHandler<T> eventHandler, boolean isFilter) {
    this.fxScene = fxScene;
    this.eventType = eventType;
    this.eventHandler = eventHandler;
    this.isFilter = isFilter;

    if (isFilter) {
      fxScene.addEventFilter(eventType, eventHandler);
    } else {
      fxScene.addEventHandler(eventType, eventHandler);
    }
  }

  void unsubscribe() {
    if (isFilter) {
      fxScene.removeEventFilter(eventType, eventHandler);
    } else {
      fxScene.removeEventHandler(eventType, eventHandler);
    }
  }
}
