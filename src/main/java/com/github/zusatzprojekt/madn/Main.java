package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerValueReceiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Map;

public class Main extends Application implements FxmlControllerConnector2 {
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

    private FXMLLoader sceneLoader(String fxmlFile, Double width, Double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        if (mainScene == null){
            mainScene = new Scene(loader.load(), width == null ? 100 : width, height == null ? 100 : height);
        } else {
            Stage mainStage = (Stage) mainScene.getWindow();
            mainScene = new Scene(loader.load(), width == null ? mainScene.getWidth() : width, height == null ? mainScene.getHeight() : height);
            mainStage.setScene(mainScene);
        }

        ((FxmlController) loader.getController()).setConnector(this);
        return loader;
    }

    @Override
    public void loadScene(String fxmlFile, double width, double height) throws IOException {
        sceneLoader(fxmlFile,width,height);
    }

    @Override
    public void loadScene(String fxmlFile) throws IOException {
        sceneLoader(fxmlFile,null,null);
    }

    @Override
    public void loadScene(String fxmlFile, double width, double height, Map<String, Object> values) throws IOException {
        FXMLLoader loader = sceneLoader(fxmlFile,width,height);
        FxmlControllerValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }

    @Override
    public void loadScene(String fxmlFile, Map<String, Object> values) throws IOException {
        FXMLLoader loader = sceneLoader(fxmlFile,null,null);
        FxmlControllerValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }
}