package com.whitewoodcity.atlantafx.base.theme;

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
    return "/com/whitewoodcity/atlantafx/base/theme/city-dark.css";
  }

  @Override
  public String getUserAgentStylesheetBSS() {
    return "/com/whitewoodcity/atlantafx/base/theme/city-dark.bss";
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
