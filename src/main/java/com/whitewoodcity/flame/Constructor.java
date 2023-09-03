package com.whitewoodcity.flame;

import com.whitewoodcity.flame.pushnpoppane.PushAndPop;

import java.util.Map;

@FunctionalInterface
public interface Constructor {
  PushAndPop build(Map<String, Object> json);
}
