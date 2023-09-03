package com.whitewoodcity.fxgl.service;

public interface ReplaceableGameSceneWithConcurrentService extends ReplaceableGameScene{
  void disableConcurrency();
  void restoreConcurrency();//make sure you call this method before pop sub scene because there is a listener to activate when it is removed
  void disableInput();
  void restoreInput();
}
