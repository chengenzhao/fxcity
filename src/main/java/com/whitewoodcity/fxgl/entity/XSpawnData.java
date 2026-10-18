package com.whitewoodcity.fxgl.entity;

import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class XSpawnData extends SpawnData {
  public XSpawnData(double x, double y) {
    super(x, y);
  }

  public XSpawnData(double x) {
    super(x);
  }

  public XSpawnData() {
  }

  public XSpawnData(double x, double y, double z) {
    super(x, y, z);
  }

  public XSpawnData(Point2D position) {
    super(position);
  }

  public XSpawnData(Point3D position) {
    super(position);
  }

  public <V> XSpawnData(String k, V v) {
    super();
    put(k,v);
  }
}
