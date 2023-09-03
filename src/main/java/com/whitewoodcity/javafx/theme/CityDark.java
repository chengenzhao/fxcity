package com.whitewoodcity.javafx.theme;

import atlantafx.base.theme.Theme;

public final class CityDark implements Theme {
  public CityDark() {
  }

  @Override
  public String getName() {
    return "City Dark";
  }

  @Override
  public String getUserAgentStylesheet() {
    return "/com/whitewoodcity/javafx/theme/city-dark.css";
  }

  @Override
  public String getUserAgentStylesheetBSS() {
    return "/com/whitewoodcity/javafx/theme/city-dark.bss";
  }

  @Override
  public boolean isDarkMode() {
    return true;
  }

  @Override
  public boolean isDefault() {
    return Theme.super.isDefault();
  }
}
