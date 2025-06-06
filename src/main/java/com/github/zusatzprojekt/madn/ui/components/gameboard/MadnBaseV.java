package com.github.zusatzprojekt.madn.ui.components.gameboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * MadnBaseV repräsentiert die Felder der Parkpositionen auf dem Spielbrett.
 * Die Klasse ist UI-bezogen und basiert auf einem FXML-Layout, das sie mit der Superklasse lädt.
 */
public class MadnBaseV extends MadnFieldContainerExtV {
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font("Arial Bold", 166.66));

    @FXML
    private StackPane textContainer;
    @FXML
    private Text baseText;


    // == Constructor ==================================================================================================

    /**
     * Konstruktor lädt das zugehörige FXML und richtet Verbindungen ein.
     */
    public MadnBaseV() {
        super("ui/components/gameboard/madn-base-v.fxml");

        createBindings();
    }


    // == Bindings =====================================================================================================

    /**
     * Erstellt Verbindungen zwischen UI-Elementen und Eigenschaften.
     * Ziel ist es, Layout und Stil automatisch an Größenänderungen anzupassen.
     */
    private void createBindings() {
        MadnFieldV[] fields = getFields();

        // Eigenschaften der Felder
        for (int i = 0; i < fields.length; i++) {
            fields[i].layoutXProperty().bind(radius.multiply(2.0 * (i % 2)).add(spacing.multiply(i % 2)));
            fields[i].layoutYProperty().bind(radius.multiply(i > 1 ? 2.0 : 0.0).add(spacing.multiply(i > 1 ? 1 : 0)));
        }

        baseText.fontProperty().bind(font);

        textContainer.prefWidthProperty().bind(radius.multiply(4.0).add(spacing));
        textContainer.prefHeightProperty().bind(radius.multiply(4.0).add(spacing));

        radius.addListener((observableValue, oldValue, value) -> {
            font.setValue(new Font("Arial Bold", value.doubleValue() / 0.6));
        });

    }

}
