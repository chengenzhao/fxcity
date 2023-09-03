package com.whitewoodcity.flame;

public enum Country {
  CHINA("CN"), USA("US");

  private String country;

  Country(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return country;
  }
}
