package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.KeyInputBuilder;
import com.almasb.fxgl.input.Input;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class XInput {
  private final Input input;
  private Map<KeyCode, KeyInputBuilder> keyInputBuilderMap;
  private final Map<KeyCode, Runnable> keyPresses, keyReleases, keyActions;

  public XInput(Input input) {
    this.input = input;
    keyInputBuilderMap = new HashMap<>();
    keyPresses = null;
    keyReleases = null;
    keyActions = null;
  }

  public XInput(Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions) {
    this.input = null;
    this.keyPresses = keyPresses;
    this.keyReleases = keyReleases;
    this.keyActions = keyActions;
  }

  public XInput onActionBegin(KeyCode keyCode, Runnable onActionBegin){
    if(keyPresses!=null){
      keyPresses.put(keyCode, onActionBegin);
    }else{
      var builder = (keyInputBuilderMap.containsKey(keyCode)) ? keyInputBuilderMap.get(keyCode) : FXGL.onKeyBuilder(input, keyCode);
      keyInputBuilderMap.put(keyCode, builder);
      builder.onActionBegin(onActionBegin);
    }
    return this;
  }

  public XInput onActionEnd(KeyCode keyCode, Runnable onActionEnd){
    if(keyPresses!=null) {
      keyReleases.put(keyCode, onActionEnd);
    }else{
      var builder = (keyInputBuilderMap.containsKey(keyCode)) ? keyInputBuilderMap.get(keyCode) : FXGL.onKeyBuilder(input, keyCode);
      keyInputBuilderMap.put(keyCode, builder);
      builder.onActionEnd(onActionEnd);
    }
    return this;
  }

  public XInput onAction(KeyCode keyCode, Runnable onAction){
    if(keyActions!=null) {
      keyActions.put(keyCode, onAction);
    }else{
      var builder = (keyInputBuilderMap.containsKey(keyCode)) ? keyInputBuilderMap.get(keyCode) : FXGL.onKeyBuilder(input, keyCode);
      keyInputBuilderMap.put(keyCode, builder);
      builder.onAction(onAction);
    }
    return this;
  }

  public XInput mockKeyPress(KeyCode keyCode){
    if(keyPresses!=null) {
      if(keyPresses.containsKey(keyCode)) keyPresses.get(keyCode).run();
    } else{
      input.mockKeyPress(keyCode);
    }
    return this;
  }

  public XInput mockKeyRelease(KeyCode keyCode){
    if(keyReleases!=null) {
      if(keyReleases.containsKey(keyCode)) keyReleases.get(keyCode).run();
    } else{
      input.mockKeyRelease(keyCode);
    }
    return this;
  }
}
