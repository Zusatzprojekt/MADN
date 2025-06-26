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

public class AppManager {
    private static Stage mainStage;
    private static Map<String, String> arguments;

    // == Helper methods ===============================================================================================

    private static void checkMainStageSet() {
        if (mainStage == null) {
            throw new RuntimeException(new NullPointerException("Static field 'mainStage' of AppManager not set"));
        }
    }

    private static void checkArgumentsSet() {
        if (mainStage == null) {
            throw new RuntimeException(new NullPointerException("Static field 'arguments' of AppManager not set"));
        }
    }


    // == Component loader =============================================================================================

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

    public static void loadScene(String fxmlFile) {
        sceneLoader(fxmlFile, null, null);
    }

    public static void loadScene(String fxmlFile, double width, double height) {
        sceneLoader(fxmlFile, width, height);
    }

    public static void loadScene(String fxmlFile, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile, null, null);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }

    public static void loadScene(String fxmlFile, double width, double height, Map<String, Object> values) {
        FXMLLoader loader = sceneLoader(fxmlFile, width, height);
        FxmlValueReceiver receiver = loader.getController();
        receiver.receiveValues(values);
    }


    // == Application management =======================================================================================

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
