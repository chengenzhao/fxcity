package com.whitewoodcity.flame;

import com.whitewoodcity.flame.pushnpoppane.PushAndPop;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

public class Navigator extends HashMap<String, Constructor> {

  private String initialRoute = "/";

  public Navigator() {
    super();
  }

  public Navigator(String initialRoute) {
    super();
    this.initialRoute = initialRoute;
  }

  public Navigator route(String key, Constructor value) {
    put(key, value);
    return this;
  }

  public String getInitialRoute() {
    return initialRoute;
  }

  public PushAndPop initialRoute() {
    return get(initialRoute).build(new HashMap<>());
  }

  public void setInitialRoute(String initialRoute) {
    this.initialRoute = initialRoute;
  }

  public static void pushNamed(String paneName) {
    pushNamed(paneName, new HashMap<>());
  }

  public static void pushNamed(Runnable setOnStarted, String paneName) {
    setOnStarted.run();
    pushNamed(paneName);
  }

  public static void pushNamed(String paneName, Runnable setOnFinished) {
    pushNamed(paneName);
    setOnFinished.run();
  }

  public static void pushNamed(String paneName, Map<String, Object> parameters) {
    push(MainInterface.getRoutes().get(paneName).build(parameters));
  }

  public static void pushNamed(String paneName, Map<String, Object> parameters, Runnable setOnFinished) {
    pushNamed(paneName, parameters);
    setOnFinished.run();
  }

  public static void pushNamed(Runnable setOnStarted, String paneName, Map<String, Object> parameters) {
    setOnStarted.run();
    pushNamed(paneName, parameters);
  }

  public static void push(PushAndPop node) {
    var root = MainInterface.getRoot();
    var currentNode = (PushAndPop) root.getChildren().get(root.getChildren().size() - 1);
    currentNode.disable();

    node.disable();
    root.getChildren().add(node.getNode());
    var transition = node.startTransition();
    transition.setOnFinished(e -> Platform.runLater(()->{
      node.enable();
      node.getNode().requestFocus();
    }));
    transition.play();;
  }

  public static void replaceNamed(String paneName) {
    replaceNamed(paneName, new HashMap<>());
  }

  public static void replaceNamed(String paneName, Map<String, Object> parameters, Runnable setOnFinished) {
    replaceNamed(paneName, parameters);
    setOnFinished.run();
  }

  public static void replaceNamed(Runnable setOnStarted, String paneName, Map<String, Object> parameters) {
    setOnStarted.run();
    replaceNamed(paneName, parameters);
  }

  public static void replaceNamed(String paneName, Map<String, Object> parameters) {
    replace(MainInterface.getRoutes().get(paneName).build(parameters));
  }

  public static void replace(PushAndPop node) {
    var root = MainInterface.getRoot();
    var currentNode = (PushAndPop) root.getChildren().get(root.getChildren().size() - 1);
    currentNode.disable();
    node.disable();
    root.getChildren().add(root.getChildren().size() - 1, node.getNode());
    var transition = currentNode.endTransition();
    transition.setOnFinished(e -> Platform.runLater(()->{
      node.enable();
      node.getNode().requestFocus();
      root.getChildren().remove(currentNode);
      currentNode.dispose();
    }));
    transition.play();
  }

  public static void pop() {
    pop(new HashMap<>());
  }

  public static void pop(Map<String, Object> parameters) {
    var root = MainInterface.getRoot();
    if (root.getChildren().size() > 1) {
      var node = (PushAndPop) root.getChildren().get(root.getChildren().size() - 1);
      node.disable();

      var nextNode = (PushAndPop) root.getChildren().get(root.getChildren().size() - 2);
      nextNode.getParameters().putAll(parameters);
      nextNode.disable();

      var transition = node.endTransition();
      transition.setOnFinished(e -> Platform.runLater(()->{
        nextNode.enable();
        nextNode.getNode().requestFocus();
        root.getChildren().remove(node);
        node.dispose();
      }));
      transition.play();
    }
  }

  public static void pop(Runnable setOnStarted){//, Runnable setOnFinished
    setOnStarted.run();
    pop(new HashMap<>());
  }

  public static void pop(Runnable setOnStarted, Runnable setOnFinished) {
    setOnStarted.run();
    pop(new HashMap<>(), setOnFinished);
  }

  public static void pop(Map<String, Object> parameters, Runnable setOnFinished) {
    var root = MainInterface.getRoot();
    if (root.getChildren().size() > 1) {
      var node = (PushAndPop) root.getChildren().get(root.getChildren().size() - 1);
      node.disable();

      var nextNode = (PushAndPop) root.getChildren().get(root.getChildren().size() - 2);
      nextNode.setParameters(parameters);
      nextNode.disable();

      var transition = node.endTransition();
      transition.setOnFinished(e -> Platform.runLater(()->{
        nextNode.enable();
        nextNode.getNode().requestFocus();
        root.getChildren().remove(node);
        node.dispose();
        setOnFinished.run();
      }));
      transition.play();
    }
  }
}
