package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application implements FxmlControllerConnector {
    Scene mainScene;
    @Override
    public void start(Stage stage) throws IOException {
        loadScene("ui/start-view.fxml", 1280, 720);

        stage.setTitle("Mensch Ärgere Dich Nicht");
        stage.setScene(mainScene);
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void closeApplication() {
        ((Stage) mainScene.getWindow()).close();
    }

    private void sceneLoader(String fxmlFile, Double width, Double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        if (mainScene == null){
            mainScene = new Scene(loader.load(), width == null ? 100 : width, height == null ? 100 : height);
        } else {
            Stage mainStage = (Stage) mainScene.getWindow();
            mainStage.setWidth(width == null ? mainScene.getWidth() : width);
            mainStage.setHeight(height == null ? mainScene.getHeight() : height);
            mainScene.setRoot(loader.load());
        }

        ((FxmlController) loader.getController()).setConnector(this);
    }

    @Override
    public void loadScene(String fxmlFile, double width, double height) throws IOException {
        sceneLoader(fxmlFile,width,height);
    }

    @Override
    public void loadScene(String fxmlFile) throws IOException {
        sceneLoader(fxmlFile,null,null);
    }
}