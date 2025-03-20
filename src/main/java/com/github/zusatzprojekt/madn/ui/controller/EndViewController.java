package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import javafx.fxml.FXML;

import java.io.IOException;

public class EndViewController implements FxmlController {
    private FxmlControllerConnector connector;

    @FXML
    public void quitGame() {
        connector.closeApplication();
    }

    @FXML
    public void switchToStartView() throws IOException {
        connector.loadScene("ui/start-view.fxml");
    }

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        this.connector = connector;
    }
}
