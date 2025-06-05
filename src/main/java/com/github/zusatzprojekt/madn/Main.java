package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Hauptklasse der JavaFX-Anwendung "Mensch Ärgere Dich Nicht".
 * Diese Klasse startet die GUI und lädt die Startansicht.
 */
public class Main extends Application {

    /**
     * Einstiegspunkt der JavaFX-Anwendung.
     * Diese Methode wird automatisch von JavaFX aufgerufen, sobald die Applikation startet.
     *
     * @param stage Das Hauptfenster (Stage) der Anwendung.
     */
    @Override
    public void start(Stage stage) {
        // Übergibt das Hauptfenster (Stage) an den UIManager
        UIManager.setMainStage(stage);

        // Lädt die Startszene aus der FXML-Datei mit angegebener Breite und Höhe
        UIManager.loadScene("ui/start-view.fxml", 1280.0, 720.0);

        // Setzt die Startparameter
        stage.setTitle("Mensch Ärgere Dich Nicht");
        stage.getIcons().add(new Image(Resources.getStream("images/madn_icon.png")));
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.show();
    }

    /**
     * Startpunkt der Anwendung.
     * Ruft die JavaFX-Startmethode auf.
     *
     * @param args Kommandozeilenargumente (nicht verwendet).
     */
    public static void main(String[] args) {
        launch(args);
    }

}