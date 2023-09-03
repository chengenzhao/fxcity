package com.whitewoodcity.fxgl.service.exception;

import java.util.Arrays;

public class WorldPropertiesNotFoundException extends Exception{
  public WorldPropertiesNotFoundException(String... properties) {
    super("Couldn't find the "+ Arrays.toString(properties) +" in the world properties, please check the code to see if it has been set.");
  }
}
