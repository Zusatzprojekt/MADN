package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.ui.controller.StartmenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/game-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Mensch Ärgere Dich Nicht");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}