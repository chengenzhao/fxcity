package com.whitewoodcity.fxgl.service.handler;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.profile.DataFile;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SaveLoadHandler implements com.almasb.fxgl.profile.SaveLoadHandler {

  public static final String GAME_DATA = "gameData";

  @Override
  public void onLoad(DataFile data) {

    //will load FXGL Scene Service data also, so usually don't use this method

    // get your previously saved bundle
    var bundle = data.getBundle(GAME_DATA);

    // retrieve saved data and update your game with it
    bundle.getData().forEach(FXGL::set);
  }

  @Override
  public void onSave(DataFile data) {
    // create a new bundle to store your data
    var bundle = new Bundle(GAME_DATA);

    // store some data
    var state = FXGL.getWorldProperties();
    state.forEach((k, v) -> {
      if (v instanceof Serializable s)
        bundle.put(k, s);
      return null;
    });

    // record time
    bundle.put("time", LocalDateTime.now());

    // give the bundle to data file
    data.putBundle(bundle);
  }
}
