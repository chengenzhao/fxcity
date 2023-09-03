package com.whitewoodcity.fxgl.service;

public interface DimensionService {
  default int getGameWidth(){
    return 4200;
  }

  default int getGameHeight() {
    return 1000;
  }
}
