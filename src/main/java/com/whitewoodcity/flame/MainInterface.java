package com.whitewoodcity.flame;

import javafx.scene.layout.StackPane;

public interface MainInterface {
    Navigator routes = new Navigator("/");
    StackPane root = new StackPane();

    static Navigator getRoutes() {
        return routes;
    }

    static StackPane getRoot() {
        return root;
    }
}
