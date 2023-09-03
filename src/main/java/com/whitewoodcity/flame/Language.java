package com.whitewoodcity.flame;

public enum Language{
  CHINESE("zh"), ENGLISH("en");

  private String lan;

  Language(String lan) {
    this.lan = lan;
  }

  @Override
  public String toString() {
    return lan;
  }
}