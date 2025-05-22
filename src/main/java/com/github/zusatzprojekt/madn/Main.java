package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.IOException;
import java.util.Map;
import static java.util.Objects.requireNonNull;

public class Main extends Application implements FxmlControllerConnector2 {
    private Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        loadScene("ui/start-view.fxml", 1280, 720);

        stage.setTitle("Mensch Ärgere Dich Nicht");
        stage.getIcons().add(new Image(Resources.getStream("images/madn_icon.png")));
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

    private FXMLLoader sceneLoader(String fxmlFile, Double width, Double height) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        if (mainScene == null){
            try {
                mainScene = new Scene(loader.load(), width == null ? 100 : width, height == null ? 100 : height);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Stage mainStage = (Stage) mainScene.getWindow();

            try {
                mainScene = new Scene(loader.load(), width == null ? mainScene.getWidth() : width, height == null ? mainScene.getHeight() : height);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainStage.setScene(mainScene);
        }

        ((FxmlController) loader.getController()).setConnector(this);
        return loader;
    }

    @Override
    public void loadScene(String fxmlFile, double width, double height) {
        sceneLoader(fxmlFile,width,height);
    }

    @Override
    public void loadScene(String fxmlFile) {
        sceneLoader(fxmlFile,null,null);
    }

    @Override
    public void loadScene(String fxmlFile, double width, double height, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile,width,height);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }

    @Override
    public void loadScene(String fxmlFile, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile,null,null);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }

    @Override
    public Window getWindow() {
        return mainScene.getWindow();
    }

}