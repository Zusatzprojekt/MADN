package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Tell the UI-manager this is the main stage
        UIManager.setMainStage(stage);

        // Load start scene
        UIManager.loadScene("ui/start-view.fxml", 1280.0, 720.0);

        // Set start parameters
        stage.setTitle("Mensch Ärgere Dich Nicht");
        stage.getIcons().add(new Image(Resources.getStream("images/madn_icon.png")));
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}