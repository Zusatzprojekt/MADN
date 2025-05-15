package com.github.zusatzprojekt.madn.ui;

import com.github.zusatzprojekt.madn.Resources;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class UILoader {
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
}
