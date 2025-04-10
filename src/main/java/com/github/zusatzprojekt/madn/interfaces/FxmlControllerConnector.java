package com.github.zusatzprojekt.madn.interfaces;

import java.io.IOException;

public interface FxmlControllerConnector {
    void closeApplication();
    void loadScene(String fxmlFile, double width, double height);
    void loadScene(String fxmlFile);
}
