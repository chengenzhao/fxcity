package com.whitewoodcity.fxgl.app;

import com.almasb.fxgl.core.util.Platform;

public record RuntimeInfo(
  Platform platform,
  String version,
  String build
) {
}
