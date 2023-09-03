package com.whitewoodcity.fxgl.service;

public interface ThemeService {
  default Theme getTheme(){
    return Theme.DARK;
  }

  default void setTheme(Theme theme){}
}
