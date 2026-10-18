package com.whitewoodcity.fxgl.transition;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTransitions {
  protected Transition currentTransition;
  protected final Map<String, Transition> transitions = new HashMap<>();

  public Transition record(String name, Transition transition){
    transitions.put(name, transition);
    return transition;
  }

  public Transition play(String name){
    return this.play(name,1);
  }

  public Transition play(String name, int cycleCount){
    setTransition(name);
    return play(cycleCount);
  }

  public Transition startTransition(String name) {
    setTransition(name);
    return play();
  }

  public Transition loop(String name){
    return loopTransition(name);
  }

  public Transition loopTransition(String name) {
    setTransition(name);
    return loop();
  }

  /**
   * Try to loop an action but do nothing if there is an action is running
   * @param loopName
   * @return false if there is an action running and do nothing
   * true if successfully run the loop.
   */
  public boolean loopIdle(String loopName){
    if(isRunning()) return false;

    stopTransition();
    loopTransition(loopName);
    return true;
  }


  /**
   * Try to loop an action but do nothing if there is an action is running
   * @return false if there is an action running and do nothing
   * true if successfully run the play.
   */
  public boolean playIdle(String name){
    if(isRunning()) return false;

    stopTransition();
    play(name);
    return true;
  }

  /**
   *
   * @return whether this method has broken the loop,
   * false means there is a loop running while true means successfully start a new loop
   */
  public boolean playNoOverride(String name){
    if(isPlaying(name))
      return false;

    stopTransition();
    play(name);
    return true;
  }

  /**
   *
   * @return whether this method has broken the loop,
   * false means there is a loop running while true means successfully start a new loop
   */
  public boolean loopNoOverride(String loopName){
    if(isLooping(loopName))
      return false;

    stopTransition();
    loopTransition(loopName);
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

  public Transition play(){
    return this.play(1);
  }

  public Transition play(int cycleCount) {
    if (currentTransition != null) {
      currentTransition.setCycleCount(cycleCount);
      currentTransition.playFromStart();
    }
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

  public boolean isRunning(String transition){
    return isRunning() && currentTransition == transitions.get(transition);
  }

  public boolean isPlaying(String transition){
    return isPlaying() && currentTransition == transitions.get(transition);
  }

  public boolean isPlaying() {//playing means running positive times
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.RUNNING && currentTransition.getCycleCount() > 0;
    else return false;
  }

  public boolean isLooping(String transition){
    return isLooping() && currentTransition == transitions.get(transition);
  }

  public boolean isLooping() {//looping means running infinite times
    if (currentTransition != null)
      return currentTransition.getStatus() == Animation.Status.RUNNING && currentTransition.getCycleCount() == Timeline.INDEFINITE;
    else return false;
  }

  public void play(EventHandler<ActionEvent> handler) {
    play(1, handler);
  }

  public void play(int cycleCount, EventHandler<ActionEvent> handler) {
    currentTransition.setCycleCount(cycleCount);
    currentTransition.setOnFinished(handler);
    currentTransition.playFromStart();
  }
}
