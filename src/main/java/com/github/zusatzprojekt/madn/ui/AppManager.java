package com.github.zusatzprojekt.madn.ui;

import com.github.zusatzprojekt.madn.Resources;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Zentrale Utility-Klasse zur Verwaltung der Anwendung und zur Steuerung von FXML-basierten Komponenten und Szenen.
 * <p>
 * Diese Klasse kapselt globale Zustände wie {@code Stage} und Programmargumente sowie die Steuerung des Ladens
 * von Komponenten und Wechseln zwischen Szenen.
 * </p>
 *
 * <p><strong>Wichtige Funktionen:</strong></p>
 * <ul>
 *   <li>Laden und Setzen der Hauptszene</li>
 *   <li>Laden von FXML-Komponenten mit oder ohne Controller</li>
 *   <li>Sichere Verwaltung von {@code Stage} und Startparametern</li>
 * </ul>
 *
 * <p><strong>Hinweis:</strong> Alle Methoden sind statisch. Es soll keine Instanz dieser Klasse erzeugt werden.</p>
 */
public class AppManager {
    private static Stage mainStage;
    private static Map<String, String> arguments;

    // == Helper methods ===============================================================================================

    /**
     * Überprüft, ob die Haupt-Stage gesetzt wurde.
     *
     * @throws RuntimeException falls {@code mainStage} nicht gesetzt wurde
     */
    private static void checkMainStageSet() {
        if (mainStage == null) {
            throw new RuntimeException(new NullPointerException("Static field 'mainStage' of AppManager not set"));
        }
    }

    /**
     * Überprüft, ob die Start-Argumente gesetzt wurden.
     *
     * @throws RuntimeException falls {@code arguments} nicht gesetzt wurden
     */
    private static void checkArgumentsSet() {
        if (mainStage == null) {
            throw new RuntimeException(new NullPointerException("Static field 'arguments' of AppManager not set"));
        }
    }


    // == Component loader =============================================================================================

    /**
     * Lädt ein FXML-Fragment und setzt das Root-Objekt und den Controller manuell.
     *
     * @param name       Pfad zur FXML-Datei
     * @param root       Root-Node der Komponente
     * @param controller zu verwendender Controller
     * @throws RuntimeException bei Ladefehlern
     */
    public static void loadComponentFxml(String name, Object root, Object controller) {
        FXMLLoader loader = new FXMLLoader(Resources.getURL(name));
        loader.setRoot(requireNonNull(root));
        loader.setController(requireNonNull(controller));

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // == Main scene loader ============================================================================================

    /**
     * Lädt eine neue Szene in das Hauptfenster.
     *
     * @param fxmlFile Pfad zur FXML-Datei
     * @param width    Breite der Szene (null = automatisch)
     * @param height   Höhe der Szene (null = automatisch)
     * @return {@code FXMLLoader}, um z.B. auf den Controller zuzugreifen
     * @throws RuntimeException bei Ladefehlern
     */
    private static FXMLLoader sceneLoader(String fxmlFile, Double width, Double height) {
        checkMainStageSet();

        FXMLLoader loader = new FXMLLoader(Resources.getURL(fxmlFile));

        try {
            if (mainStage.getScene() == null) {
                mainStage.setScene(new Scene(loader.load(), width == null ? 100 : width, height == null ? 100 : height));
            } else {
                mainStage.setScene(new Scene(loader.load(), width == null ? mainStage.getScene().getWidth() : width, height == null ? mainStage.getScene().getHeight() : height));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loader;
    }

    /**
     * Lädt eine neue Szene ohne Parameter.
     *
     * @param fxmlFile Pfad zur FXML-Datei
     */
    public static void loadScene(String fxmlFile) {
        sceneLoader(fxmlFile, null, null);
    }

    /**
     * Lädt eine neue Szene mit fester Größe.
     *
     * @param fxmlFile Pfad zur FXML-Datei
     * @param width    gewünschte Breite
     * @param height   gewünschte Höhe
     */
    public static void loadScene(String fxmlFile, double width, double height) {
        sceneLoader(fxmlFile, width, height);
    }

    /**
     * Lädt eine neue Szene und übergibt initiale Werte an den Controller (falls er {@code FxmlValueReceiver} implementiert).
     *
     * @param fxmlFile Pfad zur FXML-Datei
     * @param values   Daten zur Übergabe an die Zielszene
     */
    public static void loadScene(String fxmlFile, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile, null, null);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }

    /**
     * Lädt eine neue Szene mit fester Größe und Übergabewerten.
     *
     * @param fxmlFile Pfad zur FXML-Datei
     * @param width    gewünschte Breite
     * @param height   gewünschte Höhe
     * @param values   Daten zur Übergabe an die Zielszene
     */
    public static void loadScene(String fxmlFile, double width, double height, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile, width, height);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }


    // == Application management =======================================================================================


    /**
     * Schließt die Anwendung
     */
    public static void closeApplication() {
        checkMainStageSet();

        mainStage.close();
    }


    // == Getter / Setter ==============================================================================================

    public static Stage getMainStage() {
        checkMainStageSet();

        return mainStage;
    }

    public static void setMainStage(Stage stage) {
        if (mainStage == null) {
            mainStage = stage;
        } else {
            throw new RuntimeException(new IllegalAccessError("Static field 'mainStage' of AppManager already set; Can only be set once"));
        }
    }

    public static Map<String, String> getArguments() {
        checkArgumentsSet();

        return arguments;
    }

    public static void setArguments(Map<String, String> args) {
        if (arguments == null) {
            arguments = Collections.unmodifiableMap(args);
        } else {
            throw new RuntimeException(new IllegalAccessError("Static field 'arguments' of AppManager already set; Can only be set once"));
        }
    }

}
