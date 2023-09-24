module com.whitewoodcity.fxcity {
  requires com.almasb.fxgl.all;
  requires javafx.media;
  requires com.fasterxml.jackson.databind;
  requires atlantafx.base;

  exports com.whitewoodcity.javafx.binding;
  exports com.whitewoodcity.flame;
  exports com.whitewoodcity.flame.pushnpoppane;
  exports com.whitewoodcity.fxgl.app;
  exports com.whitewoodcity.fxgl.app.scene;
  exports com.whitewoodcity.fxgl.service;
  exports com.whitewoodcity.fxgl.service.component;
  exports com.whitewoodcity.fxgl.service.icons.rankbadge;
  exports com.whitewoodcity.fxgl.service.icons;
  exports com.whitewoodcity.fxgl.service.exception;
  exports com.whitewoodcity.fxgl.sub3dview;
  exports com.whitewoodcity.fxgl.material;
  exports com.whitewoodcity.fxgl.dsl.components;
  exports com.whitewoodcity.fxgl.entity;
  exports com.whitewoodcity.fxgl.texture;
  exports com.whitewoodcity.fxgl.animation;

  exports com.whitewoodcity.fxgl.dsl;

  exports com.whitewoodcity.atlantafx.base.theme;
  opens com.whitewoodcity.atlantafx.base.theme;//for css&bss files
}