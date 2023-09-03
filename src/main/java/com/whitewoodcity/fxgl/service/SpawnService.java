package com.whitewoodcity.fxgl.service;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.whitewoodcity.fxgl.service.exception.MethodHasNotImplementedException;

public interface SpawnService<T> {
  default Entity build(T type, SpawnData data) throws RuntimeException {
    throw new RuntimeException(new MethodHasNotImplementedException("ReplaceGameSceneService.of method has not yet implemented"));
  }

  default Entity build(T type, double x, double y){
    return build(type, new SpawnData(x, y));
  }

  default Entity spawn(T type, double x, double y){
    return spawn(type, new SpawnData(x, y));
  }

  Entity spawn(T type, SpawnData data);

  default Entity spawn(T type, SpawnData data, GameWorld gameWorld){
    return spawn(type, data);
  }
}
