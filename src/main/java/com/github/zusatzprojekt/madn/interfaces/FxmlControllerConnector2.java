package com.github.zusatzprojekt.madn.interfaces;

import javafx.stage.Window;

import java.io.IOException;
import java.util.Map;

public interface FxmlControllerConnector2 extends FxmlControllerConnector{
    void loadScene(String fxmlFile, double width, double height, Map<String, Object> values);
    void loadScene(String fxmlFile, Map<String, Object> values);
    Window getWindow();
}
