package com.whitewoodcity.flame;

import java.util.HashSet;

public class ImageSet extends HashSet<String> {
  public ImageSet put(String s) {
    add(s);
    return this;
  }
}
