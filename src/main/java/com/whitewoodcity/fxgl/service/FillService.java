package com.whitewoodcity.fxgl.service;

import javafx.scene.paint.*;

public interface FillService extends ThemeService{
  default Paint getLoadingBackgroundFill(){
    return switch (getTheme()){
      case DARK -> new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
          new Stop(0, Color.web("bfd1df")), new Stop(0.5, Color.web("3a74a6")), new Stop(0.9, Color.web("010425")));
      case LIGHT -> new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Color.web("f2fcf3")), new Stop(0.5, Color.web("e9f5e6")), new Stop(1, Color.web("62b768")));
    };
  }

  default void enable(){};

}
