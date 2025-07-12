package com.github.zusatzprojekt.madn;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hauptklasse der JavaFX-Anwendung "Mensch ärgere Dich nicht".
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
        AppManager.setMainStage(stage);

        // Übergibt Commandline-Argumente
        HashMap<String, String> args = new HashMap<>();
        getParameters().getNamed().forEach((argKey, argValue) -> args.put(argKey, argValue.strip().toLowerCase()));
        getParameters().getUnnamed().forEach(arg -> {
            if (arg.startsWith("--") && arg.length() > 2) {
                args.put(arg.substring(2), "true");
            }
        });
        AppManager.setArguments(args);

        // Lädt die Startszene aus der FXML-Datei mit angegebener Breite und Höhe
        AppManager.loadScene("ui/start-view.fxml", 1280.0, 720.0);

        List<String> rawArgs = getParameters().getRaw();
        Map<String, String> namedArgs = getParameters().getNamed();

        // Setzt die Startparameter
        stage.setTitle("Mensch ärgere Dich nicht");
        stage.getIcons().add(new Image(Resources.getStream("images/madn_icon.png")));
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.show();
    }

    /**
     * Startpunkt der Anwendung.
     * Ruft die JavaFX-Startmethode auf.
     *
     * @param args Kommandozeilenargumente.
     */
    public static void main(String[] args) {
        launch(args);
    }

}